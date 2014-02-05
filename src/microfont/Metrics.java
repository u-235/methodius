
package microfont;

public interface Metrics {
    public static final int METRIC_BASELINE = 0;
    public static final int METRIC_LINE     = 1;
    public static final int METRIC_ASCENT   = 2;
    public static final int METRIC_DESCENT  = 3;
    public static final int METRIC_LEFT     = 4;
    public static final int METRIC_RIGHT    = 5;
    public static final int METRIC_MAX      = 5;

    public boolean isActuallyMetric(int index);

    public void clearActuallyMetric(int index);

    public int getMetric(int index);

    public void setMetric(int index, int value);
}
