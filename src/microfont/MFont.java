
package microfont;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;
import utils.event.ListenerChain;

/**
 * 
 * @author Николай
 * 
 */
public class MFont extends Object implements PixselMapListener,
                PropertyChangeListener {
    public static final String PROPERTY_ASCENT       = "mf.ascent";
    public static final String PROPERTY_AUTHOR       = "mf.author";
    public static final String PROPERTY_BASELINE     = "mf.baseline";
    public static final String PROPERTY_CODE_PAGE    = "mf.CodePage";
    public static final String PROPERTY_DESCENT      = "mf.descent";
    public static final String PROPERTY_DESCRIPTION  = "mf.description";
    public static final String PROPERTY_FIXSED       = "mf.fixsed";
    public static final String PROPERTY_HEIGHT       = "mf.height";
    public static final String PROPERTY_LINE         = "mf.line";
    public static final String PROPERTY_MARGIN_LEFT  = "mf.magrin.left";
    public static final String PROPERTY_MARGIN_RIGHT = "mf.margin.right";
    public static final String PROPERTY_NAME         = "mf.name";
    public static final String PROPERTY_PROTOTYPE    = "mf.prototype";
    public static final String PROPERTY_SYMBOLS      = "mf.symbols";
    public static final String PROPERTY_WIDTH        = "mf.width";

    private MSymbol[]          symbols               = new MSymbol[0];
    private String             name;
    private String             prototype;
    private String             description;
    private boolean            fixsed;
    private String             codePage;
    private String             author;
    private int                width;
    private int                validWidth;
    private int                height;
    private int                validHeight;
    private int                marginLeft;
    private int                marginRight;
    private int                baseline;
    private int                ascent;
    private int                line;
    private int                descent;
    private ListenerChain      listeners             = new ListenerChain();
    /**
     * Документ, которому принадлежит шрифт. Эта переменная никак не изменяется
     * в классе.
     * 
     * @see Document
     */
    Document                   document;

    public MFont() {
        codePage = null;
        width = 0;
        height = 0;
        validWidth = 0;
        validHeight = 0;
    }

    public MFont(MFont src) {
        synchronized (src) {
            name = src.name;
            prototype = src.prototype;
            fixsed = src.fixsed;
            codePage = src.codePage;
            author = src.author;
            width = src.width;
            height = src.height;
            validWidth = src.validWidth;
            validHeight = src.validHeight;
            setSymbols(src.getSymbols());
            marginLeft = src.marginLeft;
            marginRight = src.marginRight;
            baseline = src.baseline;
            ascent = src.ascent;
            line = src.line;
            descent = src.descent;
        }
    }

    /**
     * 
     * @param width
     * @return
     */
    protected boolean isValidWidth(int width) {
        if (!fixsed) return true;
        return width == validWidth;
    }

    /**
     * 
     * @param height
     * @return
     */
    protected boolean isValidHeight(int height) {
        return height == validHeight;
    }

    /**
     * Добавление получателя события изменения свойств символов.
     * 
     * @param listener Добавляемый получатель события.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(PropertyChangeListener.class, listener);
    }

    /**
     * Удаление получателя события изменения свойств символов.
     * 
     * @param listener Удаляемый получатель события.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(PropertyChangeListener.class, listener);
    }

    /**
     * Генерация события изменения свойств символов. Получатели добавляются
     * функцией {@link #addPropertyChangeListener(PropertyChangeListener)}.
     */
    protected void firePropertyChange(PropertyChangeEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PropertyChangeListener.class)
                ((PropertyChangeListener) listenerArray[i + 1])
                                .propertyChange(event);
        }
    }

    protected void firePropertyChange(String message) {
        firePropertyChange(new PropertyChangeEvent(this, message, null, null));
    }

    protected void firePropertyChange(MSymbol oldValue, MSymbol newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, PROPERTY_SYMBOLS,
                        oldValue, newValue));
    }

    protected void firePropertyChange(String property, String oldValue,
                    String newValue) {
        if (oldValue == null) {
            if (newValue == null) return;
        } else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        firePropertyChange(new PropertyChangeEvent(this, property, oldValue,
                        newValue));
    }

    protected void firePropertyChange(String property, int oldValue,
                    int newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, new Integer(
                        oldValue), new Integer(newValue)));
    }

    protected void firePropertyChange(String property, boolean oldValue,
                    boolean newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, new Boolean(
                        oldValue), new Boolean(newValue)));
    }

    /**
     * Добавление получателя события при измении пикселей одного из символов
     * шрифта.
     * 
     * @param listener Добавляемый получатель события.
     */
    public void addPixselMapListener(PixselMapListener listener) {
        listeners.add(PixselMapListener.class, listener);
    }

    /**
     * Удаление получателя события при измении пикселей одного из символов
     * шрифта.
     * 
     * @param listener Удаляемый получатель события.
     */
    public void removePixselMapListener(PixselMapListener listener) {
        listeners.remove(PixselMapListener.class, listener);
    }

    /**
     * Генерация события при измении пикселей одного из символов шрифта.
     * Получатели добавляются функцией
     * {@link #addPixselMapListener(PixselMapListener)}.
     */
    protected void firePixselEvent(PixselMapEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PixselMapListener.class)
                ((PixselMapListener) listenerArray[i + 1]).pixselChanged(event);
        }
    }

    /**
     * Получение события при изменении одного из символов. Это событие
     * транслируется получателям шрифта.
     */
    @Override
    public void pixselChanged(PixselMapEvent change) {
        firePixselEvent(change);
    }

    /**
     * Получение уведомляющего события от одного из символов. Это событие
     * транслируется получателям шрифта.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        firePropertyChange(event);
    }

    public void copy(MFont font) {
        removeAll();
        setWidth(font.width);
        setHeight(font.height);
        setFixsed(font.fixsed);
        setSymbols(font.getSymbols());

        setMarginLeft(font.marginLeft);
        setMarginRight(font.marginRight);
        setBaseline(font.baseline);
        setAscent(font.ascent);
        setLine(font.line);
        setDescent(font.descent);

        setName(font.name);
        setPrototype(font.prototype);
        setCodePage(font.codePage);
        setAuthor(font.author);
    }

    private String convertName(String name) {
        String ret;
        int i, j;

        if (name == null) return null;

        i = name.indexOf('\n');
        j = name.indexOf('\r');

        if (j > 0 && j < i) i = j;
        if (i > 0) ret = name.substring(0, i);
        else ret = name;

        ret = new String(ret.trim());
        if (ret.length() == 0) ret = null;
        return ret;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        String old = name;
        name = convertName(s);
        firePropertyChange(PROPERTY_NAME, old, name);
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String s) {
        String old = this.prototype;
        prototype = convertName(s);
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

    public boolean isFixsed() {
        return this.fixsed;
    }

    public void setFixsed(boolean fixsed) {
        boolean old = this.fixsed;
        int max = getMaxWidth();
        this.fixsed = fixsed;

        firePropertyChange(PROPERTY_FIXSED, old, this.fixsed);

        if (!old && this.fixsed) {
            validWidth = max;
            for (MSymbol sym : symbols) {
                try {
                    if (document != null) document.nestedEdit(sym);
                    sym.setWidth(max);
                } catch (DisallowOperationException e) {
                }
            }
        }
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String cp) {
        String old = codePage;
        codePage = cp;

        firePropertyChange(PROPERTY_CODE_PAGE, old, codePage);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String s) {
        String old = author;
        author = s;

        firePropertyChange(PROPERTY_AUTHOR, old, author);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int w) {
        int oldWidth;

        oldWidth = width;
        validWidth = w;
        width = w;

        firePropertyChange(PROPERTY_WIDTH, oldWidth, width);

        if (isFixsed()) {
            for (MSymbol sym : symbols) {
                try {
                    if (document != null) document.nestedEdit(sym);
                    sym.setWidth(width);
                } catch (DisallowOperationException e) {
                    // XXX print
                    System.out.println("bad width");
                }
            }
        }

        setMarginLeft(marginLeft);
        setMarginRight(marginRight);
    }

    public int getMinWidth() {
        if (symbols.length == 0) return getWidth();
        
        int ret = Integer.MAX_VALUE;
        int w;

        for (MSymbol sym : symbols) {
            w = sym.getWidth();
            ret = ret < w ? ret : w;
        }

        return ret;
    }

    public int getMaxWidth() {
        if (symbols.length == 0) return  getWidth();

        int ret = 0;
        int w ;

        for (MSymbol sym : symbols) {
            w = sym.getWidth();
            ret = ret < w ? w : ret;
        }

        return ret;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int h) {
        int old = height;

        if (h < 0) throw (new IllegalArgumentException("invalid height"));

        validHeight = h;
        height = h;

        firePropertyChange(PROPERTY_HEIGHT, old, height);

        if (old != height) {
            for (MSymbol sym : symbols) {
                try {
                    if (document != null) document.nestedEdit(sym);
                    sym.setHeight(height);
                } catch (DisallowOperationException e) {
                    // XXX print
                    System.out.println("bad height");
                }
            }
        }

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

        if (bl < 0) throw (new IllegalArgumentException("invalid baseline"));
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

        if (asc < 0) throw (new IllegalArgumentException("invalid ascent"));
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

        if (ln < 0) throw (new IllegalArgumentException("invalid line"));
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

        if (dsc < 0) throw (new IllegalArgumentException("invalid descent"));
        descent = checkDescent(dsc);

        firePropertyChange(PROPERTY_DESCENT, old, descent);
    }

    /**
     * Return <code>true</code> if <code>ref</code> belong to font.
     */
    public boolean isBelong(MSymbol ref) {
        if (ref == null) return false;
        return ref.owner == this;
    }

    /**
     * Return <code>true</code> if font has not symbols.
     * 
     * @see #getSize()
     */
    public boolean isEmpty() {
        return symbols.length <= 0;
    }

    /**
     * Return number of symbols, that contain font.
     * 
     * @see #isEmpty()
     */
    public int getSize() {
        return symbols.length;
    }

    public MSymbol symbolAtNumber(int index) {
        if (index >= symbols.length) return null;

        return symbols[index];
    }

    public MSymbol symbolByCode(int code) {
        for (MSymbol sym : symbols) {
            if (sym.getCode() == code) return sym;
        }
        return null;
    }

    public void add(MSymbol symbol) {
        MSymbol old = null;
        int i;

        if (symbol == null) return;
        if (isBelong(symbol)) return;

        symbol.removeNotifyEventListener(symbol.owner);
        symbol.removePixselMapListener(symbol.owner);
        symbol.owner = this;
        symbol.addPropertyChangeListener(this);
        symbol.addPixselMapListener(this);

        try {
            if (document != null) document.nestedEdit(symbol);
            symbol.setHeight(height);
        } catch (DisallowOperationException e) {
        }
        if (fixsed) try {
            if (document != null) document.nestedEdit(symbol);
            symbol.setWidth(width);
        } catch (DisallowOperationException e) {
        }

        i = 0;
        for (MSymbol sym : symbols) {
            if (sym.getCode() == symbol.getCode()) {
                old = sym;
                break;
            }
            if (sym.getCode() > symbol.getCode()) break;
            i++;
        }

        if (old == null) {
            MSymbol[] t = new MSymbol[symbols.length + 1];
            System.arraycopy(symbols, 0, t, 0, i);
            System.arraycopy(symbols, i, t, i + 1, symbols.length - i);
            t[i] = symbol;
            symbols = t;
        } else {
            old.removeNotifyEventListener(this);
            old.removePixselMapListener(this);
            old.owner = null;
            symbols[i] = symbol;
        }

        firePropertyChange(old, symbol);
    }

    public void remove(MSymbol symbol) {
        int i;

        if (!isBelong(symbol)) return;

        i = 0;
        for (MSymbol sym : symbols) {
            if (sym.equals(symbol)) {
                sym.removeNotifyEventListener(this);
                sym.removePixselMapListener(this);
                sym.owner = null;
                break;
            }
            i++;
        }

        MSymbol[] t = new MSymbol[symbols.length - 1];
        System.arraycopy(symbols, 0, t, 0, i);
        System.arraycopy(symbols, i + 1, t, i, symbols.length - i - 1);

        firePropertyChange(symbol, null);
    }

    public void removeAtIndex(int index) {
        remove(symbolByCode(index));
    }

    public void removeAll() {
        for (MSymbol sym : symbols) {
            remove(sym);
        }
    }

    public MSymbol[] getSymbols() {
        MSymbol[] ret = new MSymbol[symbols.length];

        System.arraycopy(symbols, 0, ret, 0, symbols.length);
        return ret;
    }

    public void setSymbols(MSymbol[] ss) {
        removeAll();
        for (MSymbol sym : ss) {
            add(sym);
        }
    }

    public Object getProperty(String property) {
        if (property.equals(PROPERTY_ASCENT)) return new Integer(getAscent());
        else if (property.equals(PROPERTY_AUTHOR)) return getAuthor();
        else if (property.equals(PROPERTY_BASELINE)) return new Integer(
                        getBaseline());
        else if (property.equals(PROPERTY_CODE_PAGE)) return getCodePage();
        else if (property.equals(PROPERTY_DESCENT)) return new Integer(
                        getDescent());
        else if (property.equals(PROPERTY_DESCRIPTION)) return getDescriptin();
        else if (property.equals(PROPERTY_FIXSED)) return new Boolean(
                        isFixsed());
        else if (property.equals(PROPERTY_HEIGHT)) return new Integer(
                        getHeight());
        else if (property.equals(PROPERTY_LINE)) return new Integer(getLine());
        else if (property.equals(PROPERTY_MARGIN_LEFT)) return new Integer(
                        getMarginLeft());
        else if (property.equals(PROPERTY_MARGIN_RIGHT)) return new Integer(
                        getMarginRight());
        else if (property.equals(PROPERTY_NAME)) return getName();
        else if (property.equals(PROPERTY_PROTOTYPE)) return getPrototype();
        else if (property.equals(PROPERTY_WIDTH))
            return new Integer(getWidth());
        return null;
    }

    public void setProperty(String property, Object value) {
        if (value instanceof Integer) {
            int i = ((Integer) value).intValue();

            if (property.equals(PROPERTY_ASCENT)) setAscent(i);
            else if (property.equals(PROPERTY_BASELINE)) setBaseline(i);
            else if (property.equals(PROPERTY_DESCENT)) setDescent(i);
            else if (property.equals(PROPERTY_HEIGHT)) setHeight(i);
            else if (property.equals(PROPERTY_LINE)) setLine(i);
            else if (property.equals(PROPERTY_MARGIN_LEFT)) setMarginLeft(i);
            else if (property.equals(PROPERTY_MARGIN_RIGHT)) setMarginRight(i);
            else if (property.equals(PROPERTY_WIDTH)) setWidth(i);
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();

            if (property.equals(PROPERTY_FIXSED)) setFixsed(b);
        } else if (value instanceof String) {
            String s = (String) value;

            if (property.equals(PROPERTY_AUTHOR)) setAuthor(s);
            else if (property.equals(PROPERTY_CODE_PAGE)) setCodePage(s);
            else if (property.equals(PROPERTY_DESCRIPTION)) setDescriptin(s);
            else if (property.equals(PROPERTY_NAME)) setName(s);
            else if (property.equals(PROPERTY_PROTOTYPE)) setPrototype(s);
        }
    }
}
