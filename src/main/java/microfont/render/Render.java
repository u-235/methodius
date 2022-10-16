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

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Интерфейс отрисовщика символов.
 */
public interface Render {
    /**
     * Отрисовка карты пикселей.
     * 
     * @param g Графический контекст для отрисовки.
     * @param x Начальная координата по горизонтали.
     * @param y Начальная координата по вертикали.
     */
    public void paint(Graphics g, int x, int y);

    /**
     * Возвращает ширину картинки, отрисовываемой рендером.
     */
    public int getWidth();

    /**
     * Возвращает высоту картинки, отрисовываемой рендером.
     */
    public int getHeight();

    /**
     * Возвращает информацию о точке изображения как о части карты пикселей.
     * 
     * @param info Объект для сохранения информации. Может быть {@code null}; в
     *            этом случае метод вернёт новый объект.
     * @param x Горизонтальная позиция точки изображения.
     * @param y Вертикальная позиция точки изображения.
     * @return {@code info} или, если {@code info} был {@code null}, новый
     *         объект с информацией о точке изображения.
     */
    public PointInfo getPointInfo(PointInfo info, int x, int y);

    /**
     * Преобразует горизонтальную координату карты пикселей в координату
     * изображения. Координата изображения берётся для левого края указанного
     * пикселя.
     * 
     * @param x Горизонтальная координата карты пикселей.
     * @return Горизонтальная координата изображения.
     */
    public int pixselToPointX(int x);

    /**
     * Преобразует вертикальную координату карты пикселей в координату
     * изображения. Координата изображения берётся для верхнего края указанного
     * пикселя.
     * 
     * @param y Вертикальная координата карты пикселей.
     * @return Вертикальная координата изображения.
     */
    public int pixselToPointY(int y);

    /**
     * Преобразует горизонтальную координату изображения в координату карты
     * пикселей. Координата карты пикселей соответствует пикселю, которому
     * принадлежит точка изображения или пикселю левее зазора, если точка
     * изображения приходится на зазор между пикселями.
     * 
     * @param x Горизонтальная координата изображения.
     * @return Горизонтальная координата карты пикселей.
     */
    public int pointToPixselX(int x);

    /**
     * Преобразует вертикальную координату изображения в координату карты
     * пикселей. Координата карты пикселей соответствует пикселю, которому
     * принадлежит точка изображения или пикселю выше зазора, если точка
     * изображения приходится на зазор между пикселями.
     * 
     * @param y Вертикальная координата изображения.
     * @return Вертикальная координата карты пикселей.
     */
    public int pointToPixselY(int y);

    /**
     * Преобразует координаты прямоугольника изображения в координаты карты
     * пикселей.
     * 
     * @param points Координаты прямоугольника изображения.
     * @param pixsels Объект для результата преобразования. Может быть
     *            {@code null}, в этом случае создаётся и возвращается новый
     *            объект. Так же может быть {@code points}, если координаты
     *            изображения в дальнейшем не нужны.
     * @return {@code pixsels} или новый объект с результатом преобразования.
     * @throws NullPointerException Если {@code points} равен {@code null}.
     */
    public Rectangle toPixselRect(Rectangle points, Rectangle pixsels);

    /**
     * Преобразует координаты прямоугольника карты пикселей в координаты
     * изображения.
     * 
     * @param pixsels Координаты прямоугольника карты пикселей.
     * @param points Объект для результата преобразования. Может быть
     *            {@code null}, в этом случае создаётся и возвращается новый
     *            объект. Так же может быть {@code pixsels}, если координаты
     *            карты пикселей в дальнейшем не нужны.
     * @return {@code points} или новый объект с результатом преобразования.
     * @throws NullPointerException Если {@code pixsels} равен {@code null}.
     */
    public Rectangle toPointRect(Rectangle pixsels, Rectangle points);
}
