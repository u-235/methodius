
package microfont.gui;

import java.awt.event.MouseEvent;
import microfont.render.PointInfo;

/**
 * Интерфейс контроллера редактора символов.
 */
public interface Editor {
    /**
     * Действие при нажатии кнопки мышки.
     * 
     * @param comp Редактор символов.
     * @param mouse Информация о состоянии мышки.
     * @param info Информация о точке символа, соответствующей положению мышки.
     */
    public void mousePressed(MSymbolEditor comp, MouseEvent mouse,
                    PointInfo info);

    /**
     * Действие при отпускании кнопки мышки.
     * 
     * @param comp Редактор символов.
     * @param mouse Информация о состоянии мышки.
     * @param info Информация о точке символа, соответствующей положению мышки.
     */
    public void mouseReleased(MSymbolEditor comp, MouseEvent mouse,
                    PointInfo info);

    /**
     * Действие при щелчке кнопкой мышки.
     * 
     * @param comp Редактор символов.
     * @param mouse Информация о состоянии мышки.
     * @param info Информация о точке символа, соответствующей положению мышки.
     */
    public void mouseClicked(MSymbolEditor comp, MouseEvent mouse,
                    PointInfo info);

    /**
     * Действие при перемещении мышки с нажатой кнопкой.
     * 
     * @param comp Редактор символов.
     * @param mouse Информация о состоянии мышки.
     * @param info Информация о точке символа, соответствующей положению мышки.
     */
    public void mouseDragged(MSymbolEditor comp, MouseEvent mouse,
                    PointInfo info);

    /**
     * Действие при перемещении мышки без нажатия кнопок.
     * 
     * @param comp Редактор символов.
     * @param mouse Информация о состоянии мышки.
     * @param info Информация о точке символа, соответствующей положению мышки.
     */
    public void mouseMoved(MSymbolEditor comp, MouseEvent mouse, PointInfo info);
}
