
package microfont;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import microfont.edit.MFontEdit;
import microfont.edit.MSymbolEdit;
import utils.event.ListenerChain;

/**
 * 
 *
 */
public class Document {
    private ListenerChain listeners = new ListenerChain();
    private MFont         font;
    /** Объект с изменениями шрифта. */
    private MFontEdit     undoFont;
    /** Объект с изменениями символа. */
    private MSymbolEdit   undoSymbol;

    public void addUndoableEditListener(UndoableEditListener listener) {
        listeners.add(UndoableEditListener.class, listener);
    }

    public void removeUndoableEditListener(UndoableEditListener listener) {
        listeners.remove(UndoableEditListener.class, listener);
    }

    protected void fireUndoEvent(UndoableEditEvent change) {
        Object[] listenerArray;

        if (listeners == null) return;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == UndoableEditListener.class)
                ((UndoableEditListener) listenerArray[i + 1])
                                .undoableEditHappened(change);
        }
    }

    public synchronized void fontEdit(String operation) {
        endEdit();
        undoFont = new MFontEdit(font, operation);
    }

    public synchronized void symbolEdit(MSymbol sym, String operation) {
        endEdit();
        undoSymbol = new MSymbolEdit(sym, operation);
    }

    public synchronized void nestedEdit(MSymbol sym) {
        if (undoFont == null) return;
        undoFont.addEdit(new MSymbolEdit(sym, null));
    }

    public synchronized void endEdit() {
        if (undoFont != null) {
            undoFont.end();
            fireUndoEvent(new UndoableEditEvent(this, undoFont));
        } else if (undoSymbol != null) {
            undoSymbol.end();
            fireUndoEvent(new UndoableEditEvent(this, undoSymbol));
        }

        undoFont = null;
        undoSymbol = null;
    }

    public MFont getFont() {
        return font;
    }

    public synchronized void setFont(MFont mf) {
        if (font != null) {
            endEdit();
            font.document = null;
        }

        font = mf;

        if (font != null) {
            font.document = this;
        }
    }
}
