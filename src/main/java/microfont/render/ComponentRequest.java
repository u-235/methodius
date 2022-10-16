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
