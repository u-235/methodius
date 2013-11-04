package microfont.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.Timer;

import microfont.MFont;
import microfont.MSymbol;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;

/**
 * Класс для представления {@linkplain MFont шрифта} в JList.
 * 
 */
public class MListModel extends AbstractListModel<MSymbol> implements
                MFontListener
{
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
            this.font.removeListener(this);
        }

        this.font = font;

        fireIntervalRemoved(this, 0, oldInd);

        if (font != null) {
            font.addListener(this);
            fireIntervalAdded(this, 0, font.getSize() - 1);
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
    public void mFontEvent(MFontEvent change) {
        MFont font;
        int first, last;

        font = (MFont) change.getSource();
        switch (change.getReason()) {
        case MFontEvent.FONT_SYMBOL_ADDED:
        case MFontEvent.FONT_SYMBOL_REMOVE:
        case MFontEvent.FONT_CHARSET:
            first = 0;
            last = font.getSize() - 1;
            break;
        case MFontEvent.FONT_SYMBOL_CHANGED:
            delayUpdate(change.getIndex());
            return;
        default:
            first = 0;
            last = font.getSize() - 1;
        }
        fireContentsChanged(this, first, last);
    }
}
