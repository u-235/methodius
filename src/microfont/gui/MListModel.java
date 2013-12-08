
package microfont.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;
import javax.swing.Timer;
import microfont.MFont;
import microfont.MSymbol;

/**
 * Класс для представления {@linkplain MFont шрифта} в JList.
 * 
 */
public class MListModel extends AbstractListModel<MSymbol> implements
                PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    /** */
    private MFont             font;
    private Timer             delay;
    private int               firstIndex, lastIndex;

    public MListModel() {
        final MListModel p = this;
        delay = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (this) {
                    fireContentsChanged(p, firstIndex, firstIndex);
                    firstIndex = Integer.MAX_VALUE;
                    lastIndex = -1;
                }
            }
        });
        delay.setRepeats(false);
    }

    private void delayUpdate(int index) {
        synchronized (delay) {
            firstIndex = firstIndex > index ? index : firstIndex;
            lastIndex = lastIndex < index ? index : lastIndex;
            if (!delay.isRunning()) delay.start();
        }
    }

    /**
     * 
     * @return
     */
    public MFont getFont() {
        return font;
    }

    /**
     * 
     * @param font
     */
    public void setFont(MFont font) {
        int oldInd = 0;

        if (this.font != null) {
            oldInd = this.font.getSize() - 1;
            this.font.removePropertyChangeListener(this);
        }

        this.font = font;

        if (oldInd > 0) fireIntervalRemoved(this, 0, oldInd);

        if (font != null) {
            font.addPropertyChangeListener(this);
            int i = this.font.getSize() - 1;
            if (i < 0) i = 0;
            fireIntervalAdded(this, 0, i);
        }
    }

    @Override
    public int getSize() {
        if (font == null) return 0;

        return font.getSize();
    }

    @Override
    public MSymbol getElementAt(int index) {
        if (font == null) return null;
        if (index < 0) index = 0;
        if (index >= font.getSize()) index = font.getSize();
        return font.symbolAtNumber(index);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        MFont font;

        font = (MFont) event.getSource();

        fireContentsChanged(this, 0, font.getSize() - 1);
    }
}
