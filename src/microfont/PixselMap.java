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

    protected int     left, right, top, bottom;
    private boolean   change;

    /**
     * Итератор для последовательного доступа к пикселям прямоугольной области
     * (<i>области сканирования</i>) {@linkplain PixselMap карты}. Область
     * сканирования может быть произвольного размера, но оставаться в пределах
     * карты. Так же возможен выбор направления сканирования пикселей.
     * <p>
     * Размеры, задаваемые при создании, могут быть скорректированы, если
     * область сканирования выходит за границы карты. Например, если начальная
     * точка по горизонтали <b>x</b> равна -3 и ширина области <b>w</b> равна 7,
     * то <b>x</b> будет 0 и <b>w</b> станет 4. Если же ширина карты меньше
     * <code>x+w</code> , то <b>w</b> будет соответственно уменьшен. Так же
     * корректируются вертикальные размеры. Получить действительные размеры
     * можно при помощи {@link #getX()}, {@link #getY()}, {@link #getWidth()} и
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
            return endX - startX + 1;
        }

        /**
         * Возвращает высоту области сканирования.
         */
        public int getHeight() {
            return endY - startY + 1;
        }

        /**
         * Возвращает текущую позицию сканирования по горизонтали.
         */
        public int posX() {
            return posX;
        }

        /**
         * Возвращает текущую позицию сканирования по вертикали.
         */
        public int posY() {
            return posY;
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
     */
    public PixselMap(int width, int height, boolean[] src) {
        init(width, height);
        if (src != null) this.setArray(src);
    }

    /**
     * Конструктор для создания карты с заданными размерами и копированием
     * пикселей из массива <code>src</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param src Копируемый массив.
     */
    public PixselMap(int width, int height, byte[] src) {
        init(width, height);
        if (src != null) this.setArray(src);
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

    /**
     * Возвращает индекс байта в массиве для пикселя с заданной позицией.
     * 
     * @param x Горизонтальная позиция пикселя.
     * @param y Вертикальная позиция пикселя.
     */
    private int index(int x, int y) {
        return ((width + ITEM_SIZE - 1) >> ITEM_SHIFT) * y + (x >> ITEM_SHIFT);
    }

    /**
     * Возвращает {@linkplain PixselIterator итератор} карты с заданной областью
     * и направлением сканирования.
     * 
     * @param x Горизонтальная начальная координата области сканирования.
     * @param y Вертикальная начальная координата области сканирования.
     * @param width Ширина области сканирования.
     * @param height Высота области сканирования.
     * @param dir Направление сканирования. Может быть одним из
     *            <ul>
     *            <li>{@link PixselIterator#DIR_LEFT_TOP}
     *            <li>{@link PixselIterator#DIR_RIGHT_TOP}
     *            <li>
     *            {@link PixselIterator#DIR_LEFT_BOTTOM}
     *            <li>{@link PixselIterator#DIR_RIGHT_BOTTOM}
     *            <li>{@link PixselIterator#DIR_TOP_LEFT}
     *            <li>{@link PixselIterator#DIR_TOP_RIGHT}
     *            <li>
     *            {@link PixselIterator#DIR_BOTTOM_LEFT}
     *            <li>{@link PixselIterator#DIR_BOTTOM_RIGHT}
     *            </ul>
     * @return Итератор для карты.
     */
    public PixselIterator getIterator(int x, int y, int width, int height,
                    int dir) {
        return new PixselIterator(this, x, y, width, height, dir);
    }

    /**
     * Возвращает <code>true</code> если карта пуста. Это значит, что по крайней
     * мере один из размеров карты равен нулю.
     */
    public boolean isEmpty() {
        return pixsels == null;
    }

    protected boolean hasChange() {
        return change;
    }

    protected void cleanChange() {
        change = false;
    }

    protected void fixChange(int x, int y) {
        if (!change) {
            left = x;
            right = x;
            top = y;
            bottom = y;
        }

        change = true;

        left = left < x ? left : x;
        right = right > x ? right : x;
        top = top < y ? top : y;
        bottom = bottom > y ? bottom : y;
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
     */
    protected void changeSize(int w, int h) {
        int nw, nh;
        byte[] narr;

        cleanChange();

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
            return;
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
            if (pixsels == null) narr = doPixselArray(nw, nh);
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
        if (width != nw || height != nh) {
            fixChange(0, 0);
            fixChange(nw - 1, nh - 1);
        }

        pixsels = narr;
        width = nw;
        height = nh;
        return;
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
     * Метод возвращает высоту символа.
     * 
     * @return Количество пикселей по вертикали.
     */
    public int getHeight() {
        return height;
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
}
