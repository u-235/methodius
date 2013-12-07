
package microfont;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import microfont.undo.MSymbolUndo;

/**
 * Класс MSymbol для хранения и изменения символа {@link MFont шрифта}.<br>
 * Важно знать, что хотя символ и является разделяемым ресурсом, но один и тот
 * же символ не может принадлежать различным шрифтам.
 * 
 * <h3>Код символа.</h3>
 * <p>
 * Код символа - это индекс символа в кодовой странице шрифта. Для большинства
 * кодовых страниц это число в диаппазоне от 0 до 255. И для разных кодировок
 * один и тот же символ может иметь различный код. Это может стать причиной
 * затруднений при преобразовании шрифта из одной кодировки в другую.
 * <p>
 * Поэтому символ имеет два свойства:
 * <ol>
 * <li>{@link #code} - соответствует коду символа в кодовой странице шрифта. Это
 * свойство доступно через {@link #getCode()} {@link #setCode(int)}
 * <li>{@link #unicode} - код символа в UTF-16, независящий от кодировки.
 * MSymbol может изменять это свойство только один раз вызвав
 * {@link #setUnicode(int)}, пока переменная {@link #hasUnicode} равна
 * <code>false</code>, в дальнейшем возможно только получение значения методом
 * {@link #getUnicode()}. Более того, это свойство может быть неактуальным, это
 * можно проверить при помощи {@link #isUnicode()}. *
 * </ol>
 * 
 * <h3>Поддержка отменяемых действий.</h3>
 * <p>
 */
public class MSymbol extends PixselMap {
    /** Шрифт, к которому принадлежит символ. */
    MFont                owner;
    /** Индекс символа в шрифте. Индекс зависит от кодовой страницы шрифта. */
    private int          code;
    /** Код символа в UTF-16 */
    private int          unicode;
    /** Был ли установлен код символа. */
    private boolean      hasUnicode;
    /** Объект с изменениями символа. */
    private MSymbolUndo  undo;

    public static String PROPERTY_CODE    = "MSymbolCode";
    public static String PROPERTY_UNICODE = "MSymbolUnicode";

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

    /**
     * Создание копии символа.<br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.
     * 
     * @param src Копируемый символ.
     */
    public MSymbol(MSymbol src) {
        super(src);
        code = src.code;
    }

    /**
     * Результат проверки допустимости высоты зависит от того, принадлежит ли
     * символ шрифту или нет.<br>
     * Если символ принадлежит шрифту, то возвращается значение метода
     * {@link MFont#isValidHeight(int)}. Фактически это значит, что высота может
     * быть изменена <b>только методами шрифта</b>.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidHeight(int)}.
     */
    @Override
    protected boolean isValidHeight(int h) {
        if (owner != null) return owner.isValidHeight(h);
        return super.isValidHeight(h);
    }

    /**
     * Результат проверки допустимости ширины зависит от того, принадлежит ли
     * символ шрифту или нет.<br>
     * Если символ принадлежит шрифту, то возвращается значение метода
     * {@link MFont#isValidWidth(int)}.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidWidth(int)}.
     */
    @Override
    protected boolean isValidWidth(int w) {
        if (owner != null) return owner.isValidWidth(w);
        return super.isValidWidth(w);
    }

    protected boolean isValidCode(int code) {
        if (owner != null) return owner.isValidCode(code);
        return true;
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     * 
     * @see #setCode(int)
     */
    public int getCode() {
        return code;
    }

    /**
     * Метод устанавливает индекс символа в шрифте.
     * 
     * @param c Новый индекс.
     * @throws DisallowOperationException Если символ принадлежит шрифту и
     *             изменение индекса в шрифте невозможно.
     * @see #getCode()
     */
    public synchronized void setCode(int c) throws DisallowOperationException {
        if (code == c) return;

        if (!isValidCode(c))
            throw new DisallowOperationException("change code");

        code = c;
    }

    /**
     * Возвращает <code>true</code> если символ имеет код UTF-16.
     * 
     * @see #setUnicode(int)
     * @see #getUnicode()
     */
    public boolean isUnicode() {
        return hasUnicode;
    }

    /**
     * Метод возвращает код символа в UTF-16.
     * 
     * @see #isUnicode()
     * @see #setUnicode(int)
     */
    public int getUnicode() {
        return code;
    }

    /**
     * Метод устанавливает код символа в UTF-16.
     * 
     * @param u Новый код символа.
     * @throws DisallowOperationException Если код символа уже был установлен.
     * @see #isUnicode()
     * @see #getUnicode()
     */
    public synchronized void setUnicode(int u)
                    throws DisallowOperationException {
        if (unicode == u) return;

        if (hasUnicode) throw new DisallowOperationException("change unicode");

        hasUnicode = true;
        unicode = u;
    }

    /**
     * Возвращает шрифт, к которому принадлежит символ.
     * 
     * @return Шрифт или <b>null</b> если символ не принадлежит ни какому
     *         шрифту.
     */
    public MFont getOwner() {
        return owner;
    }

    /**
     * Копирование массива точек из символа <code>sym</code>. Так же изменяется
     * переменная {@link #code}.<br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.
     * 
     * @param sym Источник копирования.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией объекта и размер копируемого символа не
     *             совпадает с размером текущего символа.
     * @throws DisallowOperationException Если символ принадлежит шрифту и
     *             изменение индекса в шрифте невозможно.
     * @throws NullPointerException Если <code>sym</code> равен
     *             <code>null</code>.
     * @see #clone()
     */
    public synchronized void copy(MSymbol sym)
                    throws DisallowOperationException {
        if (sym == null) throw (new NullPointerException());

        if (sym.isUnicode()) setUnicode(sym.getUnicode());
        setCode(sym.getCode());
        super.copy(sym);
    }

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
        if (isUnicode() != sym.isUnicode()) return false;
        if (isUnicode()) return super.equals(sym) && unicode == sym.unicode;
        return super.equals(sym) && code == sym.code;

    }

    /**
     * Добавляет получателя сообщений об отменяемых операциях.
     * 
     * @param listener Получатель сообщений об отменяемых операциях.
     */
    public void addUndoableEditListener(UndoableEditListener listener) {
        listeners.add(UndoableEditListener.class, listener);
    }

    /**
     * Удаляет получателя сообщений об отменяемых операциях.
     * 
     * @param listener Получатель сообщений об отменяемых операциях.
     */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        listeners.remove(UndoableEditListener.class, listener);
    }

    /**
     * Выпускает сообщение об отменяемой операции.
     * 
     * @param change Объект, содержащий изменения, произведённые операцией.
     */
    protected void fireUndoEvent(UndoableEditEvent change) {
        Object[] listenerArray;

        if (listeners == null) return;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i++) {
            if (listenerArray[i] == UndoableEditListener.class)
                ((UndoableEditListener) listenerArray[i + 1])
                                .undoableEditHappened(change);
        }
    }

    /**
     * Создаёт объект для фиксации отменяемых изменений.
     * 
     * @param operation Название операции, должно быть на языке локализации.
     * @see #endChange()
     * @see #fireUndoEvent(UndoableEditEvent)
     */
    public synchronized void beginChange(String operation) {
        if (undo != null) return;

        undo = new MSymbolUndo(this, operation);
    }

    /**
     * Фиксирует отменяемые изменения и вызывает выпуск сообщения об отменяемой
     * операции.
     * 
     * @see #beginChange(String)
     * @see #fireUndoEvent(UndoableEditEvent)
     */
    public synchronized void endChange() {
        if (undo == null) return;

        undo.end();

        if (undo.canUndo()) fireUndoEvent(new UndoableEditEvent(this, undo));
        undo = null;
    }
}
