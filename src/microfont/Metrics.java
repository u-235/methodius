
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
