package microfont;

import java.awt.Dimension;
import java.util.*;

import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

/**
 * Класс MSymbol для хранения и изменения символа {@link MFont
 * шрифта}.<br>
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
public class MSymbol extends Object
{
    /**  */
    protected MFont                    parent;
    protected MSymbol                  prevSymbol   = null;
    protected MSymbol                  nextSymbol   = null;
    /** Индекс символа в шрифте. */
    protected int                      index;
    /** Массив пикселей */
    protected PixselMapX                pixsels;
    /** Список получателей события после изменения символа. */
    private ArrayList<MSymbolListener> listListener = new ArrayList<MSymbolListener>();

    /**
     * Конструктор символа с заданными размерами, индексом и копированием
     * массива <b>boolean</b>. <br>
     * Если копируемый массив меньше количества пикселей, то оставшиеся пиксели
     * будут установлены в ноль.
     * 
     * @param i Индекс символа в шрифте.
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     * @param a Массив для копирования.
     */
    public MSymbol(int i, int w, int h, boolean[] a) {
        if (i < 0) throw (new IllegalArgumentException("Invalid index"));
        index = i;

        pixsels = new PixselMapX(w, h, a);
    }

    /**
     * Конструктор символа с заданными размерами и копированием массива
     * <b>boolean</b>. <br>
     * Если копируемый массив меньше количества пикселей, то оставшиеся пиксели
     * будут установлены в ноль.
     * 
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     * @param a Массив для копирования.
     */
    public MSymbol(int w, int h, boolean[] a) {
        this(0, w, h, a);
    }

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
        if (i < 0) throw (new IllegalArgumentException("Invalid index"));
        index = i;

        pixsels = new PixselMapX(w, h, a);
    }

    /**
     * Конструктор символа с заданными размерами и копированием массива
     * <b>byte</b>. <br>
     * Если копируемый массив меньше количества пикселей, то оставшиеся пиксели
     * будут установлены в ноль.<br>
     * Индекс символа равен нулю.
     * 
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     * @param a Массив для копирования.
     */
    public MSymbol(int w, int h, byte[] a) {
        this(0, w, h, a);
    }

    /**
     * Конструктор по умолчанию. Символ имеет нулевую ширину и высоту. Индекс
     * символа равен нулю.
     */
    public MSymbol() {
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

        if (s.pixsels == null) pixsels = null;
        else pixsels = s.pixsels.clone();

        fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
        fireEvent(MSymbolEvent.COPY, 0, 0, pixsels.width, pixsels.height);
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
        MSymbol ret;

        ret = new MSymbol();
        if (pixsels == null) ret.pixsels = null;
        else ret.pixsels = pixsels.clone();

        ret.index = index;
        return ret;
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

        if (!pixsels.equals(s.pixsels)) return false;

        return true;
    }

    /**
     * Получение заданного пикселя.
     * 
     * @param x номер пикселя в строке. Отсчёт с нуля.
     * @param y номер строки. Отсчёт с нуля.
     * @return <b>true</b> если пиксель установлен. Метод возвращает
     *         <b>false</b> если пиксель сброшен, а так же если параметры
     *         {@code x} и {@code y} выходят за границы символа.
     */
    public boolean getPixsel(int x, int y) {
        return pixsels.getPixsel(x, y);
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
        if (pixsels.changePixsel(column, row, set))
            fireEvent(MSymbolEvent.PIXSEL, column, row, 1, 1);
    }

    /**
     * Метод возвращает ширину символа.
     * 
     * @return Количество пикселей по горизонтали.
     */
    public int getWidth() {
        return pixsels.width;
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
     */
    public void setWidth(int w) {
        if (pixsels.setWidth(w))
            fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Метод возвращает высоту символа.
     * 
     * @return Количество пикселей по вертикали.
     */
    public int getHeight() {
        return pixsels.getHeight();
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
     */
    public void setHeight(int h) {
        if (pixsels.setHeight(h))
            fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Метод возвращает ширину и высоту символа.
     */
    public Dimension getSize() {
        return pixsels.getSize();
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
     */
    public void setSize(int w, int h) {
        if (pixsels.setSize(w, h))
            fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
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
     */
    public void setSize(Dimension sz) {
        if (pixsels.setSize(sz.width, sz.height))
            fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей. Если символ имеет нулевую
     * ширину и/или высоту, то возвращается <b>null</b>.
     */
    public boolean[] getArray() {
        return pixsels.getArray();
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей, упакованную в
     * <b>byte</b>. Если символ имеет нулевую ширину и/или высоту, то
     * возвращается <b>null</b>. <br>
     * Пиксели заполняют возвращаемый массив последовательно начиная с младшего
     * бита самого первого элемента.
     */
    public byte[] getByteArray() {
        return pixsels.getByteArray();
    }

    /**
     * Метод копирует массив <b>a</b> во внутренний массив символа. Размеры
     * символа не меняются. <br>
     * Генерируется сообщение {@link MSymbolEvent#COPY COPY}.
     * 
     * @param a Копируемый массив пикселей.
     * @throws IllegalArgumentException
     */
    public void setArray(boolean[] a) throws IllegalArgumentException {
        pixsels.setArray(a);
        this.fireEvent(MSymbolEvent.COPY, 0, 0, pixsels.width, pixsels.height);
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
    public void setArray(byte[] a) throws IllegalArgumentException {
        pixsels.setArray(a);
        fireEvent(MSymbolEvent.COPY, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Сдвиг символа вправо. После сдвига левый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftRight() {
        PixselMapX.copyFrame(pixsels, -1, 0, pixsels, 0, 0, pixsels.width,
                        pixsels.height);

        this.fireEvent(MSymbolEvent.SHIFT, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Сдвиг символа влево. После сдвига правый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftLeft() {
        PixselMapX.copyFrame(this.pixsels, 1, 0, this.pixsels, 0, 0,
                        pixsels.width, pixsels.height);

        this.fireEvent(MSymbolEvent.SHIFT, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Сдвиг символа вверх. После сдвига нижняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftUp() {
        PixselMapX.copyFrame(pixsels, 0, 1, pixsels, 0, 0, pixsels.width,
                        pixsels.height);

        this.fireEvent(MSymbolEvent.SHIFT, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Сдвиг символа вниз. После сдвига верхняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftDown() {
        PixselMapX.copyFrame(pixsels, 0, -1, pixsels, 0, 0, pixsels.width,
                        pixsels.height);

        this.fireEvent(MSymbolEvent.SHIFT, 0, 0, pixsels.width, pixsels.height);
    }

    /**
     * Удаляет заданный столбец.
     * 
     * @param pos Позиция удаляемого столбца.
     * @throws IllegalArgumentException
     */
    public void removeColumn(int pos) throws IllegalArgumentException {
        PixselMapX newarr;

        if (pixsels.width == 0 || pixsels.height == 0) return;

        if (pos < 0 || pos >= pixsels.width)
            throw (new IllegalArgumentException());

        if (pixsels.width == 1) {
            newarr = null;
        }
        else {
            newarr = new PixselMapX(pixsels.width - 1, pixsels.height);
            PixselMapX.copyFrame(pixsels, 0, 0, newarr,
                            0, 0, pos - 1,
                            pixsels.height);
            PixselMapX.copyFrame(pixsels, pos + 1, 0,
                            newarr,  pos, 0,
                            pixsels.width - pos, pixsels.height);
        }

        this.fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
        this.pixsels = newarr;
    }

    /**
     * Удаляет заданную строчку.
     * 
     * @param pos Позиция удаляемой строки.
     * @throws IllegalArgumentException
     */
    public void removeRow(int pos) throws IllegalArgumentException {
        PixselMapX newarr;

        if (pixsels.width == 0 || pixsels.height == 0) return;

        if (pos < 0 || pos >= pixsels.height)
            throw (new IllegalArgumentException());

        if (pixsels.height == 1) {
            newarr = null;
        }
        else {
            newarr = new PixselMapX(pixsels.width,pixsels.height - 1);
            PixselMapX.copyFrame(pixsels, 0, 0, newarr,
                             0, 0, pixsels.width,
                            pos - 1);
            PixselMapX.copyFrame(pixsels, 0, pos + 1,
                            newarr,  0, pos,
                            pixsels.width, pixsels.height - pos);
        }

        this.fireEvent(MSymbolEvent.SIZE, 0, 0, pixsels.width, pixsels.height);
        this.pixsels = newarr;
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
     * @throws IllegalArgumentException
     */
    public void setIndex(int i) throws IllegalArgumentException {
        boolean changed = false;

        if (i < 0) throw (new IllegalArgumentException(" : index"));

        if (parent != null) return;

        if (index != i) {
            index = i;
            changed = true;
        }

        if (changed) fireEvent(MSymbolEvent.INDEX, 0, 0, pixsels.width, pixsels.height);
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
        if (toAdd == null)
            throw (new IllegalArgumentException("Invalid listener"));

        if (this.listListener.indexOf(toAdd) >= 0) {
            /*
             * Объект уже добавлен. Здесь можно было бы выбросить исключение.
             * Или ругнуться в консоль.
             */
            throw (new IllegalArgumentException("Invalid listener"));
            // return;
        }
        this.listListener.add(toAdd);
    }

    /**
     * Удаление получателя события.
     * 
     * @param toRemove Удаляемый получатель события.
     */
    public void removeListener(MSymbolListener toRemove) {
        if (this.listListener.indexOf(toRemove) < 0)
            throw (new IllegalArgumentException("Invalid listener"));
        this.listListener.remove(toRemove);
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

        change = new MSymbolEvent(this, reason);
        change.reason = reason;
        // change.symbol = this;
        change.x = x;
        change.y = y;
        change.width = w;
        change.height = h;
        for (int i = 0; i < listListener.size();) {
            if (listListener.get(i) == null) {
                listListener.remove(i);
                continue;
            }
            listListener.get(i).mSymbolEvent(change);
            i++;
        }
    }

    public void reflectVerticale() {
        pixsels.reflectVerticale();
        fireEvent(MSymbolEvent.COPY, 0, 0, pixsels.width, pixsels.height);
    }

    public void reflectHorizontale() {
        pixsels.reflectHorizontale();
        fireEvent(MSymbolEvent.COPY, 0, 0, pixsels.width, pixsels.height);
    }
}
