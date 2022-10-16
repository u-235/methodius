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

package microfont;

/**
 * Интерфейс для управления метриками шрифта.
 */
public interface Metrics {
    /** Индекс метрики "базовая линия". */
    public static final int METRIC_BASELINE = 0;
    /** Индекс метрики "надстрочная часть". */
    public static final int METRIC_ASCENT   = 1;
    /** Индекс метрики "подстрочная часть". */
    public static final int METRIC_DESCENT  = 2;
    /** Индекс метрики "высота строчных символов". */
    public static final int METRIC_LINE     = 3;
    /** Индекс метрики "левый отступ". */
    public static final int METRIC_LEFT     = 4;
    /** Индекс метрики "правый отступ". */
    public static final int METRIC_RIGHT    = 5;
    /** Максимальный индекс метрик. */
    public static final int METRIC_MAX      = 5;

    /**
     * Возвращает {@code true} если метрика с указанным индексом действительна.
     * 
     * @param index Индекс метрики. Может быть<br>
     *            {@link #METRIC_BASELINE}<br>
     *            {@link #METRIC_ASCENT}<br>
     *            {@link #METRIC_DESCENT}<br>
     *            {@link #METRIC_LINE}<br>
     *            {@link #METRIC_LEFT}<br>
     *            {@link #METRIC_RIGHT}
     * @see #setMetricActually(int, boolean)
     * @throws IllegalArgumentException Если индекс меньше нуля или больше
     *             {@link #METRIC_MAX}
     */
    public boolean isMetricActually(int index);

    /**
     * Устанавливает состояние метрики, возвращаемое методом
     * {@link #isMetricActually(int)}.
     * 
     * @param index Индекс метрики. Может быть<br>
     *            {@link #METRIC_BASELINE}<br>
     *            {@link #METRIC_ASCENT}<br>
     *            {@link #METRIC_DESCENT}<br>
     *            {@link #METRIC_LINE}<br>
     *            {@link #METRIC_LEFT}<br>
     *            {@link #METRIC_RIGHT}
     * @param state {@code true} если метрика действительна.
     * @throws IllegalArgumentException Если индекс меньше нуля или больше
     *             {@link #METRIC_MAX}
     */
    public void setMetricActually(int index, boolean state);

    /**
     * Возвращает значение метрики.
     * 
     * @param index Индекс метрики. Может быть<br>
     *            {@link #METRIC_BASELINE}<br>
     *            {@link #METRIC_ASCENT}<br>
     *            {@link #METRIC_DESCENT}<br>
     *            {@link #METRIC_LINE}<br>
     *            {@link #METRIC_LEFT}<br>
     *            {@link #METRIC_RIGHT}
     * @throws IllegalArgumentException Если индекс меньше нуля или больше
     *             {@link #METRIC_MAX}
     */
    public int getMetric(int index);

    /**
     * Устанавливает значение метрики.
     * 
     * @param index Индекс метрики. Может быть<br>
     *            {@link #METRIC_BASELINE}<br>
     *            {@link #METRIC_ASCENT}<br>
     *            {@link #METRIC_DESCENT}<br>
     *            {@link #METRIC_LINE}<br>
     *            {@link #METRIC_LEFT}<br>
     *            {@link #METRIC_RIGHT}
     * @param value Новое значение метрики.
     * @throws IllegalArgumentException Если индекс меньше нуля или больше
     *             {@link #METRIC_MAX}
     */
    public void setMetric(int index, int value);
}
