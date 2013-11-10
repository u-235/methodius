package microfont.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public abstract class AbstractUndo implements UndoableEdit
{
    private static final String DEFAULT_UNDO_NAME = "Undo";
    private static final String DEFAULT_REDO_NAME = "Redo";
    private static String       undoName          = DEFAULT_UNDO_NAME;
    private static String       redoName          = DEFAULT_REDO_NAME;

    private String              operation;
    protected boolean           isUndo;
    protected boolean           isEmpty;

    public AbstractUndo(String operation) {
        this.operation = operation;
        isUndo = true;
        isEmpty = false;
    }

    @Override
    public void undo() throws CannotUndoException {
        if (!canUndo()) throw new CannotUndoException();
        isUndo = false;
    }

    @Override
    public boolean canUndo() {
        return isUndo && !isEmpty;
    }

    @Override
    public void redo() throws CannotRedoException {
        if (!canRedo()) throw new CannotRedoException();
        isUndo = true;
    }

    @Override
    public boolean canRedo() {
        return !isUndo && !isEmpty;
    }

    @Override
    public void die() {
        isEmpty = true;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getPresentationName() {
        return operation;
    }

    @Override
    public String getUndoPresentationName() {
        if (operation != null) return undoName + " " + operation;
        return undoName;
    }

    public static void setUndoPresentationName(String name) {
        if (name != null) undoName = name;
        else undoName = DEFAULT_UNDO_NAME;
    }

    @Override
    public String getRedoPresentationName() {
        if (operation != null) return redoName + " " + operation;
        return redoName;
    }

    public static void setRedoPresentationName(String name) {
        if (name != null) redoName = name;
        else redoName = DEFAULT_REDO_NAME;
    }

    public abstract void end();
}
