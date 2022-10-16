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
