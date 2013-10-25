package microfont;

import java.awt.Dimension;

import utils.event.DataEventListener;
import utils.event.ListenerChain;

import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

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
    protected MFont   parent;
    protected MSymbol prevSymbol = null;
    protected MSymbol nextSymbol = null;
    /** Индекс символа в шрифте. */
    private int       index;

    /** Список получателей события после изменения символа. */

    private class Chain extends ListenerChain<MSymbolEvent>
    {
        @Override
        protected void listenerCall(DataEventListener listener,
                        MSymbolEvent event) {
            ((MSymbolListener) listener).mSymbolEvent(event);
        }

    }

    private Chain listListener = new Chain();

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
        if (i < 0) throw (new IllegalArgumentException("Invalid index"));
        index = i;
    }

    @Override
    public void copy(PixselMap src) {
        super.copy(src);
    }

    /**
     * Копирование массива точек из символа s. Так же изменяются переменные
     * {@link #index index}. <br>
     * Генерируются сообщения {@link MSymbolEvent#SIZE SIZE} и
     * {@link MSymbolEvent#COPY COPY}. <br>
     * Важно знать, что <b>списки {@linkplain MSymbolListener получателей
     * сообщений} не копируются</b>.
     * 
     * @param s Источник копирования.
     * @see #clone()
     */
    public void copy(MSymbol s) {
        if (s == null) throw (new NullPointerException());

        super.clone();

        fireEvent(MSymbolEvent.SIZE, 0, 0, this.getWidth(), this.getHeight());
        fireEvent(MSymbolEvent.COPY, 0, 0, this.getWidth(), this.getHeight());
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
        return new MSymbol(index, this.getWidth(), this.getHeight(),
                        this.getByteArray());
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

        if ((index != s.index)) return false;

        return super.equals(s);
    }

    public void setPixsels(PixselMap pixsels) throws DisallowOperationException {
        if (parent != null && !parent.isValidWidth(this.getWidth()))
            throw new DisallowOperationException("change width");
        if (parent != null && !parent.isValidHeight(this.getHeight()))
            throw new DisallowOperationException("change height");
        super.copy(pixsels);
    }

    public PixselMap getPixsels() {
        return this;
    }

    /**
     * Метод изменяет заданный пиксель. <br>
     * Генерируется событие {@link MSymbolEvent#PIXSEL PIXSEL}.
     * 
     * @param column номер пикселя в строке. Отсчёт с нуля.
     * @param row номер строки. Отсчёт с нуля.
     * @param set <b>true</b> если пиксель должен быть установлен, <b>false</b>
     *            если нужно сбросить.
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
     */
    public void setPixsel(int column, int row, boolean set) {
        if (this.changePixsel(column, row, set))
            fireEvent(MSymbolEvent.PIXSEL, column, row, 1, 1);
    }

    /**
     * Метод изменяет ширину символа. <br>
     * Если новая ширина меньше текущей, то лишние столбцы символа обрезаются
     * справа. <br>
     * Если новая ширина больше текущей, то справа добавляются пустые столбцы. <br>
     * Генерируется сообщение {@link MSymbolEvent#SIZE SIZE}.
     * 
     * @param w Новая ширина символа. Если параметр меньше нуля, то ширина не
     *            изменяется.
     * @throws DisallowOperationException
     */
    public void setWidth(int w) throws DisallowOperationException {
        if (parent != null && !parent.isValidWidth(w))
            throw new DisallowOperationException("change width");

        super.changeSize(w, -1);
        if (hasChange())
            fireEvent(MSymbolEvent.SIZE, 0, 0, this.getWidth(),
                            this.getHeight());
    }

    /**
     * Метод изменяет высоту символа. <br>
     * Если новая высота меньше текущей, то лишние строки символа обрезаются
     * снизу. <br>
     * Если новая высота больше текущей, то внизу добавляются пустые строки. <br>
     * Генерируется сообщение {@link MSymbolEvent#SIZE SIZE}.
     * 
     * @param h Новая высота символа. Если параметр меньше нуля, то высота не
     *            изменяется.
     * @throws DisallowOperationException
     */
    public void setHeight(int h) throws DisallowOperationException {
        if (parent != null && !parent.isValidHeight(h))
            throw new DisallowOperationException("change height");

        super.changeSize(-1, h);
        if (hasChange())
            fireEvent(MSymbolEvent.SIZE, 0, 0, this.getWidth(),
                            this.getHeight());
    }

    /**
     * Метод изменяет ширину и высоту символа. <br>
     * Если новая высота меньше текущей, то лишние строки символа обрезаются
     * снизу. Если новая высота больше текущей, то внизу добавляются пустые
     * строки. <br>
     * Если новая ширина меньше текущей, то лишние столбцы символа обрезаются
     * справа. Если новая ширина больше текущей, то справа добавляются пустые
     * столбцы. <br>
     * Генерируется сообщение {@link MSymbolEvent#SIZE SIZE}.
     * 
     * @param w Новая ширина символа. Если параметр меньше нуля, то ширина не
     *            изменяется.
     * @param h Новая высота символа. Если параметр меньше нуля, то высота не
     *            изменяется.
     * @throws DisallowOperationException
     */
    public void setSize(int w, int h) throws DisallowOperationException {
        if (parent != null && !parent.isValidWidth(w))
            throw new DisallowOperationException("change width");
        if (parent != null && !parent.isValidHeight(h))
            throw new DisallowOperationException("change height");

        super.changeSize(w, h);
        if (hasChange())
            fireEvent(MSymbolEvent.SIZE, 0, 0, this.getWidth(),
                            this.getHeight());
    }

    /**
     * Метод изменяет ширину и высоту символа. <br>
     * Если новая высота меньше текущей, то лишние строки символа обрезаются
     * снизу. Если новая высота больше текущей, то внизу добавляются пустые
     * строки. <br>
     * Если новая ширина меньше текущей, то лишние столбцы символа обрезаются
     * справа. Если новая ширина больше текущей, то справа добавляются пустые
     * столбцы. <br>
     * Генерируется сообщение {@link MSymbolEvent#SIZE SIZE}.
     * 
     * @param sz Если <b>sz.width</b> меньше нуля, то ширина не изменяется. Так
     *            же, если <b>sz.height</b> меньше нуля, то высота не
     *            изменяется.
     * @throws DisallowOperationException
     */
    public void setSize(Dimension sz) throws DisallowOperationException {
        changeSize(sz.width, sz.height);
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей. Если символ имеет нулевую
     * ширину и/или высоту, то возвращается <b>null</b>.
     */
    @Override
    public boolean[] getArray() {
        return super.getArray();
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей, упакованную в
     * <b>byte</b>. Если символ имеет нулевую ширину и/или высоту, то
     * возвращается <b>null</b>. <br>
     * Пиксели заполняют возвращаемый массив последовательно начиная с младшего
     * бита самого первого элемента.
     */
    @Override
    public byte[] getByteArray() {
        return super.getByteArray();
    }

    /**
     * Метод копирует массив <b>a</b> во внутренний массив символа. Размеры
     * символа не меняются. <br>
     * Генерируется сообщение {@link MSymbolEvent#COPY COPY}.
     * 
     * @param a Копируемый массив пикселей.
     * @throws IllegalArgumentException
     */
    @Override
    public void setArray(boolean[] a) throws IllegalArgumentException {
        super.setArray(a);
        fireEvent(MSymbolEvent.COPY, 0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Метод копирует массив пикселей, упакованных в <b>byte</b>, во внутренний
     * масссив. Пиксели в копируемом массиве располагаются с младшего байта
     * самого первого элемента. <br>
     * Генерируется сообщение {@link MSymbolEvent#COPY COPY}.
     * 
     * @param a Копируемый массив пикселей.
     * @throws IllegalArgumentException
     */
    @Override
    public void setArray(byte[] a) throws IllegalArgumentException {
        super.setArray(a);
        fireEvent(MSymbolEvent.COPY, 0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Сдвиг символа вправо. После сдвига левый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftRight() {
        int w, h;
        PixselIterator dst, src;

        if (this.isEmpty()) return;

        w = getWidth();
        h = getHeight();

        dst = getIterator(0, 0, w, h, PixselIterator.DIR_TOP_RIGHT);
        src = getIterator(0, 0, w - 1, h, PixselIterator.DIR_TOP_RIGHT);

        while (src.hasNext()) {
            dst.changeNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.changeNext(false);
        }

        fireEvent(MSymbolEvent.SHIFT, 0, 0, w, h);
    }

    /**
     * Сдвиг символа влево. После сдвига правый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftLeft() {
        int w, h;
        PixselIterator dst, src;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        dst = getIterator(0, 0, w, h, PixselIterator.DIR_TOP_LEFT);
        src = getIterator(1, 0, w - 1, h, PixselIterator.DIR_TOP_LEFT);

        while (src.hasNext()) {
            dst.changeNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.changeNext(false);
        }

        fireEvent(MSymbolEvent.SHIFT, 0, 0, w, h);
    }

    /**
     * Сдвиг символа вверх. После сдвига нижняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftUp() {
        int w, h;
        PixselIterator dst, src;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        dst = getIterator(0, 0, w, h, PixselIterator.DIR_RIGHT_TOP);
        src = getIterator(0, 1, w, h - 1, PixselIterator.DIR_RIGHT_TOP);

        while (src.hasNext()) {
            dst.changeNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.changeNext(false);
        }

        fireEvent(MSymbolEvent.SHIFT, 0, 0, w, h);
    }

    /**
     * Сдвиг символа вниз. После сдвига верхняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftDown() {
        int w, h;
        PixselIterator dst, src;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        dst = getIterator(0, 0, w, h, PixselIterator.DIR_LEFT_BOTTOM);
        src = getIterator(0, 0, w, h - 1, PixselIterator.DIR_LEFT_BOTTOM);

        while (src.hasNext()) {
            dst.changeNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.changeNext(false);
        }

        fireEvent(MSymbolEvent.SHIFT, 0, 0, w, h);
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
        fireEvent(MSymbolEvent.SIZE, 0, 0, w, h);
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
        fireEvent(MSymbolEvent.SIZE, 0, 0, w, h);
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Метод устанавливает индекс символа в шрифте. <br>
     * Генерируются события {@link MSymbolEvent#INDEX INDEX}.
     * 
     * @param i Новый индекс.
     * @throws DisallowOperationException
     */
    public void setIndex(int i) throws IllegalArgumentException,
                    DisallowOperationException {
        boolean changed = false;

        if (parent != null && !parent.isValidIndex(i))
            throw new DisallowOperationException("change index");

        if (parent != null) return;

        if (index != i) {
            index = i;
            changed = true;
        }

        if (changed)
            fireEvent(MSymbolEvent.INDEX, 0, 0, this.getWidth(),
                            this.getHeight());
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

    /**
     * 
     * @param parent
     * @throws IllegalArgumentException public void setParent(MFont parent)
     *             throws IllegalArgumentException { if (this.parent != null) {
     *             this.parent.getParent().removeSymbol(this); this.parent =
     *             null; }
     * 
     *             parent.setSymbol(this); this.parent = parent; }
     */

    /**
     * Добавление получателя события.
     * 
     * @param toAdd Добавляемый получатель события.
     */
    public void addListener(MSymbolListener toAdd) {
        listListener.add(toAdd);
    }

    /**
     * Удаление получателя события.
     * 
     * @param toRemove Удаляемый получатель события.
     */
    public void removeListener(MSymbolListener toRemove) {
        listListener.remove(toRemove);
    }

    /**
     * Генерация события изменения символа. Получатели добавляются функцией
     * {@link #addListener(MSymbolListener)}.
     * 
     * @param reason Причина изменения символа.
     * @see MSymbolListener
     */
    private void fireEvent(int reason, int x, int y, int w, int h) {
        MSymbolEvent change;
        
        if (listListener == null) return;

        change = new MSymbolEvent(this, reason);
        change.reason = reason;
        // change.symbol = this;
        change.x = x;
        change.y = y;
        change.width = w;
        change.height = h;
        listListener.fire(change);
    }

    public void reflectVerticale() {
        int w, h;
        boolean end, start, changed = false;

        w = this.getWidth();
        h = this.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w / 2; x++) {
                start = this.getPixsel(x, y);
                end = this.getPixsel(w - 1 - x, y);

                if (start != end) changed = true;

                this.changePixsel(x, y, end);
                this.changePixsel(w - 1 - x, y, start);
            }
        }

        if (changed)
            fireEvent(MSymbolEvent.COPY, 0, 0, this.getWidth(),
                            this.getHeight());
    }

    public void reflectHorizontale() {
        int w, h;
        boolean end, start, changed = false;

        w = this.getWidth();
        h = this.getHeight();

        for (int y = 0; y < h / 2; y++) {
            for (int x = 0; x < w; x++) {
                start = this.getPixsel(x, y);
                end = this.getPixsel(x, h - 1 - y);

                if (start != end) changed = true;

                this.changePixsel(x, y, end);
                this.changePixsel(x, h - 1 - y, start);
            }
        }

        if (changed)
            fireEvent(MSymbolEvent.COPY, 0, 0, this.getWidth(),
                            this.getHeight());
    }
}
