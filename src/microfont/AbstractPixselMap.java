package microfont;

import java.awt.Dimension;
import java.awt.Rectangle;

import static microfont.AbstractPixselMap.PixselIterator.*;

/**
 * Базовый класс для представления карты пикселей.
 * 
 * <h3><img src="doc-files/symbol.png" align=right> Организация карты.</h3>
 * <p>
 * AbstractPixselMap разработан исходя из следующих соображений.
 * <ul>
 * <li>Пиксель может принимать два значения, поэтому методы для доступа к
 * пикселям используют <code>boolean</code> для обозначения состояния. Состоянию
 * пикселя "закрашен" соответствует <code>true</code>, состоянию "пуст" (другими
 * словами - прозрачен, то есть видна бумага) - <code>false</code>.
 * <li>Карта пикселей имеет ширину и высоту в пикселях.
 * <li>Координаты пикселя всегда положительны. Вершина координат находится в
 * левом верхнем углу карты.
 * <li>Вся область за границами символа считается прозрачной. То есть такой код
 * вернёт <code>false</code>.
 * 
 * <pre>
 *      AbstractPixselMap map;
 *      boolean   result;
 *      . . . . .
 *      result = map.getPixsel(-1, 2);
 * </pre>
 * 
 * </ul>
 * На рисунке изображена карта высотой 8 и шириной 8 пикселей. Закрашены пиксели
 * с координатам <b>0</b>:<b>0</b>, <b>1</b>:<b>0</b> и <b>2</b>:<b>1</b> в
 * формате <b><i>колонка</i></b>:<b><i>строка</i></b>.
 * 
 * <h3>Доступ к данным.</h3>
 * <p>
 * Класс предоставляет публичные методы для получения данных и защищённые - для
 * изменения. Таким образом, класс фактически является "readonly". Кроме того,
 * успешность попытки изменить размеры карты зависит от
 * {@link #isValidWidth(int)} и {@link #isValidHeight(int)}.
 * 
 * <h3>Фиксация изменений.</h3>
 * <p>
 * Методы {@link #cleanChange()}, {@link #fixChange(int, int)} и
 * {@link #hasChange()}, а так же переменные {@link #left}, {@link #right},
 * {@link #top} и {@link #bottom} предназначены для фиксации границ изменений в
 * карте. Этот механизм в первую очередь предназначен для облегчения генерации
 * сообщений о сделанных изменениях.
 * <p>
 * Суть работы этого механизма проста. Защищённые методы, изменяющие состояние
 * карты, вызывают <code>fixChange</code> только в том случае, если карта
 * действительно изменилась. В таком случае <code>hasChange</code> возвращает
 * <code>true</code>.
 * <p>
 * Публичные методы потомков класса должны придерживаться следующего правила
 * <ol>
 * <li>Вызвать <code>cleanChange</code> для сброса флага изменений.
 * <li>Выполнить требуемую работу при помощи защищённых методов этого класса.
 * <li>Проверить <code>hasChange</code> и при необходимости сгенерировать
 * сообщение.
 * </ol>
 */
public abstract class AbstractPixselMap extends Object
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

    /** Переменные для фиксации изменений. */
    protected int     left, right, top, bottom;
    /** Переменная показывает, были изменения или нет. */
    private boolean   change;

    /**
     * Итератор для последовательного доступа к пикселям прямоугольной области
     * (<i>области сканирования</i>) {@linkplain AbstractPixselMap карты}.
     * Область сканирования может быть произвольного размера, но оставаться в
     * пределах карты. Так же возможен выбор направления сканирования пикселей.
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
     * <p>
     * Текущую позицию сканирования можно получить при помощи {@link #posX()} и
     * {@link #posY()}. Следует помнить, что <code>getNext()</code> и
     * <code>changeNext()</code> изменяют текущую позицию <b>после</b> действий
     * с пикселем.
     */
    public class PixselIterator
    {
        /** Направление слева направо, сверху вниз . */
        public final static int   DIR_LEFT_TOP     = 0;
        /** Направление справа налево, сверху вниз. */
        public final static int   DIR_RIGHT_TOP    = 1;
        /** Направление слева направо, снизу вверх. */
        public final static int   DIR_LEFT_BOTTOM  = 2;
        /** Направление справа налево, снизу вверх. */
        public final static int   DIR_RIGHT_BOTTOM = 3;
        /** Направление сверху вниз, слева направо. */
        public final static int   DIR_TOP_LEFT     = 4;
        /** Направление сверху вниз, справа налево. */
        public final static int   DIR_TOP_RIGHT    = 5;
        /** Направление снизу вверх, слева направо. */
        public final static int   DIR_BOTTOM_LEFT  = 6;
        /** Направление снизу вверх, справа налево. */
        public final static int   DIR_BOTTOM_RIGHT = 7;

        /** Сканируемая карта. */
        private AbstractPixselMap parent;
        /** Направление сканирования. */
        private int               dir;
        /** Координаты области сканирования. */
        private int               startX, startY, endX, endY;
        /** Текущая позиция сканирования. */
        private int               posX, posY;

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
        protected PixselIterator(AbstractPixselMap src, int x, int y,
                        int width, int height, int dir) {
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
            switch (dir) {
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
         * @throws BadIterationException при попытке изменения состояния после
         *             завершения сканирования.
         * @see #hasNext()
         */
        protected void changeNext(boolean set) {
            if (!hasNext()) throw new BadIterationException();
            parent._changePixsel(posX, posY, set);
            updatePosition();
            return;
        }
    }

    /**
     * Конструктор для создания карты с заданными размерами. Все пиксели
     * сброшены в <code>false</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     */
    public AbstractPixselMap(int width, int height) {
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
    public AbstractPixselMap(int width, int height, byte[] src) {
        init(width, height);
        if (src != null) _setArray(src);
    }

    /**
     * Конструктор для получения копии карты.
     * 
     * @param src Копируемая карта.
     * @see #clone()
     * @see #_copy(AbstractPixselMap)
     */
    public AbstractPixselMap(AbstractPixselMap src) {
        init(src.width, src.height);
        if (pixsels != null)
            System.arraycopy(src.pixsels, 0, pixsels, 0, pixsels.length);
    }

    /**
     * Пустой конструктор. Символ имеет нулевую ширину и высоту.
     */
    public AbstractPixselMap() {
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
     * @param w Ширина карты.
     * @param x Горизонтальная позиция пикселя.
     * @param y Вертикальная позиция пикселя.
     */
    private final int index(int w, int x, int y) {
        return ((w + ITEM_SIZE - 1) >> ITEM_SHIFT) * y + (x >> ITEM_SHIFT);
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
     *            <li>{@link PixselIterator#DIR_LEFT_BOTTOM}
     *            <li>{@link PixselIterator#DIR_RIGHT_BOTTOM}
     *            <li>{@link PixselIterator#DIR_TOP_LEFT}
     *            <li>{@link PixselIterator#DIR_TOP_RIGHT}
     *            <li>{@link PixselIterator#DIR_BOTTOM_LEFT}
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

    /**
     * Метод проверяет предполагаемую высоту на допустимость.
     * 
     * @param h Проверяемая высота.
     * @return <code>true</code> если проверяемая высота является допустимой.
     * @see #isValidWidth(int)
     * @see #_setSize(int, int)
     * @see #_copy(AbstractPixselMap)
     */
    protected abstract boolean isValidHeight(int h);

    /**
     * Метод проверяет предполагаемую ширину на допустимость.
     * 
     * @param w Проверяемая ширина.
     * @return <code>true</code> если проверяемая ширина является допустимой.
     * @see #isValidHeight(int)
     * @see #_setSize(int, int)
     * @see #_copy(AbstractPixselMap)
     */
    protected abstract boolean isValidWidth(int w);

    /**
     * Возвращает <code>true</code> если после вызова {@link #cleanChange()} был
     * хотя бы один вызов {@link #fixChange(int, int)}.
     */
    protected boolean hasChange() {
        return change;
    }

    /**
     * Сбрасывает внутренний флаг изменений.
     * 
     * @see #fixChange(int, int)
     * @see #hasChange()
     */
    protected void cleanChange() {
        change = false;
    }

    /**
     * Изменяет границы области изменений так, что бы точка с указанными
     * координатами попадала в эту область. Так же устанавливается флаг
     * изменений.
     * 
     * @param x Горизонтальная координата изменённого пикселя.
     * @param y Вертикальная координата изменённого пикселя.
     * @see #cleanChange()
     * @see #hasChange()
     */
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
     * Возвращает координаты области изменений, если они были, или
     * <code>null</code> если изменений не было.
     */
    protected Rectangle getChange() {
        if (!hasChange()) return null;
        return new Rectangle(left, top, right - left + 1, bottom - top + 1);
    }

    /**
     * Сравнение карт. Карты считаются равными, если у них совпадают ширина,
     * высота и содержимое массивов пикселей.
     * 
     * @param s Карта для сравнения.
     * @return <code>true</code> если символы равны.
     */
    @Override
    public boolean equals(Object s) {
        if (s == null) return false;

        if (!(s instanceof AbstractPixselMap)) return false;

        return equals((AbstractPixselMap) s);
    }

    /**
     * Сравнение карт. Карты считаются равными, если у них совпадают ширина,
     * высота и содержимое массивов пикселей.
     * 
     * @param s Карта для сравнения.
     * @return <code>true</code> если символы равны.
     */
    public boolean equals(AbstractPixselMap s) {
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
     * Метод возвращает ширину и высоту карты.
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Метод возвращает ширину карты.
     * 
     * @return Количество пикселей по горизонтали.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Метод возвращает высоту карты.
     * 
     * @return Количество пикселей по вертикали.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Изменение размеров карты. Так же меняется размер массива пикселей. Если
     * один из размеров равен нулю, то внутренний массив пикселей освобождается.
     * 
     * @param w Новая ширина.
     * @param h Новая высота.
     * @throws IllegalArgumentException если хотя бы один из размеров меньше
     *             нуля.
     * @throws DisallowOperationException если изменение высоты и/или ширины
     *             запрещено текущей конфигурацией.
     * @see #isValidWidth(int)
     * @see #isValidHeight(int)
     */
    protected final void _setSize(int w, int h)
                    throws DisallowOperationException {
        int nw, nh;

        if (w < 0 || h < 0) throw new IllegalArgumentException("bad size");

        if (!isValidWidth(w))
            throw new DisallowOperationException("change width");
        if (!isValidHeight(h))
            throw new DisallowOperationException("change width");

        nw = w;
        nh = h;

        /* Если новые размеры равны старым, то и делать ничего не надо. */
        if (nw == width && nh == height) return;

        /* Если один из размеров равен нулю, обнуляем символ. */
        if (nw == 0 || nh == 0) pixsels = null;
        else {
            /*
             * Если старый массив не пуст, копировать его (насколько возможно) в
             * новый.
             */
            if (pixsels == null) pixsels = doPixselArray(nw, nh);
            else {
                byte[] temp = pixsels;
                int oldW = width;
                pixsels = doPixselArray(nw, nh);

                int cw = nw > width ? width : nw;
                int ch = nh > height ? height : nh;

                width = nw;
                height = nh;

                for (int x = 0; x < cw; x++) {
                    for (int y = 0; y < ch; y++) {
                        _changePixsel(x, y, _getPixsel(temp, oldW, x, y));
                    }
                }
            }
        }

        /* Фиксируем изменения. */
        fixChange(0, 0);
        fixChange(nw - 1, nh - 1);

        width = nw;
        height = nh;
        return;
    }

    protected final boolean _getPixsel(byte[] pixsels, int w, int x, int y) {
        int i;
        if (x < 0 || x >= w) return false;
        i = index(w, x, y);
        if (i >= pixsels.length) return false;

        return (pixsels[i] & (1 << (x & ITEM_MASK))) != 0;
    }

    /**
     * Получение заданного пикселя.
     * 
     * @param x номер пикселя в строке. Отсчёт с нуля.
     * @param y номер строки. Отсчёт с нуля.
     * @return <code>true</code> если пиксель установлен. Метод возвращает
     *         <code>false</code> если пиксель сброшен, а так же если параметры
     *         <code>x</code> и <code>y</code> выходят за границы символа.
     */
    public boolean getPixsel(int x, int y) {
        return _getPixsel(pixsels, width, x, y);
    }

    /**
     * Метод изменяет заданный пиксель.
     * 
     * @param x номер пикселя в строке. Отсчёт с нуля.
     * @param y номер строки. Отсчёт с нуля.
     * @param set <code>true</code> если пиксель должен быть установлен,
     *            <code>false</code> если нужно сбросить.
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
     */
    protected final void _changePixsel(int x, int y, boolean set) {
        int index;
        byte mask;
        if (x < 0 || x >= width)
            throw (new IllegalArgumentException(" : posX"));

        if (y < 0 || y >= height)
            throw (new IllegalArgumentException(" : posY"));

        index = index(width, x, y);
        mask = (byte) (1 << (x & ITEM_MASK));

        if (((pixsels[index] & mask) != 0) != set) {
            if (set) {
                pixsels[index] |= mask;
            }
            else {
                pixsels[index] &= (byte) ~mask;
            }
            fixChange(x, y);
        }
    }

    /**
     * Копирование из карты <code>src</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param src Источник копирования.
     * @throws DisallowOperationException если изменение высоты и/или ширины
     *             запрещено текущей конфигурацией.
     * @throws NullPointerException если <code>src</code> равен
     *             <code>null</code>
     * @see #isValidWidth(int)
     * @see #isValidHeight(int)
     * @see #clone()
     */
    protected final void _copy(AbstractPixselMap src)
                    throws DisallowOperationException {
        if (src == null) throw (new NullPointerException());

        if (!isValidWidth(src.width))
            throw new DisallowOperationException("change width");
        if (!isValidHeight(src.height))
            throw new DisallowOperationException("change width");

        pixsels = doPixselArray(src.width, src.height);

        if (pixsels != null)
            System.arraycopy(src.pixsels, 0, pixsels, 0, pixsels.length);

        width = src.width;
        height = src.height;
    }

    /**
     * Метод возвращает <b>копию</b> массива пикселей. Если символ имеет нулевую
     * ширину и/или высоту, то возвращается <code>null</code>.
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
     * <code>byte</code>. Если символ имеет нулевую ширину и/или высоту, то
     * возвращается <code>null</code>. <br>
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
     * Метод копирует массив <code>src</code> во внутренний массив символа.
     * Размеры символа не меняются.
     * 
     * @param src Копируемый массив пикселей.
     * @throws NullPointerException если <code>src</code> равен
     *             <code>null</code>
     */
    protected final void _setArray(boolean[] src) throws NullPointerException {
        if (src == null) throw (new NullPointerException());

        PixselIterator pi = new PixselIterator(this, 0, 0, width, height,
                        DIR_LEFT_TOP);

        int i = 0;
        while (pi.hasNext() && i < src.length) {
            pi.changeNext(src[i]);
            i++;
        }
    }

    /**
     * Метод копирует массив пикселей, упакованных в <code>byte</code>, во
     * внутренний масссив. Пиксели в копируемом массиве располагаются с младшего
     * байта самого первого элемента.
     * 
     * @param src Копируемый массив пикселей.
     * @throws NullPointerException если <code>src</code> равен
     *             <code>null</code>
     */
    protected final void _setArray(byte[] src) throws NullPointerException {
        if (src == null) throw (new NullPointerException());

        PixselIterator pi = new PixselIterator(this, 0, 0, width, height,
                        DIR_LEFT_TOP);

        for (int i = 0; pi.hasNext() && i < src.length; i++) {
            byte m = 1;
            for (int c = 0; pi.hasNext() && c < 8; c++) {
                pi.changeNext((src[i] & m) != 0);
                m = (byte) (m << 1);
            }
        }
    }
}
