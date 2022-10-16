
package microfont.render;

/**
 * Класс для хранения информации о свойствах точки изображения (на экране) при
 * отображении карты пикселей. В зависимости от выбранного стиля точка на экране
 * может принадлежать или пикселю или зазору между пикселями.
 * 
 */
public class PointInfo {
    int     x;
    int     y;
    boolean pixsel;
    boolean deadZone;
    boolean space;

    /**
     * Возвращает горизонтальную координату пикселя, которому принадлежит точка
     * на экране. Это значение имеет смысл только если {@link #isPixsel()}
     * возвращает {@code true}.
     */
    public int getX() {
        return x;
    }

    /**
     * Возвращает вертикальную координату пикселя, которому принадлежит точка на
     * экране. Это значение имеет смысл только если {@link #isPixsel()}
     * возвращает {@code true}.
     */
    public int getY() {
        return y;
    }

    /**
     * Возвращает {@code true} если точка экрана принадлежит пикселю.
     * 
     * @see #getX()
     * @see #getY()
     */
    public boolean isPixsel() {
        return pixsel;
    }

    /**
     * Возвращает {@code true} если точка экрана лежит в "мёртвой зоне" пикселя.
     * Мёртвая зона - это небольшие участки в углах пикселя.
     */
    public boolean isDeadZone() {
        return pixsel && deadZone;
    }

    /**
     * Возвращает {@code true} если точка экрана находится в промежутке между
     * пикселями.
     */
    boolean isSpace() {
        return space;
    }

    /**
     * Возвращает {@code true} если точка экрана лежит вне карты пикселей.
     */
    public boolean isOutSide() {
        return !pixsel && !space;
    }
}
