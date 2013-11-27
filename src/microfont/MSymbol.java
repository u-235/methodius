
package microfont;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import microfont.undo.MSymbolUndo;

/**
 * Класс MSymbol для хранения и изменения символа {@link MFont шрифта}.<br>
 * Символ имеет ширину и высоту в пикселях, а так же индекс (номер) символа в
 * шрифте. Ключевое свойство символа - массив пикселей.<br>
 * Важно знать, что хотя символ и является разделяемым ресурсом, но один и тот
 * же символ не может принадлежать различным шрифтам.
 */
public class MSymbol extends PixselMap {
    /**  */
    protected MFont     parent;
    protected MSymbol   prevSymbol = null;
    protected MSymbol   nextSymbol = null;
    /** Индекс символа в шрифте. */
    private int         code;

    private MSymbolUndo undo;

    /**
     * Конструктор символа с заданными размерами, индексом и копированием
     * массива <b>byte</b>. <br>
     * Если копируемый массив меньше количества пикселей, то оставшиеся пиксели
     * будут установлены в ноль.
     * 
     * @param i Индекс символа в шрифте.
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     * @param a Массив для копирования.
     */
    public MSymbol(int i, int w, int h, byte[] a) {
        super(w, h, a);
        if (i < 0) throw (new IllegalArgumentException("Invalid code"));
        code = i;
    }

    public MSymbol(MSymbol src) {
        super(src);
        code = src.code;
    }

    @Override
    protected boolean isValidHeight(int h) {
        if (parent != null) return parent.isValidHeight(h);
        return super.isValidHeight(h);
    }

    @Override
    protected boolean isValidWidth(int w) {
        if (parent != null) return parent.isValidWidth(w);
        return super.isValidWidth(w);
    }
    
    protected boolean isValidCode(int code) {
        if (parent != null) return parent.isValidCode(code);
        return true;
    }

    /**
     * Копирование массива точек из символа s. Так же изменяются переменные
     * {@link #code code}. <br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.
     * 
     * @param s Источник копирования.
     * @throws DisallowOperationException
     * @throws IllegalArgumentException
     * @see #clone()
     */
    public void copy(MSymbol s) throws IllegalArgumentException,
                    DisallowOperationException {
        if (s == null) throw (new NullPointerException());

        setCode(s.getCode());
        super.copy(s);
    }

    /**
     * Сравнение символов. Символы считаются равными, если у них совпадают
     * индекс, ширина, высота и содержимое массивов пикселей.
     * 
     * @param s Символ для сравнения.
     * @return <b>true</b> если символы равны.
     * @Override public boolean equals(Object s) { MSymbol sym;
     * 
     *           if (s == null) { return false; }
     * 
     *           if (this.getClass() != s.getClass()) { return false; }
     * 
     *           sym=(MSymbol) s; return this.equals(sym); }
     */

    /**
     * Сравнение символов. Символы считаются равными, если у них совпадают
     * индекс, ширина, высота и содержимое массивов пикселей.
     * 
     * @param s Символ для сравнения.
     * @return <b>true</b> если символы равны.
     */
    @Override
    public boolean equals(Object s) {
        if (!(s instanceof MSymbol)) return false;
        MSymbol sym = (MSymbol) s;
        return super.equals(sym) && code == sym.code;
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     */
    public int getCode() {
        return code;
    }

    /**
     * Метод устанавливает индекс символа в шрифте..
     * 
     * @param i Новый индекс.
     * @throws DisallowOperationException
     */
    public void setCode(int i) throws IllegalArgumentException,
                    DisallowOperationException {
        if (code == i) return;

        if (!isValidCode(i))
            throw new DisallowOperationException("change code");

        code = i;
    }

    /**
     * Возвращает шрифт, к которому принадлежит символ.
     * 
     * @return Шрифт или <b>null</b> если символ не принадлежит ни одному
     *         шрифту.
     */
    public MFont getParent() {
        return parent;
    }

    protected void fireUndoEvent(UndoableEditEvent change) {
        Object[] listenerArray;

        if (listeners == null) return;
        System.out.println("MSymbol: fire undo event");

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i++) {
            if (listenerArray[i] == UndoableEditListener.class)
                ((UndoableEditListener) listenerArray[i + 1])
                                .undoableEditHappened(change);
        }
    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        listeners.add(UndoableEditListener.class, listener);
    }

    public void removeUndoableEditListener(UndoableEditListener listener) {
        listeners.remove(UndoableEditListener.class, listener);
    }

    public synchronized void beginChange(String operation) {
        if (undo != null) return;

        undo = new MSymbolUndo(this, operation);
    }

    public synchronized void endChange() {
        if (undo == null) return;

        undo.end();

        if (undo.canUndo()) fireUndoEvent(new UndoableEditEvent(this, undo));
        undo = null;
    }
}
