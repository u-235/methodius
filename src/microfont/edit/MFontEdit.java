
package microfont.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.undo.UndoableEdit;
import microfont.MFont;
import microfont.MSymbol;

/**
 * Отменяемая операция для {@linkplain MFont шрифта}.
 * 
 * @author Nick Egorov
 */
public class MFontEdit extends AbstractEdit implements PropertyChangeListener {
    final static String NESTED_FIXSED = "MFontEditNestedFixset";
    final static String NESTED_HEIGHT = "MFontEditNestedHeight";
    final static String NESTED_WIDTH  = "MFontEditNestedWidth";

    protected MFont     font;
    private Item[]      table;
    private MFontEdit   nested;

    public MFontEdit(MFont mf, String operation) {
        super(operation);

        nested = null;
        table = new Item[0];
        font = mf;

        if (font != null) {
            font.addPropertyChangeListener(this);
        }
    }

    @Override
    public void undo() {
        super.undo();

        for (int i = table.length - 1; i >= 0; i--) {
            Item e = table[i];

            if (e.newValue instanceof AbstractEdit) {
                ((AbstractEdit) e.newValue).undo();
            } else if (e.name.equals(MFont.PROPERTY_SYMBOLS)) {
                if (e.newValue != null) font.remove((MSymbol) e.newValue);
                if (e.oldValue != null) font.add((MSymbol) e.oldValue);
            } else {
                font.setProperty(e.name, e.oldValue);
            }
        }
    }

    @Override
    public void redo() {
        super.redo();

        for (Item i : table) {
            if (i.newValue instanceof AbstractEdit) {
                ((AbstractEdit) i.newValue).redo();
            } else if (i.name.equals(MFont.PROPERTY_SYMBOLS)) {
                if (i.oldValue != null) font.remove((MSymbol) i.oldValue);
                if (i.newValue != null) font.add((MSymbol) i.newValue);
            } else {
                font.setProperty(i.name, i.newValue);
            }
        }
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (nested != null) {
            nested.addEdit(anEdit);
        } else {
            add(null, null, anEdit);
        }
        return true;
    }

    @Override
    public void end() {
        if (font != null) {
            font.removePropertyChangeListener(this);
        }

        if (table.length > 0
                        && table[table.length - 1].newValue instanceof AbstractEdit) {
            AbstractEdit ae = (AbstractEdit) table[table.length - 1].newValue;
            ae.end();
            if (!ae.canUndo()) {
                Item[] t = new Item[table.length - 1];
                System.arraycopy(table, 0, t, 0, t.length);
                table = t;
            }
        }

        if (table.length == 0) die();

        for (Item i : table) {
            if (i.newValue instanceof AbstractEdit) {
                ((AbstractEdit) i.newValue).end();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() != font) return;
        String name = evt.getPropertyName();

        if (name.equals(MFont.PROPERTY_FIXSED)) {
            addNested(new MFontEdit(font, NESTED_FIXSED), evt);
        } else if (name.equals(MFont.PROPERTY_HEIGHT)) {
            addNested(new MFontEdit(font, NESTED_HEIGHT), evt);
        } else if (name.equals(MFont.PROPERTY_WIDTH)) {
            addNested(new MFontEdit(font, NESTED_WIDTH), evt);
        } else {
            if (nested != null) {
                nested.end();
                nested = null;
            }
            add(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    protected synchronized void add(String prop, Object oldValue,
                    Object newValue) {
        boolean resize = true;

        if (table.length > 0
                        && table[table.length - 1].newValue instanceof AbstractEdit) {
            AbstractEdit ae = (AbstractEdit) table[table.length - 1].newValue;
            ae.end();
            if (!ae.canUndo()) resize = false;
        }

        Item[] t = table;
        if (resize) {
            t = new Item[table.length + 1];
            System.arraycopy(table, 0, t, 0, table.length);
        }
        t[t.length - 1] = new Item(prop, oldValue, newValue);
        table = t;
    }

    protected void addNested(MFontEdit mfe, PropertyChangeEvent evt) {
        font.removePropertyChangeListener(mfe);

        if (nested != null && !mfe.getPresentationName().equals(NESTED_WIDTH)
                        && !nested.getPresentationName().equals(NESTED_FIXSED)) {
            System.out.println("nested ends");
            nested.end();
            nested = null;
        }

        if (nested == null) {
            addEdit(mfe);
            nested = mfe;
            nested.add(evt.getPropertyName(), evt.getOldValue(),
                            evt.getNewValue());
        } else {
            nested.addNested(mfe, evt);
        }
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
