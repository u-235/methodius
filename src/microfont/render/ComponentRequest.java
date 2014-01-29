
package microfont.render;

import java.awt.Rectangle;

/**
 * Интерфейс для запросов перерисовки и изменения размеров. Для наследников
 * {@code swing.JComponent} реализация может выглядеть так.
 * 
 * <pre>
 * public SomeComponent extends JComponent{
 * 
 *     // Класс для получения запросов от PixselMapRender.
 *     protected Listener implements ComponentRequest{
 *         public void requestRepaint(){
 *             repaint();
 *         }
 *         
 *         public void requestRepaint(Rectangle rect){
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
