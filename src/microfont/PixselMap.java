
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
 * <li>Операции с двумя картами. {@link #getRectangle(int, int, int, int)}
 * <ul>
 * <li><b></b> {@link #place(int, int, AbstractPixselMap)}
 * <li><b></b> {@link #or(int, int, AbstractPixselMap)}
 * <li><b></b> {@link #and(int, int, AbstractPixselMap)}
 * <li><b></b> {@link #hor(int, int, AbstractPixselMap)}
 * </ul>
 * </ul>
 */
public class PixselMap extends AbstractPixselMap {
    /** Сдвиг влево. */
    public static final int SHIFT_LEFT  = 0;
    /** Сдвиг вправо. */
    public static final int SHIFT_RIGHT = 1;
    /** Сдвиг вверх. */
    public static final int SHIFT_UP    = 2;
    /** Сдвиг вниз. */
    public static final int SHIFT_DOWN  = 3;

    /** Хранилище получателей сообщений. */
    protected ListenerChain listeners   = new ListenerChain();

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
     * пикселей из массива <code>src</code>.
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
     * Копирование из карты <code>map</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param src Источник копирования.
     * @throws DisallowOperationException
     */
    public void copy(PixselMap src) throws DisallowOperationException {
        int oldW, oldH, w, h;

        oldW = getWidth();
        oldH = getHeight();

        cleanChange();
        super.copy(src);

        w = getWidth();
        h = getHeight();
        if (oldW != w || oldH != h) {
            fireNotifyEvent(new NotifyEvent(this, "size"));
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
     * @throws DisallowOperationException
     */
    public void setSize(int w, int h) throws DisallowOperationException {
        int oldW, oldH;

        oldW = getWidth();
        oldH = getHeight();

        cleanChange();
        super.changeSize(w, h);

        if (oldW != w || oldH != h) {
            fireNotifyEvent(new NotifyEvent(this, "size"));
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
     * @throws DisallowOperationException
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
     * @throws DisallowOperationException
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
     * @throws DisallowOperationException
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
     * @throws IllegalArgumentException если позиция выходит за рамки символа.
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
     * @throws IllegalArgumentException
     */
    public void setArray(boolean[] a) throws IllegalArgumentException {
        cleanChange();
        setBooleans(a);
        firePixselEvent();
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
        cleanChange();
        setBytes(a);
        firePixselEvent();
    }

    public void shift(int dir, int step) {
        int x, y, w, h;
        int iterDir;
        PixselIterator dst, src;

        if (this.isEmpty()) return;

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
            dst.changeNext(src.getNext());
        }

        while (dst.hasNext()) {
            dst.changeNext(false);
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

    public void reflectVerticale() {
        int w, h;
        boolean end, start;

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

    public void reflectHorizontale() {
        int w, h;
        boolean end, start;

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

    public void rotate(int step) {
        /* FIXME */
    }

    public void setRectangle(int x, int y, int w, int h, boolean state) {
        /* FIXME */
    }

    public void negRectangle(int x, int y, int w, int h) {
        /* FIXME */
    }

    public AbstractPixselMap getRectangle(int x, int y, int w, int h) {
        /* FIXME */
        return null;
    }

    public void place(int x, int y, AbstractPixselMap apm) {
        /* FIXME */
    }

    public void or(int x, int y, AbstractPixselMap apm) {
        /* FIXME */
    }

    public void and(int x, int y, AbstractPixselMap apm) {
        /* FIXME */
    }

    public void hor(int x, int y, AbstractPixselMap apm) {
        /* FIXME */
    }
}
