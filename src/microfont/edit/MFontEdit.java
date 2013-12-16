
package microfont.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.undo.UndoableEdit;
import microfont.MFont;
import microfont.MSymbol;

public class MFontEdit extends AbstractEdit implements PropertyChangeListener {
    protected MFont font;
    private Item[]  table;

    public MFontEdit(MFont mf, String operation) {
        super(operation);

        table = new Item[0];

        font = mf;

        if (font != null) {
            font.addPropertyChangeListener(this);
        }
    }

    @Override
    public void undo() {
        super.undo();

        for (Item i : table) {
            if (i.newValue instanceof MSymbolEdit) {
                ((MSymbolEdit) i.newValue).undo();
            } else if (i.name.equals(MFont.PROPERTY_SYMBOLS)) {
                if (i.newValue != null) font.remove((MSymbol)i.newValue);
                if (i.oldValue != null) font.add((MSymbol)i.oldValue);
            } else {
                font.setProperty(i.name, i.oldValue);
            }
        }
    }

    @Override
    public void redo() {
        super.redo();

        for (Item i : table) {
            if (i.newValue instanceof MSymbolEdit) {
                ((MSymbolEdit) i.newValue).redo();
            } else if (i.name.equals(MFont.PROPERTY_SYMBOLS)) {
                if (i.oldValue != null) font.remove((MSymbol)i.oldValue);
                if (i.newValue != null) font.add((MSymbol)i.newValue);
            } else {
                font.setProperty(i.name, i.newValue);
            }
        }
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (anEdit == null || !(anEdit instanceof MSymbolEdit)) return false;

        add(null, null, anEdit);

        return true;
    }

    @Override
    public void end() {
        if (font != null) {
            font.removePropertyChangeListener(this);
        }

        if (table.length == 0) die();

        for (Item i : table) {
            if (i.newValue instanceof MSymbolEdit) {
                ((MSymbolEdit) i.newValue).end();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() != font) return;

        add(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    protected synchronized void add(String prop, Object oldValue,
                    Object newValue) {
        Item[] t = new Item[table.length + 1];
        System.arraycopy(table, 0, t, 0, table.length);
        t[table.length] = new Item(prop, oldValue, newValue);
        table = t;
    }

    protected class Item {
        String name;
        Object oldValue;
        Object newValue;

        protected Item(String name, Object oldValue, Object newValue) {
            this.name = name;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }
}
