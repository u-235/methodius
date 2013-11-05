package microfont;

import java.awt.Dimension;
import java.util.EventListener;

import utils.event.ListenerChain;

import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

/**
 * Класс для представления карты пикселей.
 */
public class PixselMap extends AbstractPixselMap
{
    public static final int SHIFT_LEFT  = 0;
    public static final int SHIFT_RIGHT = 1;
    public static final int SHIFT_UP    = 2;
    public static final int SHIFT_DOWN  = 3;

    /** Список получателей события после изменения символа. */

    private class Chain extends ListenerChain<MSymbolEvent>
    {
        @Override
        protected void listenerCall(EventListener listener,
                        MSymbolEvent event) {
            ((MSymbolListener) listener).mSymbolEvent(event);
        }
    }

    private Chain listListener = new Chain();

    /**
     * Конструктор для создания карты с заданными размерами. Все пиксели
     * сброшены в <code>false</code>.
     * 
     * @param width Ширина карты.
     * @param height Высота карты.
     */
    public PixselMap(int width, int height) {
        super(width, height);
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
     * @see #clone()
     * @see #copy(PixselMap)
     */
    public PixselMap(PixselMap src) {
        super(src);
    }

    /**
     * Пустой конструктор. Символ имеет нулевую ширину и высоту.
     */
    public PixselMap() {
        super();
    }

    @Override
    protected boolean isValidHeight(int h) {
        return true;
    }

    @Override
    protected boolean isValidWidth(int w) {
        return true;
    }

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
    protected void fireEvent(int reason) {
        MSymbolEvent change;

        if (listListener == null) return;

        if (!hasChange()) return;

        change = new MSymbolEvent(this, reason, left, top, right - left + 1,
                        bottom - top + 1);
        listListener.fire(change);
    }

    /**
     * Копирование из карты <code>map</code>. Кроме массива пикселей изменяются
     * переменные {@link #width}, {@link #height}.
     * 
     * @param src Источник копирования.
     * @throws DisallowOperationException
     * @see #clone()
     */
    public void copy(PixselMap src) throws DisallowOperationException {
        super._copy(src);
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
    public boolean equals(PixselMap s) {
        return super.equals(s);
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
     * @param w Новая ширина символа.
     * @param h Новая высота символа.
     * @throws DisallowOperationException
     */
    public void setSize(int w, int h) throws DisallowOperationException {
        cleanChange();
        super._setSize(w, h);
        fireEvent(MSymbolEvent.SIZE);
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
     * @param sz .
     * @throws DisallowOperationException
     */
    public void setSize(Dimension sz) throws DisallowOperationException {
        setSize(sz.width, sz.height);
    }

    public void setWidth(int w) throws DisallowOperationException {
        setSize(w, getHeight());
    }

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
    public void changePixsel(int x, int y, boolean set) {
        cleanChange();
        _changePixsel(x, y, set);
        fireEvent(MSymbolEvent.PIXSEL);
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
        _setArray(a);
        fireEvent(MSymbolEvent.COPY);
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
        _setArray(a);
        fireEvent(MSymbolEvent.COPY);
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

        fireEvent(MSymbolEvent.SHIFT);
    }

    /**
     * Сдвиг символа вправо. После сдвига левый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftRight() {
        shift(SHIFT_RIGHT, 1);
    }

    /**
     * Сдвиг символа влево. После сдвига правый столбец становится пустым. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftLeft() {
        shift(SHIFT_LEFT, 1);
    }

    /**
     * Сдвиг символа вверх. После сдвига нижняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
     */
    public void shiftUp() {
        shift(SHIFT_UP, 1);
    }

    /**
     * Сдвиг символа вниз. После сдвига верхняя строка становится пустой. <br>
     * Генерируется сообщение {@link MSymbolEvent#SHIFT SHIFT}.
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

                changePixsel(x, y, end);
                changePixsel(w - 1 - x, y, start);
            }
        }

        fireEvent(MSymbolEvent.COPY);
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

                changePixsel(x, y, end);
                changePixsel(x, h - 1 - y, start);
            }
        }

        fireEvent(MSymbolEvent.COPY);
    }
}
