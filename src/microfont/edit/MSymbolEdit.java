
package microfont.edit;

import microfont.DisallowOperationException;
import microfont.MSymbol;

public class MSymbolEdit extends AbstractEdit {
    MSymbol owner;
    MSymbol before;
    MSymbol after;

    public MSymbolEdit(MSymbol mSymbol, String operation) {
        super(operation);
        owner = mSymbol;
        before = new MSymbol(mSymbol);
    }

    @Override
    public void undo() {
        super.undo();
        try {
            owner.copy(before);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DisallowOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        super.redo();
        try {
            owner.copy(after);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DisallowOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void die() {
        super.die();
        owner = null;
        before = null;
        after = null;
    }

    @Override
    public void end() {
        super.end();

        if (owner.equals(before)) {
            die();
            return;
        }

        after = new MSymbol(owner);
    }
}
