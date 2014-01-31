
package microfont.render;

import java.awt.Rectangle;

/**
 * Интерфейс для запросов перерисовки и изменения размеров. Для наследников
 * {@code swing.JComponent} реализация может выглядеть так.
 * 
 * <pre>
 * public SomeComponent extends JComponent{
 *     // Горизонтальная позиция начала отрисовки карты пикселей.
 *     int renderX;
 *     // Вертикальная позиция начала отрисовки карты пикселей.
 *     int renderY;
 * 
 *     // Класс для получения запросов от PixselMapRender.
 *     protected class Listener implements ComponentRequest{
 *         public void requestRepaint(){
 *             repaint();
 *         }
 *         
 *         public void requestRepaint(Rectangle rect){
 *             rect.x += renderX;
 *             rect.y += renderY;
 *             repaint(rect);
 *         }
 *         
 *         public void requestInvalidate(){
 *             invalidate();
 *         }
 *     }
 * }
 * </pre>
 */
public interface ComponentRequest {
    /**
     * Запрос перерисовки всего изображения.
     */
    public void requestRepaint();

    /**
     * Запрос перерисовки фрагмента изображения.
     * 
     * @param rect Координаты фрагмента изображения.
     */
    public void requestRepaint(Rectangle rect);

    /**
     * Запрос при изменении размеров изображения.
     */
    public void requestInvalidate();
}
