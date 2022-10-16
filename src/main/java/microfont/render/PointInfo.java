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
