
package microfont;

import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import microfont.events.PixselMapListener;

/**
 * 
 */
public class MFont extends AbstractMFont implements PixselMapListener,
                PropertyChangeListener {
    public static final String PROPERTY_ASCENT       = "mf.ascent";
    public static final String PROPERTY_AUTHOR       = "mf.author";
    public static final String PROPERTY_BASELINE     = "mf.baseline";
    public static final String PROPERTY_DESCENT      = "mf.descent";
    public static final String PROPERTY_DESCRIPTION  = "mf.description";
    public static final String PROPERTY_LINE         = "mf.line";
    public static final String PROPERTY_MARGIN_LEFT  = "mf.magrin.left";
    public static final String PROPERTY_MARGIN_RIGHT = "mf.margin.right";
    public static final String PROPERTY_NAME         = "mf.name";
    public static final String PROPERTY_PROTOTYPE    = "mf.prototype";

    private String             name;
    private String             prototype;
    private String             description;
    private String             author;
    private int                marginLeft;
    private int                marginRight;
    private int                baseline;
    private int                ascent;
    private int                line;
    private int                descent;

    public MFont() {
        super();
    }

    public MFont(MFont src) {
        super(src);
        synchronized (src.getLock()) {
            name = src.name;
            prototype = src.prototype;
            author = src.author;
            marginLeft = src.marginLeft;
            marginRight = src.marginRight;
            baseline = src.baseline;
            ascent = src.ascent;
            line = src.line;
            descent = src.descent;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public synchronized int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ascent;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + baseline;
        result = prime * result + descent;
        result = prime * result
                        + ((description == null) ? 0 : description.hashCode());
        result = prime * result + line;
        result = prime * result + marginLeft;
        result = prime * result + marginRight;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                        + ((prototype == null) ? 0 : prototype.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public synchronized boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof MFont))
            return false;
        MFont other = (MFont) obj;
        if (ascent != other.ascent)
            return false;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (baseline != other.baseline)
            return false;
        if (descent != other.descent)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (line != other.line)
            return false;
        if (marginLeft != other.marginLeft)
            return false;
        if (marginRight != other.marginRight)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (prototype == null) {
            if (other.prototype != null)
                return false;
        } else if (!prototype.equals(other.prototype))
            return false;
        return true;
    }

    public void copy(MFont font) {
        super.copy(font);

        setMarginLeft(font.marginLeft);
        setMarginRight(font.marginRight);
        setBaseline(font.baseline);
        setAscent(font.ascent);
        setLine(font.line);
        setDescent(font.descent);

        setName(font.name);
        setPrototype(font.prototype);
        setAuthor(font.author);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        String old = name;
        name = s;
        firePropertyChange(PROPERTY_NAME, old, name);
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String s) {
        String old = this.prototype;
        prototype = s;
        firePropertyChange(PROPERTY_PROTOTYPE, old, prototype);
    }

    public String getDescriptin() {
        return this.description;
    }

    public void setDescriptin(String s) {
        String old = description;
        description = s;
        firePropertyChange(PROPERTY_DESCRIPTION, old, description);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String s) {
        String old = author;
        author = s;

        firePropertyChange(PROPERTY_AUTHOR, old, author);
    }

    @Override
    public void setWidth(int w) {
        super.setWidth(w);

        setMarginLeft(marginLeft);
        setMarginRight(marginRight);
    }

    @Override
    public void setHeight(int h) {
        super.setHeight(h);

        setBaseline(baseline);
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public int checkMarginLeft(int value) {
        int bound = (getMinWidth() * 3 + 5) / 10;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setMarginLeft(int margin) {
        int old = marginLeft;

        marginLeft = checkMarginLeft(margin);

        firePropertyChange(PROPERTY_MARGIN_LEFT, old, marginLeft);
    }

    public int getMarginRight() {
        return marginRight;
    }

    public int checkMarginRight(int value) {
        int bound = (getMinWidth() * 3 + 5) / 10;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setMarginRight(int margin) {
        int old = marginRight;

        marginRight = checkMarginRight(margin);

        firePropertyChange(PROPERTY_MARGIN_RIGHT, old, marginRight);
    }

    public int getBaseline() {
        return baseline;
    }

    public int checkBaseline(int value) {
        int bound = (height * 6 + 6) / 10;
        if (value < bound) return bound;
        else if (value > height) return height;
        else return value;
    }

    public void setBaseline(int bl) {
        int old = baseline;

        if (bl < 0)
            throw (new IllegalArgumentException("invalid baseline"));
        baseline = checkBaseline(bl);

        firePropertyChange(PROPERTY_BASELINE, old, baseline);

        setAscent(ascent);
        setDescent(descent);
    }

    public int getAscent() {
        return ascent;
    }

    public int checkAscent(int value) {
        int bound = baseline / 2;
        if (value < bound) return bound;
        else if (value > baseline) return baseline;
        else return value;
    }

    public void setAscent(int asc) {
        int old = ascent;

        if (asc < 0)
            throw (new IllegalArgumentException("invalid ascent"));
        ascent = checkAscent(asc);

        firePropertyChange(PROPERTY_ASCENT, old, ascent);

        setLine(line);
    }

    public int getLine() {
        return this.line;
    }

    public int checkLine(int value) {
        int bound = ascent / 2;
        if (value < bound) return bound;
        else if (value > ascent) return ascent;
        else return value;
    }

    public void setLine(int ln) {
        int old = line;

        if (ln < 0)
            throw (new IllegalArgumentException("invalid line"));
        line = checkLine(ln);

        firePropertyChange(PROPERTY_LINE, old, line);
    }

    public int getDescent() {
        return descent;
    }

    public int checkDescent(int value) {
        int bound = height - baseline;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setDescent(int dsc) {
        int old = descent;

        if (dsc < 0)
            throw (new IllegalArgumentException("invalid descent"));
        descent = checkDescent(dsc);

        firePropertyChange(PROPERTY_DESCENT, old, descent);
    }

    @Override
    public Object getProperty(String property) {
        if (property.equals(PROPERTY_ASCENT)) return new Integer(getAscent());
        else if (property.equals(PROPERTY_AUTHOR)) return getAuthor();
        else if (property.equals(PROPERTY_BASELINE)) return new Integer(
                        getBaseline());
        else if (property.equals(PROPERTY_DESCENT)) return new Integer(
                        getDescent());
        else if (property.equals(PROPERTY_DESCRIPTION)) return getDescriptin();
        else if (property.equals(PROPERTY_LINE)) return new Integer(getLine());
        else if (property.equals(PROPERTY_MARGIN_LEFT)) return new Integer(
                        getMarginLeft());
        else if (property.equals(PROPERTY_MARGIN_RIGHT)) return new Integer(
                        getMarginRight());
        else if (property.equals(PROPERTY_NAME)) return getName();
        else if (property.equals(PROPERTY_PROTOTYPE)) return getPrototype();
        else if (property.equals(PROPERTY_WIDTH))
            return new Integer(getWidth());
        return super.getProperty(property);
    }

    @Override
    public void setProperty(String property, Object value) {
        if (value instanceof Integer) {
            int i = ((Integer) value).intValue();

            if (property.equals(PROPERTY_ASCENT)) {
                setAscent(i);
                return;
            } else if (property.equals(PROPERTY_BASELINE)) {
                setBaseline(i);
                return;
            } else if (property.equals(PROPERTY_DESCENT)) {
                setDescent(i);
                return;
            } else if (property.equals(PROPERTY_LINE)) {
                setLine(i);
                return;
            } else if (property.equals(PROPERTY_MARGIN_LEFT)) {
                setMarginLeft(i);
                return;
            } else if (property.equals(PROPERTY_MARGIN_RIGHT)) {
                setMarginRight(i);
                return;
            }
        } else if (value instanceof String) {
            String s = (String) value;

            if (property.equals(PROPERTY_AUTHOR)) {
                setAuthor(s);
                return;
            } else if (property.equals(PROPERTY_DESCRIPTION)) {
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
            int w;
            if (isFixsed()) {
                w = getWidth();
            } else {
                w = getMinWidth();
            }
            if (num > w) num = w;
            prepareWidth(getWidth() - num);

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
            int w;
            if (isFixsed()) {
                w = getWidth();
            } else {
                w = getMinWidth();
            }
            if (num > w) num = w;
            prepareWidth(getWidth() - num);

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
}
