
package microfont;

import java.awt.Dimension;
import microfont.events.NotifyEvent;
import microfont.events.NotifyEventListener;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;
import utils.event.ListenerChain;

/**
 * Расширение карты пикселей. Этот класс добавляет функциональность карты.
 * 
 * <h3>Сообщения.</h3>
 * <p>
 * При изменениях, вызванных публичными методами, генерируются сообщения. Есть
 * два типа сообщений и получателей сообщений.
 * <ul>
 * <li>Сообщение {@link PixselMapEvent} и получатель {@link PixselMapListener}.
 * Выпуск этого сообщения тесно связан с <i>фиксацией изменений</i>,
 * функционирование которого описано в {@link AbstractPixselMap}. Всякое
 * изменение пикселей публичными методами приводит к генерации сообщения
 * <code>PixselMapEvent</code>.
 * <li>Сообщение {@link NotifyEvent} и получатель {@link NotifyEventListener}.
 * Это сообщение генерируется при изменении размеров карты.
 * </ul>
 * 
 * <h3>Операции с картой.</h3>
 * <p>
 * Кроме получения и изменения состояния одного пикселя класс предоставляет
 * возможность операций со всей картой или её фрагментом.
 * <ul>
 * <li>Операции с одной картой.
 * <ul>
 * <li><b>Сдвиг карты</b>. Универсальный метод {@link #shift(int, int)}
 * позволяет сдвигать пиксели в любую сторону на произвольный шаг. Обёртки этого
 * метода ({@link #shiftDown()}, {@link #shiftUp()}, {@link #shiftLeft()} и
 * {@link #shiftRight()}) сдвигают карту на один пиксель.
 * <li><b>Отражение карты</b>. Зеркальное отражение можно получить при помощи
 * {@link #reflectHorizontale()} и {@link #reflectVerticale()}.
 * <li><b>Вращение карты</b>. Метод {@link #rotate(int)} вращает карту на 90,
 * 180 или 270 градусов по часовой стрелке.
 * <li><b>Изменение фрагмента</b>.
 * {@link #setRectangle(int, int, int, int, boolean)} изменяет все пиксели
 * указанного фрагмента. {@link #negRectangle(int, int, int, int)} производит
 * инверсию пикселей фрагмента.
 * </ul>
 * <li>Операции с двумя картами. Эти операции используют вторую карту в качестве
 * штампа. Что бы получить фрагмент карты используйте
 * {@link #getRectangle(int, int, int, int)}. Универсальный метод
 * {@link #overlay(int, int, AbstractPixselMap, int)} может выполнять четыре
 * операции, для которых есть соответсвующие обёртки:
 * <ul>
 * <li><b>Вставка</b> {@link #place(int, int, AbstractPixselMap)}
 * <li><b>Объединение</b> {@link #or(int, int, AbstractPixselMap)}
 * <li><b>Умножение</b> {@link #and(int, int, AbstractPixselMap)}
 * <li><b>Исключение</b> {@link #hor(int, int, AbstractPixselMap)}
 * </ul>
 * </ul>
 */
public class PixselMap extends AbstractPixselMap {
    /** Сдвиг влево в {@link #shift(int, int)}. */
    public static final int SHIFT_LEFT    = 0;
    /** Сдвиг вправо в {@link #shift(int, int)}. */
    public static final int SHIFT_RIGHT   = 1;
    /** Сдвиг вверх в {@link #shift(int, int)}. */
    public static final int SHIFT_UP      = 2;
    /** Сдвиг вниз в {@link #shift(int, int)}. */
    public static final int SHIFT_DOWN    = 3;

    /** Операция вставки в {@link #overlay(int, int, AbstractPixselMap, int)}. */
    public static final int OVERLAY_PLACE = 0;
    /** Операция сложения в {@link #overlay(int, int, AbstractPixselMap, int)}. */
    public static final int OVERLAY_OR    = 1;
    /** Операция умножения в {@link #overlay(int, int, AbstractPixselMap, int)}. */
    public static final int OVERLAY_AND   = 2;
    /**
     * Операция исключения в {@link #overlay(int, int, AbstractPixselMap, int)}.
     */
    public static final int OVERLAY_HOR   = 3;

    public static String    NOTIFY_SIZE   = "PixselMapSize";

    /** Хранилище получателей сообщений. */
    protected ListenerChain listeners     = new ListenerChain();

    /**
     * Конструктор для создания карты с заданными размерами. Все пиксели
     * сброшены в <code>false</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     */
    public PixselMap(int width, int height) {
        this(width, height, null);
    }

    /**
     * Конструктор для создания карты с заданными размерами и копированием
     * пикселей из массива <code>src</code>. Пиксели в копируемом массиве
     * располагаются с младшего байта самого первого элемента и следуют без
     * пропусков.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     * @param src Копируемый массив.
     */
    public PixselMap(int width, int height, byte[] src) {
        super(width, height, src);
    }

    /**
     * Конструктор для получения копии карты.
     * 
     * @param src Копируемая карта.
     * @see #copy(PixselMap)
     */
    public PixselMap(PixselMap src) {
        this();
        try {
            this.copy(src);
        } catch (DisallowOperationException e) {
            /*
             * Поскольку PixselMap не ограничивает изменение размеров, то
             * исключение невозможно.
             */
        }
    }

    /**
     * Пустой конструктор. Символ имеет нулевую ширину и высоту.
     */
    public PixselMap() {
        this(0, 0);
    }

    /**
     * Проверка высоты. Метод всегда возвращает <code>true</code>.
     */
    @Override
    protected boolean isValidHeight(int h) {
        return true;
    }

    /**
     * Проверка ширины. Метод всегда возвращает <code>true</code>.
     */
    @Override
    protected boolean isValidWidth(int w) {
        return true;
    }

    /**
     * Добавление получателя события изменения свойств карты.
     * 
     * @param listener Добавляемый получатель события.
     */
    public void addNotifyEventListener(NotifyEventListener listener) {
        listeners.add(NotifyEventListener.class, listener);
    }

    /**
     * Удаление получателя события изменения свойств карты.
     * 
     * @param listener Удаляемый получатель события.
     */
    public void removeNotifyEventListener(NotifyEventListener listener) {
        listeners.remove(NotifyEventListener.class, listener);
    }

    /**
     * Генерация события изменения свойств карты. Получатели добавляются
     * функцией {@link #addNotifyEventListener(NotifyEventListener)}.
     */
    protected void fireNotifyEvent(NotifyEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == NotifyEventListener.class) {
                ((NotifyEventListener) listenerArray[i + 1])
                                .notifyHappened(event);
            }
        }
    }

    /**
     * Добавление получателя события при измении пикселей.
     * 
     * @param listener Добавляемый получатель события.
     */
    public void addPixselMapListener(PixselMapListener listener) {
        listeners.add(PixselMapListener.class, listener);
    }

    /**
     * Удаление получателя события при измении пикселей.
     * 
     * @param listener Удаляемый получатель события.
     */
    public void removePixselMapListener(PixselMapListener listener) {
        listeners.remove(PixselMapListener.class, listener);
    }

    /**
     * Генерация события при измении пикселей карты. Получатели добавляются
     * функцией {@link #addPixselMapListener(PixselMapListener)}.
     */
    protected void firePixselEvent() {
        PixselMapEvent change;
        Object[] listenerArray;

        if (!hasChange()) return;

        change = new PixselMapEvent(this, left, top, right - left + 1, bottom
                        - top + 1);

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PixselMapListener.class)
                ((PixselMapListener) listenerArray[i + 1])
                                .pixselChanged(change);
        }
    }

    /**
     * Копирование из карты <code>src</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param src Источник копирования.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией класса или его потомков и размер копируемой
     *             карты не совпадает с размером текущей карты.
     */
    @Override
    public void copy(AbstractPixselMap src) throws DisallowOperationException {
        int oldW, oldH, w, h;

        oldW = getWidth();
        oldH = getHeight();

        cleanChange();
        super.copy(src);

        w = getWidth();
        h = getHeight();
        if (oldW != w || oldH != h) {
            fireNotifyEvent(new NotifyEvent(this, NOTIFY_SIZE));
        }

        firePixselEvent();
    }

    /**
     * Метод изменяет ширину и высоту карты. <br>
     * Если новая высота меньше текущей, то лишние строки карты обрезаются
     * снизу. Если новая высота больше текущей, то внизу добавляются пустые
     * строки. <br>
     * Если новая ширина меньше текущей, то лишние столбцы карты обрезаются
     * справа. Если новая ширина больше текущей, то справа добавляются пустые
     * столбцы.
     * 
     * @param w Новая ширина символа.
     * @param h Новая высота символа.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией класса или его потомков.
     */
    public void setSize(int w, int h) throws DisallowOperationException {
        int oldW, oldH;

        oldW = getWidth();
        oldH = getHeight();

        cleanChange();
        super.changeSize(w, h);

        if (oldW != w || oldH != h) {
            fireNotifyEvent(new NotifyEvent(this, NOTIFY_SIZE));
        }
        firePixselEvent();
    }

    /**
     * Метод изменяет ширину и высоту карты. <br>
     * Если новая высота меньше текущей, то лишние строки карты обрезаются
     * снизу. Если новая высота больше текущей, то внизу добавляются пустые
     * строки. <br>
     * Если новая ширина меньше текущей, то лишние столбцы карты обрезаются
     * справа. Если новая ширина больше текущей, то справа добавляются пустые
     * столбцы.
     * 
     * @param sz Новые размеры карты.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией класса или его потомков.
     */
    public void setSize(Dimension sz) throws DisallowOperationException {
        setSize(sz.width, sz.height);
    }

    /**
     * Метод изменяет ширину карты. <br>
     * Если новая ширина меньше текущей, то лишние столбцы карты обрезаются
     * справа. Если новая ширина больше текущей, то справа добавляются пустые
     * столбцы.
     * 
     * @param w Новая ширина карты.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией класса или его потомков.
     */
    public void setWidth(int w) throws DisallowOperationException {
        setSize(w, getHeight());
    }

    /**
     * Метод изменяет высоту карты. <br>
     * Если новая высота меньше текущей, то лишние строки карты обрезаются
     * снизу. Если новая высота больше текущей, то внизу добавляются пустые
     * строки.
     * 
     * @param h Новая высота карты.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией класса или его потомков.
     */
    public void setHeight(int h) throws DisallowOperationException {
        setSize(getWidth(), h);
    }

    /**
     * Метод изменяет заданный пиксель.
     * 
     * @param x номер пикселя в строке. Отсчёт с нуля.
     * @param y номер строки. Отсчёт с нуля.
     * @param set <b>true</b> если пиксель должен быть установлен, <b>false</b>
     *            если нужно сбросить.
     */
    public void setPixsel(int x, int y, boolean set) {
        cleanChange();
        changePixsel(x, y, set);
        firePixselEvent();
    }

    /**
     * Метод копирует массив <b>a</b> во внутренний массив символа. Размеры
     * символа не меняются.
     * 
     * @param a Копируемый массив пикселей.
     */
    public void setArray(boolean[] a) throws IllegalArgumentException {
        cleanChange();
        setBooleans(a);
        firePixselEvent();
    }

    /**
     * Метод копирует массив пикселей, упакованных в <b>byte</b>, во внутренний
     * масссив. Пиксели в копируемом массиве располагаются с младшего байта
     * самого первого элемента и следуют без пропусков.
     * 
     * @param a Копируемый массив пикселей.
     */
    public void setArray(byte[] a) throws IllegalArgumentException {
        cleanChange();
        setBytes(a);
        firePixselEvent();
    }

    /**
     * Сдвигает пиксели карты к указанном направлении. Регион, противоположный
     * направлению сдвига, становится пустым.
     * 
     * @param dir направление сдвига. Может быть {@link #SHIFT_DOWN},
     *            {@link #SHIFT_LEFT}, {@link #SHIFT_RIGHT} или
     *            {@link #SHIFT_UP}.
     * @param step на сколько пикселей надо сдвинуть.
     */
    public void shift(int dir, int step) {
        int x, y, w, h;
        int iterDir;
        PixselIterator dst, src;

        if (this.isEmpty()) return;

        cleanChange();

        x = 0;
        y = 0;
        w = getWidth();
        h = getHeight();

        switch (dir) {
        case SHIFT_DOWN:
            iterDir = PixselIterator.DIR_RIGHT_BOTTOM;
            h -= step;
            break;
        case SHIFT_LEFT:
            iterDir = PixselIterator.DIR_TOP_LEFT;
            x = step;
            w -= step;
            break;
        case SHIFT_RIGHT:
            iterDir = PixselIterator.DIR_TOP_RIGHT;
            w -= step;
            break;
        case SHIFT_UP:
            iterDir = PixselIterator.DIR_RIGHT_TOP;
            y = step;
            h -= step;
            break;
        default:
            throw new IllegalArgumentException();
        }

        dst = getIterator(0, 0, getWidth(), getHeight(), iterDir);
        src = getIterator(x, y, w, h, iterDir);

        while (src.hasNext()) {
            dst.setNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.setNext(false);
        }

        firePixselEvent();
    }

    /**
     * Сдвиг символа вправо. После сдвига левый столбец становится пустым.
     */
    public void shiftRight() {
        shift(SHIFT_RIGHT, 1);
    }

    /**
     * Сдвиг символа влево. После сдвига правый столбец становится пустым.
     */
    public void shiftLeft() {
        shift(SHIFT_LEFT, 1);
    }

    /**
     * Сдвиг символа вверх. После сдвига нижняя строка становится пустой.
     */
    public void shiftUp() {
        shift(SHIFT_UP, 1);
    }

    /**
     * Сдвиг символа вниз. После сдвига верхняя строка становится пустой.
     */
    public void shiftDown() {
        shift(SHIFT_DOWN, 1);
    }

    /**
     * Отражение карты по вертикали.
     */
    public void reflectVerticale() {
        int w, h;
        boolean end, start;

        cleanChange();

        w = getWidth();
        h = getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w / 2; x++) {
                start = getPixsel(x, y);
                end = getPixsel(w - 1 - x, y);

                setPixsel(x, y, end);
                setPixsel(w - 1 - x, y, start);
            }
        }

        firePixselEvent();
    }

    /**
     * Отражение карты по горизонтали.
     */
    public void reflectHorizontale() {
        int w, h;
        boolean end, start;

        cleanChange();

        w = getWidth();
        h = getHeight();

        for (int y = 0; y < h / 2; y++) {
            for (int x = 0; x < w; x++) {
                start = getPixsel(x, y);
                end = getPixsel(x, h - 1 - y);

                setPixsel(x, y, end);
                setPixsel(x, h - 1 - y, start);
            }
        }

        firePixselEvent();
    }

    /**
     * Удаляет заданный столбец.
     * 
     * @param pos Позиция удаляемого столбца.
     * @param num
     * @throws DisallowOperationException
     */
    public void removeColumn(int pos, int num)
                    throws DisallowOperationException {
        PixselMap tMap;
        int w, h;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();

        if (isValidWidth(w - num))
            throw new DisallowOperationException("change width");

        tMap = new PixselMap(w - 1, h);

        if (w > 1) {
            PixselIterator dst, src;
            dst = tMap.getIterator(0, 0, w - 1, h, PixselIterator.DIR_LEFT_TOP);
            src = getIterator(0, 0, pos, h, PixselIterator.DIR_LEFT_TOP);

            while (src.hasNext()) {
                dst.setNext(src.getNext());
            }

            src = getIterator(pos + 1, 0, w - pos, h,
                            PixselIterator.DIR_LEFT_TOP);

            while (src.hasNext()) {
                dst.setNext(src.getNext());
            }
        }

        super.copy(tMap);
    }

    /**
     * Удаляет заданную строчку.
     * 
     * @param pos Позиция удаляемой строки.
     * @param num
     * @throws DisallowOperationException
     */
    public void removeRow(int pos, int num) throws DisallowOperationException {
        PixselMap tMap;
        int w, h;

        if (isEmpty()) return;

        w = getWidth();
        h = getHeight();
        if (isValidHeight(h - num))
            throw new DisallowOperationException("change height");

        tMap = new PixselMap(w, h - 1);

        if (h > 1) {
            PixselIterator dst, src;
            dst = tMap.getIterator(0, 0, w, h - 1, PixselIterator.DIR_TOP_LEFT);
            src = getIterator(0, 0, w, pos, PixselIterator.DIR_TOP_LEFT);

            while (src.hasNext()) {
                dst.setNext(src.getNext());
            }

            src = getIterator(0, pos + 1, w, h - pos,
                            PixselIterator.DIR_TOP_LEFT);

            while (src.hasNext()) {
                dst.setNext(src.getNext());
            }
        }

        super.copy(tMap);
    }

    /**
     * Поворот карты. Поворот возможен на 90, 180 или 270 градусов.
     * 
     * @param step количество четвертей круга, на которое надо повернуть карту.
     */
    public void rotate(int step) {
        AbstractPixselMap apm;
        boolean resized, reqResize;
        int w, h;
        PixselIterator spi, dpi;

        if (isEmpty()) return;
        step %= 4;
        if (step == 0) return;

        w = getWidth();
        h = getHeight();
        resized = isValidHeight(w) && isValidWidth(h);
        reqResize = step != 2;

        if (reqResize) apm = new AbstractPixselMap(h, w);
        else apm = new AbstractPixselMap(w, h);

        // Подготовка итераторов в зависимости от поворота.
        spi = getIterator(0, 0, w, h, PixselIterator.DIR_LEFT_TOP);
        switch (step) {
        case 1:
            dpi = apm.getIterator(0, 0, apm.getWidth(), apm.getHeight(),
                            PixselIterator.DIR_TOP_RIGHT);
            break;
        case 2:
            dpi = apm.getIterator(0, 0, apm.getWidth(), apm.getHeight(),
                            PixselIterator.DIR_RIGHT_BOTTOM);
            break;
        default: // i.e step == 3
            dpi = apm.getIterator(0, 0, apm.getWidth(), apm.getHeight(),
                            PixselIterator.DIR_BOTTOM_LEFT);
        }

        // Заполнение вспомогательной карты.
        while (spi.hasNext()) {
            dpi.setNext(spi.getNext());
        }

        // Изменения размера разрешены или не требуются.
        if (resized || !reqResize) {
            try {
                copy(apm);
                return;
            } catch (DisallowOperationException e) {
                // Исключение никогда не может возникнуть исходя из условий
                // блока.
            }
        }

        /*
         * Поворот на 90 или 270 градусов при запрещённых изменениях размера.
         * Ось поворота находится посередине карты. XXX это место можно
         * улучшить, попробовав подгонять координаты вставки так, что бы
         * пропадало наименьшее число закрашенных пикселей.
         */
        place((w - h) / 2, (h - w) / 2, apm);
    }

    /**
     * Создаёт карту для заданного фрагмента. Метод корректирует позицию и
     * размер фрагмента так, что бы он не выходил за пределы исходной карты.
     * 
     * @param x Начальная позиция фрагмента по горизонтали.
     * @param y Начальная позиция фрагмента по вертикали.
     * @param w Ширина фрагмента.
     * @param h Высота фрагмента.
     * @return Карта с копией пикселей заданного фрагмента.
     */
    public AbstractPixselMap getRectangle(int x, int y, int w, int h) {
        if (x < 0) {
            w += x;
            x = 0;
        }

        if (w >= getWidth()) w = getWidth() - 1;
        if (w < 0) w = 0;

        if (y < 0) {
            h += y;
            y = 0;
        }

        if (h >= getHeight()) h = getHeight() - 1;
        if (h < 0) h = 0;

        AbstractPixselMap apm = new AbstractPixselMap(w, h);
        PixselIterator spi = getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);
        PixselIterator dpi = apm.getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);

        cleanChange();

        while (spi.hasNext() && dpi.hasNext()) {
            dpi.setNext(spi.getNext());
        }

        return apm;
    }

    /**
     * Устанавливает пиксели заданного фрагмента в указанное состояние
     * <code>state</code>.
     * 
     * @param x Начальная позиция фрагмента по горизонтали.
     * @param y Начальная позиция фрагмента по вертикали.
     * @param w Ширина фрагмента.
     * @param h Высота фрагмента.
     * @param state Устанавливаемое состояние пикселей.
     */
    public void setRectangle(int x, int y, int w, int h, boolean state) {
        PixselIterator pi = getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);

        cleanChange();

        while (pi.hasNext()) {
            pi.setNext(state);
        }

        firePixselEvent();
    }

    /**
     * Инвертирует пиксели заданного фрагмента.
     * 
     * @param x Начальная позиция фрагмента по горизонтали.
     * @param y Начальная позиция фрагмента по вертикали.
     * @param w Ширина фрагмента.
     * @param h Высота фрагмента.
     */
    public void negRectangle(int x, int y, int w, int h) {
        PixselIterator spi = getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);
        PixselIterator dpi = getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);

        cleanChange();

        while (spi.hasNext()) {
            dpi.setNext(!spi.getNext());
        }

        firePixselEvent();
    }

    /**
     * Наложение карты <code>apm</code>. Тип наложения зависит от параметра
     * <code>op</code>.
     * 
     * @param x начальная позиция по горизонтали.
     * @param y начальная позиция по вертикали.
     * @param apm карта, выступающая в роли штампа.
     * @param op выполняемая операция. Может быть
     *            <ul>
     *            <li>{@link #OVERLAY_PLACE} пиксели карты замещаются пикселями
     *            штампа.
     *            <li>{@link #OVERLAY_OR} результат попиксельного ЛОГИЧЕСКОГО
     *            ИЛИ карты и штампа сохраняется в карте.
     *            <li>{@link #OVERLAY_AND} результат попиксельного ЛОГИЧЕСКОГО И
     *            карты и штампа сохраняется в карте.
     *            <li>{@link #OVERLAY_HOR} результат попиксельного ИСКЛЮЧАЮЩЕГО
     *            ИЛИ карты и штампа сохраняется в карте.
     *            </ul>
     */
    public void overlay(int x, int y, AbstractPixselMap apm, int op) {
        int srcX, srcY, w, h;

        w = apm.getWidth();
        h = apm.getHeight();
        srcX = 0;
        srcY = 0;

        if (x >= getWidth() || y >= getHeight()) return;

        if (x < 0) {
            srcX = -x;
            w += x;
            x = 0;
        }

        if (w >= getWidth()) w = getWidth() - 1;
        if (w < 0) return;

        if (y < 0) {
            srcY = -y;
            h += y;
            y = 0;
        }

        if (h >= getHeight()) h = getHeight() - 1;
        if (h < 0) return;

        cleanChange();

        PixselIterator spi = getIterator(srcX, srcY, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);
        PixselIterator tpi = apm.getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);
        PixselIterator dpi = apm.getIterator(x, y, w, h,
                        PixselIterator.DIR_LEFT_BOTTOM);

        switch (op) {
        case OVERLAY_OR:
            while (spi.hasNext() && dpi.hasNext()) {
                dpi.setNext(tpi.getNext() | spi.getNext());
            }
            break;
        case OVERLAY_AND:
            while (spi.hasNext() && dpi.hasNext()) {
                dpi.setNext(tpi.getNext() & spi.getNext());
            }
            break;
        case OVERLAY_HOR:
            while (spi.hasNext() && dpi.hasNext()) {
                dpi.setNext(tpi.getNext() ^ spi.getNext());
            }
            break;
        default:
            while (spi.hasNext() && dpi.hasNext()) {
                dpi.setNext(spi.getNext());
            }
        }

        firePixselEvent();
    }

    /**
     * Замещение пикселей из карты <code>apm</code>.
     * 
     * @param x начальная позиция по горизонтали.
     * @param y начальная позиция по вертикали.
     * @param apm карта, выступающая в роли штампа.
     */
    public void place(int x, int y, AbstractPixselMap apm) {
        overlay(x, y, apm, OVERLAY_PLACE);
    }

    /**
     * ЛОГИЧЕСКОЕ ИЛИ с <code>apm</code>.
     * 
     * @param x начальная позиция по горизонтали.
     * @param y начальная позиция по вертикали.
     * @param apm карта, выступающая в роли штампа. Результат попиксельного
     *            ЛОГИЧЕСКОГО ИЛИ карты и штампа сохраняется в карте.
     */
    public void or(int x, int y, AbstractPixselMap apm) {
        overlay(x, y, apm, OVERLAY_OR);
    }

    /**
     * ЛОГИЧЕСКОЕ И с <code>apm</code>.
     * 
     * @param x начальная позиция по горизонтали.
     * @param y начальная позиция по вертикали.
     * @param apm карта, выступающая в роли штампа. Результат попиксельного
     *            ЛОГИЧЕСКОГО И карты и штампа сохраняется в карте.
     */
    public void and(int x, int y, AbstractPixselMap apm) {
        overlay(x, y, apm, OVERLAY_AND);
    }

    /**
     * ИСКЛЮЧАЮЩЕЕ ИЛИ с <code>apm</code>.
     * 
     * @param x начальная позиция по горизонтали.
     * @param y начальная позиция по вертикали.
     * @param apm карта, выступающая в роли штампа. Результат попиксельного
     *            ИСКЛЮЧАЮЩЕГО ИЛИ карты и штампа сохраняется в карте.
     */
    public void hor(int x, int y, AbstractPixselMap apm) {
        overlay(x, y, apm, OVERLAY_HOR);
    }
}
