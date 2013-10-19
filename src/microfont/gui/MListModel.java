package microfont.gui;

import javax.swing.AbstractListModel;

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

    public MListModel() {
        // this.setFont(new MFont());
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

        if (font == null) fireContentsChanged(this, 0, oldInd);
        else {
            this.font.addListener(this);
            fireContentsChanged(this, 0, font.getSize() - 1);
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
            first = change.getIndex();
            last = first;
            break;
        default:
            first = 0;
            last = font.getSize() - 1;
            // return;
        }
        fireContentsChanged(this, first, last);
    }
}
