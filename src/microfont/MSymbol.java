package microfont;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;
import microfont.undo.MSymbolUndo;

/**
 * Класс MSymbol для хранения и изменения символа {@link MFont шрифта}.<br>
 * Символ имеет ширину и высоту в пикселях, а так же индекс (номер) символа в
 * шрифте. Ключевое свойство символа - массив пикселей.<br>
 * Важно знать, что хотя символ и является разделяемым ресурсом, но один и тот
 * же символ не может принадлежать различным шрифтам.
 * 
 * <p>
 * <img src="doc-files/symbol.png" align=right> <b>Массив пикселей и
 * координаты.</b><br>
 * Пиксели представлены массивом <b>boolean</b>, который имеет размер, равный
 * произведению ширины и высоты пикселя. Младшему элементу массива соответствует
 * левый верхний пиксель символа. Пиксели размещаются слева направо и сверху
 * вниз. Таким образом, последний элемент массива содержит правый нижний
 * пиксель. <br>
 * На рисунке изображён символ высотой 8 и шириной 8 пикселей. Закрашены пиксели
 * с индексами массива <b>0</b>, <b>1</b> и <b>10</b>. В то же время эти пиксели
 * соответствуют координатам <b>0</b>:<b>0</b>, <b>1</b>:<b>0</b> и
 * <b>2</b>:<b>1</b> в формате <b><i>колонка</i></b>:<b><i>строка</i></b>.<br>
 * Состоянию пикселя "закрашен" соответствует <b>true</b>, состоянию "пуст"
 * (другими словами - прозрачен, то есть видна бумага) - <b>false</b>. При
 * копировании с использованием других типов данных состоянию "закрашен"
 * соответствует бит, содержащий единицу. <br>
 * Так же важно знать, что вся область за границами символа считается
 * прозрачной. То есть такой код {@code}
 * 
 * <pre>
 *  MSymbol mysymbol;
 *  boolean   myvar;
 *  . . . . .
 *  myvar = mysymbol.getPixsel(-1, 2);
 * </pre>
 * 
 * вернёт <b>false</b>.
 * 
 * <p>
 * <b>Сообщения.</b> <br>
 * При изменении символа генерируются сообщения, к которым могут подключиться
 * {@linkplain MSymbolListener получатели сообщения}. Это полезно для
 * оперативного отображения изменений при визуальном редактировании.
 * 
 */
public class MSymbol extends PixselMap
{
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

    /**
     * Копирование массива точек из символа s. Так же изменяются переменные
     * {@link #code code}. <br>
     * Генерируются сообщения {@link MSymbolEvent#SIZE SIZE} и
     * {@link MSymbolEvent#COPY COPY}. <br>
     * Важно знать, что <b>списки {@linkplain MSymbolListener получателей
     * сообщений} не копируются</b>.
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
        super.clone();

        fireEvent(MSymbolEvent.SIZE);
        fireEvent(MSymbolEvent.COPY);
    }

    /**
     * Создаёт и возвращает копию символа. <br>
     * Важно знать, что <b>списки {@linkplain MSymbolListener получателей
     * сообщений} не копируются</b>.
     * 
     * @return Новый символ.
     * @see #copy(MSymbol)
     */
    @Override
    public MSymbol clone() {
        return new MSymbol(code, getWidth(), getHeight(), getByteArray());
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
    public boolean equals(MSymbol s) {

        if (s == null) return false;

        if ((code != s.code)) return false;

        return super.equals(s);
    }

    /**
     * Удаляет заданный столбец.
     * 
     * @param pos Позиция удаляемого столбца.
     * @throws IllegalArgumentException
     * @throws DisallowOperationException
     */
    public void removeColumn(int pos) throws IllegalArgumentException,
                    DisallowOperationException {
        PixselMap tMap;
        int w, h;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        if (pos < 0 || pos >= w) throw (new IllegalArgumentException());

        if (parent != null && !parent.isValidWidth(w - 1))
            throw new DisallowOperationException("change width");

        tMap = new PixselMap(w - 1, h);

        if (w > 1) {
            PixselIterator dst, src;
            dst = tMap.getIterator(0, 0, w - 1, h, PixselIterator.DIR_LEFT_TOP);
            src = getIterator(0, 0, pos, h, PixselIterator.DIR_LEFT_TOP);

            while (src.hasNext()) {
                dst.changeNext(src.getNext());
            }

            src = getIterator(pos + 1, 0, w - pos, h,
                            PixselIterator.DIR_LEFT_TOP);

            while (src.hasNext()) {
                dst.changeNext(src.getNext());
            }
        }

        super.copy(tMap);
        fireEvent(MSymbolEvent.SIZE);
    }

    /**
     * Удаляет заданную строчку.
     * 
     * @param pos Позиция удаляемой строки.
     * @throws IllegalArgumentException
     * @throws DisallowOperationException
     */
    public void removeRow(int pos) throws IllegalArgumentException,
                    DisallowOperationException {
        PixselMap tMap;
        int w, h;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        if (pos < 0 || pos >= h) throw (new IllegalArgumentException());

        if (parent != null && !parent.isValidHeight(h))
            throw new DisallowOperationException("change height");

        tMap = new PixselMap(w, h - 1);

        if (h > 1) {
            PixselIterator dst, src;
            dst = tMap.getIterator(0, 0, w, h - 1, PixselIterator.DIR_TOP_LEFT);
            src = getIterator(0, 0, w, pos, PixselIterator.DIR_TOP_LEFT);

            while (src.hasNext()) {
                dst.changeNext(src.getNext());
            }

            src = getIterator(0, pos + 1, w, h - pos,
                            PixselIterator.DIR_TOP_LEFT);

            while (src.hasNext()) {
                dst.changeNext(src.getNext());
            }
        }

        super.copy(tMap);
        fireEvent(MSymbolEvent.SIZE);
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     */
    public int getCode() {
        return code;
    }

    /**
     * Метод устанавливает индекс символа в шрифте. <br>
     * Генерируются события {@link MSymbolEvent#INDEX INDEX}.
     * 
     * @param i Новый индекс.
     * @throws DisallowOperationException
     */
    public void setCode(int i) throws IllegalArgumentException,
                    DisallowOperationException {
        boolean changed = false;

        if (parent != null && !parent.isValidIndex(i))
            throw new DisallowOperationException("change code");

        if (parent != null) return;

        if (code != i) {
            code = i;
            changed = true;
        }

        if (changed) fireEvent(MSymbolEvent.INDEX);
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

        listenerArray = listeners.getListenerList();
        for (int i = 1; i < listenerArray.length; i++) {
            if (listenerArray[i] instanceof UndoableEditListener)
                ((UndoableEditListener) listenerArray[i])
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
        
        undo=new MSymbolUndo(this, operation);
    }

    public synchronized void endChange() {
        if (undo == null) return;
        
        undo.end();
        
        if (undo.canUndo()) fireUndoEvent(new UndoableEditEvent(this, undo));
        undo=null;
    }
}
