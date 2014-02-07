
package microfont.edit;

import java.util.logging.Level;
import microfont.AbstractMFont;
import microfont.DisallowOperationException;
import microfont.MSymbol;

public class MSymbolEdit extends AbstractEdit {
    MSymbol owner;
    MSymbol before;
    MSymbol after;

    public MSymbolEdit(MSymbol mSymbol, String operation) {
        super(operation);
        owner = mSymbol;
        before = mSymbol.clone();
    }

    @Override
    public void undo() {
        super.undo();
        try {
            owner.copy(before);
        } catch (NullPointerException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in undo", e);
        } catch (DisallowOperationException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in undo", e);
        }
    }

    @Override
    public void redo() {
        super.redo();
        try {
            owner.copy(after);
        } catch (NullPointerException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in redo", e);
        } catch (DisallowOperationException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in redo", e);
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

        after = owner.clone();
    }
}
