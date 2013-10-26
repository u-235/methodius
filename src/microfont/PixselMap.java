package microfont;

import java.awt.Dimension;
import utils.event.DataEventListener;
import utils.event.ListenerChain;

import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

/**
 * Класс для представления карты пикселей.
 */
public class PixselMap extends AbstractPixselMap
{
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
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected boolean isValidWidth(int w) {
        // TODO Auto-generated method stub
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
        setSize(getHeight(), h);
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

        fireEvent(MSymbolEvent.SHIFT);
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

        fireEvent(MSymbolEvent.SHIFT);
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

        fireEvent(MSymbolEvent.SHIFT);
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

        fireEvent(MSymbolEvent.SHIFT);
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
            fireEvent(MSymbolEvent.COPY);
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
            fireEvent(MSymbolEvent.COPY);
    }
}
