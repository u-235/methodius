/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
