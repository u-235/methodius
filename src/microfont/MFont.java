
package microfont;

import java.util.logging.Level;

/**
 * 
 */
public class MFont extends AbstractMFont implements Metrics {
    /** Высота надстрочной части символа. */
    public static final String PROPERTY_ASCENT                = "mf.ascent";
    /** Базовая линия строки. */
    public static final String PROPERTY_BASELINE              = "mf.baseline";
    /** Подстрочная часть символа. */
    public static final String PROPERTY_DESCENT               = "mf.descent";
    /** Высота строки символов. */
    public static final String PROPERTY_LINE                  = "mf.line";
    /** Левое поле. */
    public static final String PROPERTY_MARGIN_LEFT           = "mf.magrin.left";
    /** Правое поле. */
    public static final String PROPERTY_MARGIN_RIGHT          = "mf.margin.right";
    /** Высота надстрочной части символа. */
    public static final String PROPERTY_ACTUALLY_ASCENT       = "mf.state.ascent";
    /** Базовая линия строки. */
    public static final String PROPERTY_ACTUALLY_BASELINE     = "mf.state.baseline";
    /** Подстрочная часть символа. */
    public static final String PROPERTY_ACTUALLY_DESCENT      = "mf.state.descent";
    /** Высота строки символов. */
    public static final String PROPERTY_ACTUALLY_LINE         = "mf.state.line";
    /** Левое поле. */
    public static final String PROPERTY_ACTUALLY_MARGIN_LEFT  = "mf.state.magrin.left";
    /** Правое поле. */
    public static final String PROPERTY_ACTUALLY_MARGIN_RIGHT = "mf.state.margin.right";
    public static final String PROPERTY_DESCRIPTION           = "mf.description";
    public static final String PROPERTY_NAME                  = "mf.name";
    public static final String PROPERTY_PROTOTYPE             = "mf.prototype";

    private String             name;
    private String             prototype;
    private String             description;
    private int[]              metrics;
    private boolean[]          actually;

    /**
     * Создание пустого шрифта.
     */
    public MFont() {
        super();
        metrics = new int[METRIC_MAX + 1];
        actually = new boolean[METRIC_MAX + 1];
        for (int i = 0; i <= METRIC_MAX; i++) {
            metrics[i] = 0;
            actually[i] = false;
        }
    }

    @Override
    public MFont clone() {
        MFont ret = new MFont();
        synchronized (getLock()) {
            ret.copy(this);
            return ret;
        }
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        synchronized (getLock()) {
            int result = super.hashCode();

            for (int i = 0; i <= METRIC_MAX; i++) {
                result = prime * result + (actually[i] ? 1231 : 1237);
                result = prime * result + (actually[i] ? metrics[i] : 0);
            }

            result = prime
                            * result
                            + ((description == null) ? 0 : description
                                            .hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                            + ((prototype == null) ? 0 : prototype.hashCode());
            return result;
        }
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof MFont)) return false;
        synchronized (getLock()) {
            MFont other = (MFont) obj;
            synchronized (other.getLock()) {
                for (int i = 0; i < METRIC_MAX; i++) {
                    if (actually[i] != other.actually[i]) return false;
                    if (actually[i] && metrics[i] != other.metrics[i])
                        return false;
                }
                if (description == null) {
                    if (other.description != null) return false;
                } else if (!description.equals(other.description))
                    return false;
                if (name == null) {
                    if (other.name != null) return false;
                } else if (!name.equals(other.name)) return false;
                if (prototype == null) {
                    if (other.prototype != null) return false;
                } else if (!prototype.equals(other.prototype)) return false;
                return true;
            }
        }
    }

    /**
     * Копирует шрифт. Получатели сообщений не копируются.
     * 
     * @param font Копируемый шрифт.
     */
    public void copy(MFont font) {
        synchronized (font.getLock()) {
            synchronized (getLock()) {
                super.copy(font);

                for (int i = 0; i <= METRIC_MAX; i++) {
                    setMetricActually(i, font.isMetricActually(i));
                    setMetric(i, font.getMetric(i));
                }

                setName(font.name);
                setPrototype(font.prototype);
            }
        }
    }

    /**
     * Возвращает имя шрифта.
     * 
     * @return Имя шрифта, может быть {@code null}.
     */
    public String getName() {
        synchronized (getLock()) {
            return name;
        }
    }

    /**
     * Устанавливает имя шрифта.
     * 
     * @param s Новое имя.
     */
    public void setName(String s) {
        synchronized (getLock()) {
            String old = name;
            name = s;
            firePropertyChange(PROPERTY_NAME, old, name);
        }
    }

    /**
     * Возвращает название прототипа шрифта.
     * 
     * @return Название прототипа, может быть {@code null}.
     */
    public String getPrototype() {
        synchronized (getLock()) {
            return prototype;
        }
    }

    /**
     * Устанавливает название прототипа шрифта.
     * 
     * @param s Название прототипа.
     */
    public void setPrototype(String s) {
        synchronized (getLock()) {
            String old = this.prototype;
            prototype = s;
            firePropertyChange(PROPERTY_PROTOTYPE, old, prototype);
        }
    }

    /**
     * Возвращает описание шрифта.
     * 
     * @return Описание шрифта, может быть {@code null}.
     */
    public String getDescriptin() {
        synchronized (getLock()) {
            return description;
        }
    }

    /**
     * Устанавливает описание шрифта.
     * 
     * @param s Описание шрифта.
     */
    public void setDescriptin(String s) {
        synchronized (getLock()) {
            String old = description;
            description = s;
            firePropertyChange(PROPERTY_DESCRIPTION, old, description);
        }
    }

    @Override
    public void setWidth(int w) {
        synchronized (getLock()) {
            super.setWidth(w);

            setMetric(METRIC_LEFT, getMetric(METRIC_LEFT));
            setMetric(METRIC_RIGHT, getMetric(METRIC_RIGHT));
        }
    }

    @Override
    public void setHeight(int h) {
        synchronized (getLock()) {
            super.setHeight(h);

            setMetric(METRIC_BASELINE, getMetric(METRIC_BASELINE));
        }
    }

    @Override
    public boolean isMetricActually(int index) {
        if (index < 0 || index > METRIC_MAX)
            throw new IllegalArgumentException("index=" + index);
        synchronized (getLock()) {
            return actually[index];
        }
    }

    @Override
    public void setMetricActually(int index, boolean state) {
        if (index < 0 || index > METRIC_MAX)
            throw new IllegalArgumentException("index=" + index);
        synchronized (getLock()) {
            boolean old = actually[index];
            actually[index] = state;
            fireActuallyChange(index, old, state);
        }
    }

    @Override
    public int getMetric(int index) {
        if (index < 0 || index > METRIC_MAX)
            throw new IllegalArgumentException("index=" + index);
        synchronized (getLock()) {
            return metrics[index];
        }
    }

    @Override
    public void setMetric(int index, int value) {
        if (index < 0 || index > METRIC_MAX)
            throw new IllegalArgumentException("index=" + index);

        synchronized (getLock()) {
            int old = metrics[index];

            switch (index) {
            case METRIC_BASELINE:
                metrics[index] = checkBaseline(value);
                setMetric(METRIC_ASCENT, metrics[METRIC_ASCENT]);
                setMetric(METRIC_DESCENT, metrics[METRIC_DESCENT]);
                break;
            case METRIC_LINE:
                metrics[index] = checkLine(value);
                break;
            case METRIC_ASCENT:
                metrics[index] = checkAscent(value);
                setMetric(METRIC_LINE, metrics[METRIC_LINE]);
                break;
            case METRIC_DESCENT:
                metrics[index] = checkDescent(value);
                break;
            case METRIC_LEFT:
            case METRIC_RIGHT:
                metrics[index] = checkMargin(value);
                break;
            }
            fireMetricChange(index, old, value);
        }
    }

    /**
     * Проверяет {@code value} на допустимость значения для левого или правого
     * поля символов. Поле не может быть больше 30% от ширины самого узкого
     * символа и не может быть меньше нуля.
     * 
     * @param value Проверяемое значение.
     * @return Откорректированное значение {@code value}, допустимое для шрифта.
     */
    public int checkMargin(int value) {
        synchronized (getLock()) {
            int bound = (int) (getMinWidth() * 0.3);
            if (value < 0) return 0;
            else if (value > bound) return bound;
            else return value;
        }
    }

    /**
     * Проверяет {@code value} на допустимость значения для базовой линии
     * символов. Базовая линия не может быть больше 90% и не может быть меньше
     * 60% от высоты символов.
     * 
     * @param value Проверяемое значение.
     * @return Откорректированное значение {@code value}, допустимое для шрифта.
     */
    public int checkBaseline(int value) {
        synchronized (getLock()) {
            int bound = (int) (height * 0.60);
            if (value < bound) return bound;
            bound = (int) (height * 0.90);
            if (value > bound) return bound;
            return value;
        }
    }

    /**
     * Проверяет {@code value} на допустимость значения высоты строчных
     * символов. Это значение не может быть больше базовой линии символов и
     * меньше 50% базовой линии.
     * 
     * @param value Проверяемое значение.
     * @return Откорректированное значение {@code value}, допустимое для шрифта.
     */
    public int checkAscent(int value) {
        synchronized (getLock()) {
            int baseline = getMetric(METRIC_BASELINE);
            int bound = baseline / 2;
            if (value < bound) return bound;
            else if (value > baseline) return baseline;
            else return value;
        }
    }

    /**
     * Проверяет {@code value} на допустимость высоты прописных символов. Это
     * значение не может быть больше высоты строчных символов и меньше 50%
     * высоты строчных символов.
     * 
     * @param value Проверяемое значение.
     * @return Откорректированное значение {@code value}, допустимое для шрифта.
     */
    public int checkLine(int value) {
        synchronized (getLock()) {
            int ascent = getMetric(METRIC_ASCENT);
            int bound = ascent / 2;
            if (value < bound) return bound;
            else if (value > ascent) return ascent;
            else return value;
        }
    }

    /**
     * Проверяет {@code value} на допустимость значения подстрочной части
     * символов. Поле не может быть больше разницы между высотой символов и
     * базовой линией и не может быть меньше нуля.
     * 
     * @param value Проверяемое значение.
     * @return Откорректированное значение {@code value}, допустимое для шрифта.
     */
    public int checkDescent(int value) {
        synchronized (getLock()) {
            int bound = height - getMetric(METRIC_BASELINE);
            if (value < 0) return 0;
            else if (value > bound) return bound;
            else return value;
        }
    }

    @Override
    public Object getProperty(String property) {
        if (property.equals(PROPERTY_ASCENT)) return getMetric(METRIC_ASCENT);
        else if (property.equals(PROPERTY_BASELINE)) return getMetric(METRIC_BASELINE);
        else if (property.equals(PROPERTY_DESCENT)) return getMetric(METRIC_DESCENT);
        else if (property.equals(PROPERTY_DESCRIPTION)) return getDescriptin();
        else if (property.equals(PROPERTY_LINE)) return getMetric(METRIC_LINE);
        else if (property.equals(PROPERTY_MARGIN_LEFT)) return getMetric(METRIC_LEFT);
        else if (property.equals(PROPERTY_MARGIN_RIGHT)) return getMetric(METRIC_RIGHT);
        else if (property.equals(PROPERTY_NAME)) return getName();
        else if (property.equals(PROPERTY_PROTOTYPE)) return getPrototype();
        else if (property.equals(PROPERTY_WIDTH)) return getWidth();
        return super.getProperty(property);
    }

    @Override
    public void setProperty(String property, Object value) {
        if (value instanceof Integer) {
            int v = ((Integer) value).intValue();

            if (property.equals(PROPERTY_ASCENT)) {
                setMetric(METRIC_ASCENT, v);
                return;
            } else if (property.equals(PROPERTY_BASELINE)) {
                setMetric(METRIC_BASELINE, v);
                return;
            } else if (property.equals(PROPERTY_DESCENT)) {
                setMetric(METRIC_DESCENT, v);
                return;
            } else if (property.equals(PROPERTY_LINE)) {
                setMetric(METRIC_LINE, v);
                return;
            } else if (property.equals(PROPERTY_MARGIN_LEFT)) {
                setMetric(METRIC_LEFT, v);
                return;
            } else if (property.equals(PROPERTY_MARGIN_RIGHT)) {
                setMetric(METRIC_RIGHT, v);
                return;
            }
        } else if (value instanceof String) {
            String s = (String) value;

            if (property.equals(PROPERTY_DESCRIPTION)) {
                setDescriptin(s);
                return;
            } else if (property.equals(PROPERTY_NAME)) {
                setName(s);
                return;
            } else if (property.equals(PROPERTY_PROTOTYPE)) {
                setPrototype(s);
                return;
            }
        }

        super.setProperty(property, value);
    }

    /**
     * Возвращает количество пустых колонок слева.
     * 
     * @see #emptyTop()
     * @see #emptyBottom()
     * @see #emptyRight()
     */
    public int emptyLeft() {
        synchronized (getLock()) {
            int ret = getWidth();

            for (int i = 0; i < length(); i++) {
                int t = symbolByIndex(i).emptyLeft();
                ret = ret < t ? ret : t;
            }
            return ret;
        }
    }

    /**
     * Возвращает количество пустых колонок справа.
     * 
     * @see #emptyTop()
     * @see #emptyBottom()
     * @see #emptyLeft()
     */
    public int emptyRight() {
        synchronized (getLock()) {
            int ret = getWidth();

            for (int i = 0; i < length(); i++) {
                int t = symbolByIndex(i).emptyRight();
                ret = ret < t ? ret : t;
            }
            return ret;
        }
    }

    /**
     * Возвращает количество пустых строк сверху.
     * 
     * @see #emptyBottom()
     * @see #emptyLeft()
     * @see #emptyRight()
     */
    public int emptyTop() {
        synchronized (getLock()) {
            int ret = getHeight();

            for (int i = 0; i < length(); i++) {
                int t = symbolByIndex(i).emptyTop();
                ret = ret < t ? ret : t;
            }
            return ret;
        }
    }

    /**
     * Возвращает количество пустых строк снизу.
     * 
     * @see #emptyTop()
     * @see #emptyLeft()
     * @see #emptyRight()
     */
    public int emptyBottom() {
        synchronized (getLock()) {
            int ret = getHeight();

            for (int i = 0; i < length(); i++) {
                int t = symbolByIndex(i).emptyBottom();
                ret = ret < t ? ret : t;
            }
            return ret;
        }
    }

    /**
     * Удаляет столбец слева.
     * 
     * @param num Количество удаляемых столбцов.
     * @see #removeRight(int)
     */
    public void removeLeft(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            int w = getMinWidth();
            if (num > w) num = w;
            prepareWidth(w - num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).removeLeft(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail remove left", e);
                }
            }
            applyWidth();
        }
    }

    /**
     * Удаляет столбец справа.
     * 
     * @param num Количество удаляемых столбцов.
     * @see #removeLeft(int)
     */
    public void removeRight(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            int w = getMinWidth();
            if (num > w) num = w;
            prepareWidth(w - num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).removeRight(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail remove right", e);
                }
            }
            applyWidth();
        }
    }

    /**
     * Удаляет строки сверху.
     * 
     * @param num Количество удаляемых строк.
     * @see #removeBottom(int)
     */
    public void removeTop(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            int h = getHeight();
            if (num > h) num = h;
            prepareHeight(h - num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).removeTop(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail remove top", e);
                }
            }
            applyHeight();
        }
    }

    /**
     * Удаляет строки снизу.
     * 
     * @param num Количество удаляемых строк.
     * @see #removeTop(int)
     */
    public void removeBottom(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            int h = getHeight();
            if (num > h) num = h;
            prepareHeight(h - num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).removeBottom(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail remove bottom", e);
                }
            }
            applyHeight();
        }
    }

    /**
     * Вставляет столбец слева.
     * 
     * @param num Количество вставляемых столбцов.
     * @see #addRight(int)
     */
    public void addLeft(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            prepareWidth(getWidth() + num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).addLeft(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail add left", e);
                }
            }
            applyWidth();
        }
    }

    /**
     * Вставляет столбец справа.
     * 
     * @param num Количество вставляемых столбцов.
     * @see #addLeft(int)
     */
    public void addRight(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            prepareWidth(getWidth() + num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).addRight(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail add right", e);
                }
            }
            applyWidth();
        }
    }

    /**
     * Вставляет строки сверху.
     * 
     * @param num Количество вставляемых строк.
     * @see #addBottom(int)
     */
    public void addTop(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            prepareHeight(getHeight() + num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).addTop(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail add top", e);
                }
            }
            applyHeight();
        }
    }

    /**
     * Вставляет строки снизу.
     * 
     * @param num Количество вставляемых строк.
     * @see #addTop(int)
     */
    public void addBottom(int num) {
        if (num <= 0) return;

        synchronized (getLock()) {
            prepareHeight(getHeight() + num);

            for (int i = 0; i < length(); i++) {
                try {
                    symbolByIndex(i).addBottom(num);
                } catch (DisallowOperationException e) {
                    // Это исключение не должно возникнуть никогда.
                    logger().log(Level.SEVERE, "fail add bottom", e);
                }
            }
            applyHeight();
        }
    }

    /**
     * Выпускает сообщение об изменении актуальности метрик шрифта.
     * 
     * @param index Индекс метрики. Может быть одним из следующих значений:<br>
     *            {@link Metrics#METRIC_ASCENT} <br>
     *            {@link Metrics#METRIC_BASELINE} <br>
     *            {@link Metrics#METRIC_DESCENT} <br>
     *            {@link Metrics#METRIC_LEFT} <br>
     *            {@link Metrics#METRIC_LINE} <br>
     *            {@link Metrics#METRIC_RIGHT}
     * @param oldValue Старое значение актуальности.
     * @param newValue Новое значение актуальности.
     */
    protected void fireActuallyChange(int index, boolean oldValue,
                    boolean newValue) {
        String property;

        switch (index) {
        case METRIC_BASELINE:
            property = PROPERTY_ACTUALLY_BASELINE;
            break;
        case METRIC_LINE:
            property = PROPERTY_ACTUALLY_LINE;
            break;
        case METRIC_ASCENT:
            property = PROPERTY_ACTUALLY_ASCENT;
            break;
        case METRIC_DESCENT:
            property = PROPERTY_ACTUALLY_DESCENT;
            break;
        case METRIC_LEFT:
            property = PROPERTY_ACTUALLY_MARGIN_LEFT;
            break;
        case METRIC_RIGHT:
            property = PROPERTY_ACTUALLY_MARGIN_RIGHT;
            break;
        default:
            return;
        }

        firePropertyChange(property, oldValue, newValue);
    }

    /**
     * Выпускает сообщение о изменении метрик шрифта.
     * 
     * @param index Индекс метрики. Может быть одним из следующих значений:<br>
     *            {@link Metrics#METRIC_ASCENT} <br>
     *            {@link Metrics#METRIC_BASELINE} <br>
     *            {@link Metrics#METRIC_DESCENT} <br>
     *            {@link Metrics#METRIC_LEFT} <br>
     *            {@link Metrics#METRIC_LINE} <br>
     *            {@link Metrics#METRIC_RIGHT}
     * @param oldValue Старое значение метрики.
     * @param newValue Новое значение метрики.
     */
    protected void fireMetricChange(int index, int oldValue, int newValue) {
        String property;

        switch (index) {
        case METRIC_BASELINE:
            property = PROPERTY_BASELINE;
            break;
        case METRIC_LINE:
            property = PROPERTY_LINE;
            break;
        case METRIC_ASCENT:
            property = PROPERTY_ASCENT;
            break;
        case METRIC_DESCENT:
            property = PROPERTY_DESCENT;
            break;
        case METRIC_LEFT:
            property = PROPERTY_MARGIN_LEFT;
            break;
        case METRIC_RIGHT:
            property = PROPERTY_MARGIN_RIGHT;
            break;
        default:
            return;
        }

        firePropertyChange(property, oldValue, newValue);
    }
}
