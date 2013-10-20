package microfont;

import java.awt.Dimension;

import static microfont.PixselMap.PixselIterator.*;

/**
 * Класс для представления карты пикселей.
 */
public class PixselMap extends Object
{
    static final int  ITEM_SIZE  = 8;
    static final int  ITEM_SHIFT = 3;
    static final byte ITEM_MASK  = 0x07;

    /** Массив пикселей */
    private byte      pixsels[];
    /** Ширина карты в пикселях. */
    private int       width;
    /** Высота карты в пикселях. */
    private int       height;

    /**
     * Итератор для последовательного доступа к пикселям прямоугольной области
     * {@linkplain PixselMap карты} произвольного размера. Так же возможен выбор
     * направления сканирования пикселей.
     * <p>
     * Размеры, задаваемые при создании, могут быть скорректированы, если
     * область сканирования выходит за границы карты. Например, если
     * <code>x</code> равен -3 и <code>w</code> равен 7, то <code>x</code> будет
     * 0 и <code>w</code> станет 4. Если же ширина карты меньше <code>x+w</code>
     * , то <code>w</code> будет соответственно уменьшен. Так же корректируются
     * вертикальные размеры. Получить действительные размеры можно при помощи
     * {@link #getX()}, {@link #getY()}, {@link #getWidth()} и
     * {@link #getHeight()}.
     * <p>
     * Ширина и высота области сканирования не может быть отрицательным числом,
     * однако это может произойти в результате коррекции или ошибки при задании
     * параметров конструктора. В таком случае {@link #hasNext()} вернёт
     * <code>false</code> сразу после создания итератора.
     * <p>
     * Для проверки окончания сканирования есть два способа.
     * <ol>
     * <li>Вызывать метод {@link #hasNext()} перед вызовом {@link #getNext()}
     * или {@link #changeNext(boolean)}.
     * <li>Поместить <code>getNext</code> <code>changeNext</code> в блок
     * <code>try</code> и отлавливать исключение {@link BadIterationException}.
     * </ol>
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

        private PixselMap       parent;
        private int             dir;
        private int             startX, startY, endX, endY;
        private int             posX, posY;

        /**
         * Создаёт итератор с заданными размерами и направлением сканирования.
         * При создании размеры могут быть скорректированы, если область
         * сканирования выходит за границы карты.
         * <p>
         * {@linkplain PixselIterator Подробнее о ограничениях}
         * 
         * @param src Карта, для которой будет создан итератор.
         * @param x Позиция области по горизонтали.
         * @param y Позиция области по вертикали.
         * @param width Ширина области.
         * @param height Высота области.
         * @param dir Направление сканирования. Может быть одним из
         *            <ul>
         *            <li>{@link #DIR_LEFT_TOP} <li>{@link #DIR_RIGHT_TOP} <li>
         *            {@link #DIR_LEFT_BOTTOM} <li>{@link #DIR_RIGHT_BOTTOM} 
         *            <li>{@link #DIR_TOP_LEFT} <li>{@link #DIR_TOP_RIGHT} <li>
         *            {@link #DIR_BOTTOM_LEFT} <li>{@link #DIR_BOTTOM_RIGHT}
         *            </ul>
         */
        protected PixselIterator(PixselMap src, int x, int y, int width,
                        int height, int dir) {
            parent = src;
            this.dir = dir;
            startX = x;
            startY = y;
            endX = x + width - 1;
            endY = y + height - 1;

            if (startX < 0) {
                endX += startX;
                startX = 0;
            }

            if (startY < 0) {
                endY += startY;
                startY = 0;
            }

            if (endX >= src.width) endX = src.width - 1;
            if (endY >= src.height) endY = src.height - 1;

            switch (this.dir) {
            case DIR_BOTTOM_LEFT:
            case DIR_LEFT_BOTTOM:
                posX = startX;
                posY = endY;
                break;
            case DIR_BOTTOM_RIGHT:
            case DIR_RIGHT_BOTTOM:
                posX = endX;
                posY = endY;
                break;
            default:// i.e. DIR_TOP_LEFT
                this.dir = DIR_TOP_LEFT;
            case DIR_LEFT_TOP:
                posX = startX;
                posY = startY;
                break;
            case DIR_RIGHT_TOP:
            case DIR_TOP_RIGHT:
                posX = endX;
                posY = startY;
                break;
            }
        }

        private void updatePosition() {
            switch (this.dir) {
            case DIR_BOTTOM_LEFT:
                posY--;
                if (posY < startY) {
                    posY = endY;
                    posX++;
                }
                break;
            case DIR_BOTTOM_RIGHT:
                posY--;
                if (posY < startY) {
                    posY = endY;
                    posX--;
                }
                break;
            case DIR_LEFT_BOTTOM:
                posX++;
                if (posX > endX) {
                    posX = startX;
                    posY--;
                }
                break;
            case DIR_LEFT_TOP:
                posX++;
                if (posX > endX) {
                    posX = startX;
                    posY++;
                }
                break;
            case DIR_RIGHT_BOTTOM:
                posX--;
                if (posX < startX) {
                    posX = endX;
                    posY--;
                }
                break;
            case DIR_RIGHT_TOP:
                posX--;
                if (posX < startX) {
                    posX = endX;
                    posY++;
                }
                break;
            case DIR_TOP_RIGHT:
                posY++;
                if (posY > endY) {
                    posY = startY;
                    posX--;
                }
                break;
            default:// i.e. DIR_TOP_LEFT
                posY++;
                if (posY > endY) {
                    posY = startY;
                    posX++;
                }
            }
        }

        /**
         * Возвращает горизонтальную позицию области сканирования.
         */
        public int getX() {
            return startX;
        }

        /**
         * Возвращает вертикальную позицию области сканирования.
         */
        public int getY() {
            return startY;
        }

        /**
         * Возвращает ширину области сканирования.
         */
        public int getWidth() {
            return endX;
        }

        /**
         * Возвращает высоту области сканирования.
         */
        public int getHeight() {
            return endY;
        }

        /**
         * Возвращает <code>false</code> если отсканирована вся область. В этом
         * случае вызов метода {@link #getNext()} или
         * {@link #changeNext(boolean)} вызовет исключение
         * {@link BadIterationException}
         * <p>
         * Если сканирование ещё не завершено, то метод возвращает
         * <code>true</code>.
         */
        public boolean hasNext() {
            return posY <= endY && posY >= startY && posX <= endX
                            && posX >= startX;
        }

        /**
         * Возвращает состояние пикселя. Внутренний указатель сканирования
         * изменяется в соответствии с направлением, заданным при создании
         * итератора.
         * 
         * @throws BadIterationException при попытке получения состояния после
         *             завершения сканирования.
         * @see #hasNext()
         */
        public boolean getNext() {
            boolean rv = false;
            if (!hasNext()) throw new BadIterationException();
            rv = parent.getPixsel(posX, posY);
            updatePosition();
            return rv;
        }

        /**
         * Изменяет пиксель. Внутренний указатель сканирования изменяется в
         * соответствии с направлением, заданным при создании итератора.
         * 
         * @param set Новое состояние пикселя.
         * @return <code>true</code> если пиксель действительно был изменён.
         * @throws BadIterationException при попытке изменения состояния после
         *             завершения сканирования.
         * @see #hasNext()
         */
        public boolean changeNext(boolean set) {
            boolean rv;
            if (!hasNext()) throw new BadIterationException();
            rv = parent.changePixsel(posX, posY, set);
            updatePosition();
            return rv;
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
     * пикселей из массива <code>src</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param src Копируемый массив.
     * @see #fromArray(boolean[], byte[])
     */
    public PixselMap(int width, int height, boolean[] src) {
        init(width, height);
        setArray(src);
    }

    /**
     * Конструктор для создания карты с заданными размерами и копированием
     * пикселей из массива <code>src</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param src Копируемый массив.
     * @see #fromArray(byte[], byte[])
     */
    public PixselMap(int width, int height, byte[] src) {
        init(width, height);
        setArray(src);
    }

    /**
     * Конструктор для получения копии карты.
     * 
     * @param src Копируемая карта.
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
     * 
     * @param src
     * @param dst
     * @param x
     * @return
     */
    public static int adjustWidth(PixselMap src, PixselMap dst, int x) {
        int w = src.width - x;
        return w > dst.width ? dst.width : w;
    }

    /**
     * 
     * @param src
     * @param dst
     * @param x
     * @return
     */
    public static int adjustHeight(PixselMap src, PixselMap dst, int y) {
        int h = src.height - y;
        return h > dst.height ? dst.height : h;
    }
    
    public PixselIterator getIterator(int x, int y, int width, int height, int dir) {
        return new PixselIterator(this, x, y, width, height, dir);
    }
    
    public boolean isEmpty() {
        return pixsels == null;
    }

    /**
     * Копирование из карты <code>map</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param src Источник копирования.
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
     * 
     * @param x
     * @param y
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
     * @param fragment
     * @param x
     * @param y
     */
    public void place(PixselMap fragment, int x, int y) {
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

    /**
     * Получение заданного пикселя.
     * 
     * @param x номер пикселя в строке. Отсчёт с нуля.
     * @param y номер строки. Отсчёт с нуля.
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
     * @param column номер пикселя в строке. Отсчёт с нуля.
     * @param row номер строки. Отсчёт с нуля.
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
        boolean changed = false;
        int nw, nh;
        byte[] narr;

        /* Проверка ширины. */
        if (w < 0) {
            nw = width;
        }
        else {
            nw = w;
        }
        /* Проверка высоты. */
        if (h < 0) {
            nh = height;
        }
        else {
            nh = h;
        }
        /* Если новые размеры равны старым, то и делать ничего не надо. */
        if (nw == width && nh == height) {
            return false;
        }
        /* Если один из размеров равен нулю, обнуляем символ. */
        if (nw == 0 || nh == 0) {
            narr = null;
        }
        else {
            /*
             * Если старый массив не пуст, копировать его (насколько возможно) в
             * новый.
             */
            if (this.pixsels == null) narr = doPixselArray(nw, nh);
            else {
                PixselMap temp = new PixselMap(nw, nh);

                int cw = nw > width ? width : nw;
                int ch = nh > height ? height : nh;

                PixselIterator si = new PixselIterator(this, 0, 0, cw, ch,
                                DIR_LEFT_TOP);
                PixselIterator di = new PixselIterator(temp, 0, 0, cw, ch,
                                DIR_LEFT_TOP);

                while (si.hasNext() && di.hasNext()) {
                    di.changeNext(si.getNext());
                }

                narr = temp.pixsels;
            }
        }

        /* Фиксируем изменения. */
        if (width != nw || height != nh) changed = true;

        pixsels = narr;
        width = nw;
        height = nh;
        return changed;
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

        PixselIterator pi = new PixselIterator(this, 0, 0, width, height,
                        DIR_LEFT_TOP);

        byte[] rv = new byte[(width * height + 7) / 8];

        for (int i = 0; pi.hasNext() && i < rv.length; i++) {
            byte m = 1;
            rv[i] = 0;
            for (int c = 0; pi.hasNext() && c < 8; c++) {
                if (pi.getNext()) rv[i] |= m;
                m = (byte) (m << 1);
            }
        }
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

        PixselIterator pi = new PixselIterator(this, 0, 0, width, height,
                        DIR_LEFT_TOP);

        int i = 0;
        while (pi.hasNext() && i < a.length) {
            pi.changeNext(a[i]);
            i++;
        }
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

        PixselIterator pi = new PixselIterator(this, 0, 0, width, height,
                        DIR_LEFT_TOP);

        for (int i = 0; pi.hasNext() && i < a.length; i++) {
            byte m = 1;
            for (int c = 0; pi.hasNext() && c < 8; c++) {
                pi.changeNext((a[i] & m) != 0);
                m = (byte) (m << 1);
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
}
