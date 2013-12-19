
package microfont.edit;

import microfont.MFont;

/**
 * Отменяемая операция для {@linkplain MFont шрифта}.
 * 
 * @author Nick Egorov
 */
public class MFontEdit extends AbstractEdit {
    MFont owner;
    MFont before;
    MFont after;

    public MFontEdit(MFont mf, String operation) {
        super(operation);
        owner = mf;
        before = new MFont(mf);
    }

    @Override
    public void undo() {
        super.undo();

        owner.copy(before);
    }

    @Override
    public void redo() {
        super.redo();

        owner.copy(after);
    }
    
    @Override
    public void die() {
        super.die();
        owner=null;
        before=null;
        after=null;
    }

    @Override
    public void end() {
        super.end();

        if (owner.equals(before)) {
            die();
            return;
        }

        after = new MFont(owner);
    }
}
