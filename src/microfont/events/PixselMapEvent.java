
package microfont.events;

import java.awt.Rectangle;
import java.util.EventObject;
import microfont.PixselMap;

/**
 * Событие при изменении состояния пикселей {@link PixselMap}.
 */
public class PixselMapEvent extends EventObject {
    private static final long serialVersionUID = 4283930318715669061L;
    private int               x, y, width, height;

    /**
     * Создание события.
     * 
     * @param source Карта, в которой произошли изменения.
     * @param x Горизонтальная позиция начала фрагмента с изменившимися
     *            пикселями.
     * @param y Вертикальная позиция начала фрагмента с изменившимися пикселями.
     * @param width Ширина фрагмента с изменившимися пикселями.
     * @param height Высота фрагмента с изменившимися пикселями.
     */
    public PixselMapEvent(PixselMap source, int x, int y, int width, int height) {
        super(source);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Создание события.
     * 
     * @param source Карта, в которой произошли изменения.
     * @param rect Координаты фрагмента карты с изменившимися пикселями.
     */
    public PixselMapEvent(PixselMap source, Rectangle rect) {
        this(source, rect.x, rect.x, rect.width, rect.height);
    }

    /**
     * Возвращает координаты фрагмента карты с изменившимися пикселями.
     */
    public Rectangle rect() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Возвращает горизонтальную позицию начала фрагмента карты с изменившимися
     * пикселями.
     */
    public int x() {
        return x;
    }

    /**
     * Возвращает вертикальную позицию начала фрагмента карты с изменившимися
     * пикселями.
     */
    public int y() {
        return y;
    }

    /**
     * Возвращает ширину фрагмента карты с изменившимися пикселями.
     */
    public int width() {
        return width;
    }

    /**
     * Возвращает высоту фрагмента карты с изменившимися пикселями.
     */
    public int height() {
        return height;
    }
}
