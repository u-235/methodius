
package microfont.render;

import java.awt.Rectangle;

/**
 * Интерфейс для запросов перерисовки и изменения размеров.
 */
public interface ComponentRequest {
    /**
     * Запрос перерисовки всего изображения.
     */
    public void requestRepaint();

    /**
     * Запрос перерисовки фрагмента изображения.
     * @param rect Координаты фрагмента изображения.
     */
    public void requestRepaint(Rectangle rect);

    /**
     * Запрос при изменении размеров изображения.
     */
    public void requestInvalidate();
}
