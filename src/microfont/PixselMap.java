package microfont;

import java.awt.Dimension;

import microfont.events.BadIterationException;
import static microfont.PixselMapX.PixselIterator.*;

/**
 * Класс для представления карты пикселей.
 */
public class PixselMap extends Object
{
    /** Ширина карты в пикселях. */
    protected int     width;
    /** Высота карты в пикселях. */
    protected int     height;
    /**  */
    private int       diffX      = 0;
    /**  */
    private int       diffY      = 0;
    /** Флаг, показывающий, что карта является слепком изменений. */
    private boolean   diff       = false;

    static final int  ITEM_SIZE  = 8;
    static final int  ITEM_SHIFT = 3;
    static final byte ITEM_MASK  = 0x07;
    /** Массив пикселей */
    protected byte    pixsels[];

    /**
     * Итератор для последовательного доступа к пикселям {@linkplain PixselMap
     * карты}. Вы можете выбрать прямоугольную область карты произвольного
     * размера. Так же возможен выбор направления сканирования пикселей.
     * 
     */
    public class PixselIterator
    {
        /** Направление слева направо, сверху вниз . */
        public final static int DIR_LEFT_TOP     = 0;
        /** Направление справа налево, сверху вниз. */
        public final static int DIR_RIGHT_TOP    = 1;
        /** Направление слева направо, снизу вверх. */
        public final static int DIR_LEFT_BOTTOM  = 2;
        /** Направление справа налево, снизу вверх. */
        public final static int DIR_RIGHT_BOTTOM = 3;
        /** Направление сверху вниз, слева направо. */
        public final static int DIR_TOP_LEFT     = 4;
        /** Направление сверху вниз, справа налево. */
        public final static int DIR_TOP_RIGHT    = 5;
        /** Направление снизу вверх, слева направо. */
        public final static int DIR_BOTTOM_LEFT  = 6;
        /** Направление снизу вверх, справа налево. */
        public final static int DIR_BOTTOM_RIGHT = 7;

        private PixselMap      map;
        private int             dir;
        private int             startX, startY, w, h;
        private int             posX, posY;

        /**
         * 
         * @param src
         * @param diffX
         * @param diffY
         * @param w
         * @param h
         */
        protected PixselIterator(PixselMap src, int x, int y, int w, int h,
                        int dir) {
            this.map = src;
            this.dir = dir;
            startX = x;
            startY = y;
            this.w = w;
            this.h = h;

            if (startX < 0) {
                this.w -= startX;
                startX = 0;
            }
            if (startY < 0) {
                this.h -= startY;
                startY = 0;
            }

            if (this.w < 0) w = 0;
            if (this.h < 0) h = 0;
        }

        private void updatePosition() {

        }

        public boolean hasNext() {
            if (posY <= h && posX < w) return true;
            return false;
        }

        /**
         * 
         * @return
         * @throws BadIterationException
         */
        public boolean getNext() {
            boolean ret = false;
            if (!hasNext()) throw new BadIterationException();
            // TODO Auto-generated method stub

            updatePosition();
            return ret;
        }

        public void changeNext(boolean set) {
            if (!hasNext()) throw new BadIterationException();
            // TODO Auto-generated method stub

            updatePosition();
        }
    }

    /**
     * Конструктор для создания карты с заданными размерами. Все пиксели
     * сброшены в <code>false</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     */
    public PixselMap(int width, int height) {
        init(width, height);
    }

    /**
     * Конструктор для создания карты с заданными размерами и копированием
     * пикселей из массива <code>map</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param map Копируемый массив.
     * @see #fromArray(boolean[], int[])
     */
    public PixselMap(int width, int height, boolean[] src) {
        init(width, height);
        fromArray(src, pixsels);
    }

    /**
     * Конструктор для создания карты с заданными размерами и копированием
     * пикселей из массива <code>map</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param map Копируемый массив.
     * @see #fromArray(byte[], int[])
     */
    public PixselMap(int width, int height, byte[] src) {
        init(width, height);
        fromArray(src, pixsels);
    }

    /**
     * Конструктор для получения копии карты.
     * 
     * @param map Копируемая карта.
     * @see #clone()
     * @see #copy(PixselMap)
     */
    public PixselMap(PixselMap src) {
        init(src.width, src.height);
        if (pixsels != null)
            System.arraycopy(src.pixsels, 0, pixsels, 0, pixsels.length);
    }

    /**
     * Пустой конструктор. Символ имеет нулевую ширину и высоту.
     */
    public PixselMap() {
    }

    /**
     * Устанавливает ширину и высоту карты и подготавливает внутренний массив
     * для пикселей, заменяя существующий на массив с требуемым размером.
     * 
     * @param width Высота карты.
     * @param height Ширина карты.
     * @see #doPixselArray(int, int)
     */
    private void init(int width, int height) {
        pixsels = doPixselArray(width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * Создаёт массив для хранения пикселей с требуемым размером. Если ширина
     * и/или высота равна нулю, то возвращает <code>null</code>.
     * 
     * @param width Высота карты.
     * @param height Ширина карты.
     * @return Массив с требуемым размером.
     * @throws IllegalArgumentException если ширина и/или высота меньше нуля.
     * @see #init(int, int)
     */
    private byte[] doPixselArray(int width, int height) {
        if (width < 0) throw (new IllegalArgumentException("Invalid width"));
        if (height < 0) throw (new IllegalArgumentException("Invalid height"));

        if (width == 0 || height == 0) return null;
        return new byte[((width + ITEM_SIZE - 1) / ITEM_SIZE) * height];
    }

    private static int index(int width, int x, int y) {
        return ((width + ITEM_SIZE - 1) >> ITEM_SHIFT) * y + (x >> ITEM_SHIFT);
    }

    private int index(int x, int y) {
        return index(this.width, x, y);
    }

    /**
     * Копирование из карты <code>map</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param map Источник копирования.
     * @see #clone()
     */
    public void copy(PixselMap src) {
        byte[] tm;

        if (src == null) throw (new NullPointerException());

        tm = doPixselArray(src.width, src.height);

        if (tm != null) System.arraycopy(src.pixsels, 0, tm, 0, tm.length);

        pixsels = tm;
        width = src.width;
        height = src.height;
    }

    /**
     * Создаёт и возвращает полную копию символа. Фактически метод является
     * обёрткой для {@link #PixselMap(PixselMap)}.
     * 
     * @return Копия карты.
     * @see #PixselMap(PixselMap)
     * @see #copy(PixselMap)
     */
    @Override
    public PixselMap clone() {
        return new PixselMap(this);
    }

    /**
     * Сравнение карт. Карты считаются равными, если у них совпадают ширина,
     * высота и содержимое массивов пикселей.
     * 
     * @param s Карта для сравнения.
     * @return <b>true</b> если символы равны.
     */
    @Override
    public boolean equals(Object s) {
        if (s == null) return false;

        if (!(s instanceof PixselMap)) return false;

        return equals((PixselMap) s);
    }

    /**
     * Сравнение карт. Карты считаются равными, если у них совпадают ширина,
     * высота и содержимое массивов пикселей.
     * 
     * @param s Карта для сравнения.
     * @return <b>true</b> если символы равны.
     */
    public boolean equals(PixselMap s) {
        int i;

        if (s == null) return false;

        if ((width != s.width) || (height != s.height)) return false;

        i = pixsels.length;
        while (i > 0) {
            i--;
            if (pixsels[i] != s.pixsels[i]) return false;
        }

        return true;
    }

    /**
     * Создаёт и возвращает итератор для последовательного доступа к пикселям
     * заданного фрагмента карты. Размеры фрагмента корректируются с целью
     * предотвращения выхода за пределы карты.
     * <p>
     * Доступ к пикселям осуществляется слева направо, сверху вниз.
     * 
     * @param diffX
     * @param diffY
     * @param w
     * @param h
     * @return
     */
    public PixselIterator getIterator(int x, int y, int w, int h) {
        return new PixselIterator(this, x, y, w, h, DIR_LEFT_TOP);
    }

    /**
     * 
     * @param diffX
     * @param diffY
     * @param w
     * @param h
     * @return
     */
    public PixselMap getFragment(int x, int y, int w, int h) {
        if (x < 0) {
            w -= x;
            x = 0;
        }

        if (y < 0) {
            h -= y;
            y = 0;
        }

        if (x + w > width) w = width - x;
        if (y + h > height) h = height - y;

        if (w <= 0 || h <= 0) return null;

        PixselMap rv = new PixselMap(w, h);
        PixselIterator src = new PixselIterator(this, x, y, w, h, DIR_LEFT_TOP);
        PixselIterator dst = new PixselIterator(rv, 0, 0, w, h, DIR_LEFT_TOP);

        while (src.hasNext() && dst.hasNext()) {
            dst.changeNext(src.getNext());
        }

        return rv;
    }

    /**
     * 
     * @param diffX
     * @param diffY
     * @param fragment
     */
    public void setFragment(int x, int y, PixselMap fragment) {
        int w;
        int h;

        w = fragment.width;
        h = fragment.height;

        if (x + w > width) w = width - x;
        if (y + h > height) h = height - y;

        if (w <= 0 || h <= 0) return;

        PixselIterator src = new PixselIterator(fragment, 0, 0, w, h,
                        DIR_LEFT_TOP);
        PixselIterator dst = new PixselIterator(this, x, y, w, h, DIR_LEFT_TOP);

        while (src.hasNext() && dst.hasNext()) {
            dst.changeNext(src.getNext());
        }
    }

    public PixselMap getDifferent(PixselMap old) {
        PixselMap rv;
        int w = 0;
        int h = 0;

        if (old == null) return null;

        if (equals(old)) return null;

        if (this.width != old.width || this.height != old.height) {
            rv = new PixselMap(old);
            rv.diff = true;
            return rv;
        }

        rv = new PixselMap(w, h);

        return rv;
    }

    public void restore(PixselMap different) {
        if (different == null || different.diff == false) return;

        if (different.diffX == 0 && different.diffY == 0) {
            pixsels = different.pixsels;
        }

    }

    /**
     * Получение заданного пикселя.
     * 
     * @param diffX номер пикселя в строке. Отсчёт с нуля.
     * @param diffY номер строки. Отсчёт с нуля.
     * @return <b>true</b> если пиксель установлен. Метод возвращает
     *         <b>false</b> если пиксель сброшен, а так же если параметры
     *         {@code diffX} и {@code diffY} выходят за границы символа.
     */
    public boolean getPixsel(int x, int y) {
        if (x < 0 || x >= width) return false;
        if (y < 0 || y >= height) return false;

        return (pixsels[index(x, y)] & (1 << (x & ITEM_MASK))) != 0;
    }

    /**
     * Метод изменяет заданный пиксель.
     * 
     * @param posX номер пикселя в строке. Отсчёт с нуля.
     * @param posY номер строки. Отсчёт с нуля.
     * @param set <b>true</b> если пиксель должен быть установлен, <b>false</b>
     *            если нужно сбросить.
     * @return <code>true</code> если метод действительно изменил содержимое.
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
     */
    public boolean changePixsel(int column, int row, boolean set) {
        int index;
        byte mask;
        if (column < 0 || column >= width)
            throw (new IllegalArgumentException(" : posX"));

        if (row < 0 || row >= height)
            throw (new IllegalArgumentException(" : posY"));

        index = index(column, row);
        mask = (byte) (1 << (column & ITEM_MASK));

        if (((pixsels[index] & mask) != 0) != set) {
            if (set) {
                pixsels[index] |= mask;
            }
            else {
                pixsels[index] &= (byte) ~mask;
            }
            return true;
        }

        return false;
    }

    /**
     * 
     * @param column
     * @param row
     * @return
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
     */
    public boolean setPixsel(int column, int row) {
        return changePixsel(column, row, true);
    }

    /**
     * 
     * @param column
     * @param row
     * @return
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
     */
    public boolean cleanPixsel(int column, int row) {
        return changePixsel(column, row, false);
    }

    /**
     * Метод возвращает ширину и высоту символа.
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Изменение размеров символа. Так же меняется размер массива пикселей. Если
     * один из размеров равен нулю, то массив пикселей освобождается.
     * 
     * @param w Новая ширина. Если число отрицательное, то ширина не меняется.
     * @param h Новая высота. Если число отрицательное, то высота не меняется.
     * @return <b>true</b> если размер действительно изменился. Это полезно,
     *         например публичным методам для генерации уведомления об
     *         изменениях.
     */
    protected boolean setSize(int w, int h) {
        int nw, nh;
        byte[] narr;

        /* Проверка ширины. */
        if (w < 0) {
            nw = this.width;
        }
        else {
            nw = w;
        }
        /* Проверка высоты. */
        if (h < 0) {
            nh = this.height;
        }
        else {
            nh = h;
        }
        /* Если новые размеры равны старым, то и делать ничего не надо. */
        if (nw == this.width && nh == this.height) {
            return false;
        }
        /* Если один из размеров равен нулю, обнуляем символ. */
        if (nw == 0 || nh == 0) {
            narr = null;
        }
        else {
            narr = doPixselArray(nw, nh);
            /*
             * Если старый массив не пуст, копировать его (насколько возможно) в
             * новый.
             */
            if (this.pixsels != null) {
                copyFrame(this.pixsels, this.width, this.height, 0, 0, narr,
                                nw, nh, 0, 0, nw, nh);
            }
        }

        /* Фиксируем изменения. */
        this.pixsels = narr;
        this.width = nw;
        this.height = nh;
        return true;
    }

    /**
     * Метод возвращает ширину символа.
     * 
     * @return Количество пикселей по горизонтали.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Метод изменяет ширину символа. <br>
     * Если новая ширина меньше текущей, то лишние столбцы символа обрезаются
     * справа. <br>
     * Если новая ширина больше текущей, то справа добавляются пустые столбцы.
     * 
     * @param w Новая ширина символа. Если параметр меньше нуля, то ширина не
     *            изменяется.
     * @return <code>true</code> если метод действительно изменил содержимое.
     */
    protected boolean setWidth(int w) {
        return setSize(w, height);
    }

    /**
     * Метод возвращает высоту символа.
     * 
     * @return Количество пикселей по вертикали.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Метод изменяет высоту символа. <br>
     * Если новая высота меньше текущей, то лишние строки символа обрезаются
     * снизу. <br>
     * Если новая высота больше текущей, то внизу добавляются пустые строки. <br>
     * 
     * @param h Новая высота символа. Если параметр меньше нуля, то высота не
     *            изменяется.
     */
    protected boolean setHeight(int h) {
        return setSize(width, h);
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей. Если символ имеет нулевую
     * ширину и/или высоту, то возвращается <b>null</b>.
     */
    public boolean[] getArray() {
        if (pixsels == null) return null;

        boolean[] rv = new boolean[width * height];

        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rv[i] = getPixsel(x, y);
                i++;
            }
        }

        return rv;
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей, упакованную в
     * <b>byte</b>. Если символ имеет нулевую ширину и/или высоту, то
     * возвращается <b>null</b>. <br>
     * Пиксели заполняют возвращаемый массив последовательно начиная с младшего
     * бита самого первого элемента.
     */
    public byte[] getByteArray() {
        if (pixsels == null) return null;

        byte[] rv = new byte[(width * height + 7) / 8];
        toArray(pixsels, rv);
        return rv;
    }

    /**
     * Метод копирует массив <b>a</b> во внутренний массив символа. Размеры
     * символа не меняются.
     * 
     * @param a Копируемый массив пикселей.
     * @throws IllegalArgumentException
     */
    public void setArray(boolean[] a) throws IllegalArgumentException {
        if (a == null) throw (new IllegalArgumentException());

        fromArray(a, pixsels);
    }

    /**
     * Метод копирует массив пикселей, упакованных в <b>byte</b>, во внутренний
     * масссив. Пиксели в копируемом массиве располагаются с младшего байта
     * самого первого элемента. <br>
     * 
     * @param a Копируемый массив пикселей.
     * @throws IllegalArgumentException
     */
    public void setArray(byte[] a) throws IllegalArgumentException {
        if (a == null) throw (new IllegalArgumentException());

        fromArray(a, pixsels);
    }

    public void reflectVerticale() {
        // TODO Auto-generated method stub
        boolean end, start;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 2; x++) {
                start = getPixsel(x, y);
                end = getPixsel(width - 1 - x, y);

                changePixsel(x, y, end);
                changePixsel(width - 1 - x, y, start);
            }
        }
    }

    public void reflectHorizontale() {
        // TODO Auto-generated method stub
        boolean end, start;

        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < width; x++) {
                start = getPixsel(x, y);
                end = getPixsel(x, height - 1 - y);

                changePixsel(x, y, end);
                changePixsel(x, height - 1 - y, start);
            }
        }
    }

    /**
     * Метод копирует фрагмент из одного массива в другой. Размер копируемого
     * фрагмента может быть уменьшен в случае, если он выходит за границы
     * символов.
     * 
     * @param src Массив, из которого копируется фрагмент.
     * @param srcW Ширина символа, который представляет массив <b>src</b>.
     * @param srcH Высота символа, который представляет массив <b>src</b>.
     * @param srcX Горизонтальная позиция, с которой начинается копирование из
     *            массива <b>src</b>.
     * @param srcY Вертикальная позиция, с которой начинается копирование из
     *            массива <b>src</b>.
     * @param dst Массив, в который копируется фрагмент.
     * @param dstW Ширина символа символа, который представляет массив
     *            <b>dst</b>.
     * @param dstH Высота символа, который представляет массив <b>dst</b>.
     * @param dstX Горизонтальная позиция, с которой начинается копирование в
     *            массив <b>dst</b>.
     * @param dstY Вертикальная позиция, с которой начинается копирование в
     *            массив <b>dst</b>.
     * @param w Ширина копируемого фрагмента.
     * @param h Высота копируемого фрагмента.
     * @return <b>true</b> если копирование произошло.
     */
    private static boolean copyFrame(byte[] src, int srcW, int srcH, int srcX,
                    int srcY, byte[] dst, int dstW, int dstH, int dstX,
                    int dstY, int w, int h) {
        boolean t, rv;

        if (src == null || dst == null) {
            return false;
        }

        rv = false;
        /*
         * Собственно копирование. В зависимости от точки копирования выбирается
         * копирование от начала к концу или наоборот.
         */
        if (srcW * srcY + srcX > dstW * dstY + dstX) {
            for (int ir = 0; ir < h; ir++) {
                for (int ic = 0; ic < w; ic++) {
                    if ((ir + srcY >= srcH) || (ir + srcY < 0)
                                    || (ic + srcX >= srcW) || (ic + srcX < 0)) {
                        t = false;
                    }
                    else {
                        t = ((src[index(srcW, srcX + ic, ir + srcY)] & (1 << ((srcX + ic) & ITEM_MASK))) != 0);
                    }

                    if ((ir + dstY >= dstH) || (ir + dstY < 0)
                                    || (ic + dstX >= dstW) || (ic + dstX < 0)) {
                        continue;
                    }

                    if (t) dst[index(dstW, dstX + ic, ir + dstY)] |= (1 << ((dstX + ic) & ITEM_MASK));
                    else dst[index(dstW, dstX + ic, ir + dstY)] &= ~(1 << ((dstX + ic) & ITEM_MASK));
                    rv = true;
                }
            }
        }
        else {
            for (int ir = h - 1; ir >= 0; ir--) {
                for (int ic = w - 1; ic >= 0; ic--) {
                    if ((ir + srcY >= srcH) || (ir + srcY < 0)
                                    || (ic + srcX >= srcW) || (ic + srcX < 0)) {
                        t = false;
                    }
                    else {
                        t = ((src[index(srcW, srcX + ic, ir + srcY)] & (1 << ((srcX + ic) & ITEM_MASK))) != 0);
                    }

                    if ((ir + dstY >= dstH) || (ir + dstY < 0)
                                    || (ic + dstX >= dstW) || (ic + dstX < 0)) {
                        continue;
                    }

                    if (t) dst[index(dstW, dstX + ic, ir + dstY)] |= (1 << ((dstX + ic) & ITEM_MASK));
                    else dst[index(dstW, dstX + ic, ir + dstY)] &= ~(1 << ((dstX + ic) & ITEM_MASK));
                    rv = true;
                }
            }
        }
        return rv;
    }

    protected static boolean copyFrame(PixselMap src, int srcX, int srcY,
                    PixselMap dst, int dstX, int dstY, int w, int h) {
        return copyFrame(src.pixsels, src.width, src.height, srcX, srcY,
                        dst.pixsels, dst.width, dst.height, dstX, dstY, w, h);
    }

    /**
     * Метод копирует массив пикселей в массив <b>byte</b>. <br>
     * Массив байт представлятся как непрерывная последовательность пикселей,
     * начинающихся с младшего бита первого байта. Каждый бит источника
     * трактуется как пиксель. <br>
     * Если суммарное количество бит <b>map</b> меньше длины <b>dst</b>, то
     * остаток в приёмнике не изменяется.
     * 
     * @param map Источник копирования.
     * @param dst Цель копирования.
     */
    private void toArray(byte[] src, byte[] dst) {
        int i = 0;
        int b = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (i >= dst.length) return;
                if ((src[index(width, x, y)] & (1 << (x & ITEM_MASK))) != 0) {
                    dst[i] |= (1 << b);
                }
                b++;
                if (b > 7) {
                    b = 0;
                    i++;
                }
            }
        }
    }

    /**
     * Метод копирует массив <b>byte</b> в массив пикселей. <br>
     * Массив байт представлятся как непрерывная последовательность пикселей,
     * начинающихся с младшего бита первого байта. Каждый бит источника
     * трактуется как пиксель. <br>
     * Если суммарное количество бит <b>map</b> меньше длины <b>dst</b>, то
     * остаток в приёмнике не изменяется.
     * 
     * @param map Источник копирования.
     * @param dst Цель копирования.
     */
    private void fromArray(boolean[] src, byte[] dst) {

    }

    /**
     * Метод копирует массив <b>byte</b> в массив пикселей. <br>
     * Массив байт представлятся как непрерывная последовательность пикселей,
     * начинающихся с младшего бита первого байта. Каждый бит источника
     * трактуется как пиксель. <br>
     * Если суммарное количество бит <b>map</b> меньше длины <b>dst</b>, то
     * остаток в приёмнике не изменяется.
     * 
     * @param map Источник копирования.
     * @param dst Цель копирования.
     */
    private void fromArray(byte[] src, byte[] dst) {

    }
}
