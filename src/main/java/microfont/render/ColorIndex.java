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
 * Константы для обозначения индекса цвета в {@link PixselMapRender}.
 */
public interface ColorIndex {
    /** Индекс цвета бумаги. */
    public final static int COLOR_PAPER         = 0;
    /** Индекс цвета чернил. */
    public final static int COLOR_INK           = 1;
    /** Индекс цвета бумаги для полей. */
    public final static int COLOR_PAPER_MARGINS = 2;
    /** Индекс цвета чернил для полей. */
    public final static int COLOR_INK_MARGINS   = 3;
    /** Индекс цвета бумаги для надстрочной символа. */
    public final static int COLOR_PAPER_ASCENT  = 4;
    /** Индекс цвета чернил для надстрочной символа. */
    public final static int COLOR_INK_ASCENT    = 5;
    /** Индекс цвета бумаги для подстрочной части символа. */
    public final static int COLOR_PAPER_DESCENT = 6;
    /** Индекс цвета чернил для подстрочной части символа. */
    public final static int COLOR_INK_DESCENT   = 7;
    /** Индекс цвета зазора между пикселями. */
    public final static int COLOR_SPACE         = 8;
    /** Индекс цвета сетки. */
    public final static int COLOR_GRID          = 9;
    /** Максимальный индекс цвета. */
    public final static int COLOR_MAX           = 9;
}
