
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
    public static final String FONT_ASCENT         = "mf.ascent";
    public static final String FONT_LINE           = "mf.line";
    public static final String FONT_AUTHOR         = "mf.author";
    public static final String FONT_BASELINE       = "mf.baseline";
    public static final String FONT_CODE_PAGE      = "mf.CodePage";
    public static final String FONT_DESCENT        = "mf.descent";
    public static final String FONT_FIXSED         = "mf.fixsed";
    public static final String FONT_HEIGHT         = "mf.height";
    public static final String FONT_MARGIN_LEFT    = "mf.magrin.left";
    public static final String FONT_MARGIN_RIGHT   = "mf.margin.right";
    public static final String FONT_NAME           = "mf.name";
    public static final String FONT_PROTOTYPE      = "mf.prototype";
    public static final String FONT_REMOVE_ALL     = "mf.remove.all";
    public static final String FONT_SIZE           = "mf.size";
    public static final String FONT_SYMBOL_ADDED   = "mf.symbol.added";
    public static final String FONT_SYMBOL_CHANGED = "mf.symbol.changed";
    public static final String FONT_SYMBOL_REMOVE  = "mf.symbol.removed";
    public static final String FONT_SYMBOL_REPLACE = "mf.symbol.replaced";
    public static final String FONT_WIDTH          = "mf.width";
    public static final String FONT_WIDTH_MAX      = "mf.width.max";
    public static final String FONT_WIDTH_MIN      = "mf.width.min";
    public static final String FONT_DESCRIPTION    = "mf.description";

    private MSymbol[]          symbols             = new MSymbol[0];
    private int                size                = 0;
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
    private ListenerChain      listeners           = new ListenerChain();
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
        String reason;

        if (oldValue == null) {
            if (newValue == null) return;
            reason = FONT_SYMBOL_ADDED;
        } else {
            if (newValue == null) reason = FONT_SYMBOL_REMOVE;
            else if (oldValue.equals(newValue)) return;
            else reason = FONT_SYMBOL_REPLACE;
        }

        firePropertyChange(new PropertyChangeEvent(this, reason, oldValue,
                        newValue));
    }

    protected void firePropertyChange(String message, String oldValue,
                    String newValue) {
        if (oldValue == null) {
            if (newValue == null) return;
        } else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        firePropertyChange(new PropertyChangeEvent(this, message, oldValue,
                        newValue));
    }

    protected void firePropertyChange(String message, int oldValue, int newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, message, new Integer(
                        oldValue), new Integer(newValue)));
    }

    protected void firePropertyChange(String message, boolean oldValue,
                    boolean newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, message, new Boolean(
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
        boolean oldFix;
        int oldWidth, oldHeight;
        if (font == null) return;

        /* Уловка для замены символов. */
        oldFix = fixsed;
        oldWidth = width;
        oldHeight = height;
        fixsed = font.fixsed;
        width = font.width;
        height = font.height;
        validWidth = font.validWidth;
        validHeight = font.validHeight;

        setName(font.name);
        setPrototype(font.prototype);
        setCodePage(font.codePage);
        setAuthor(font.author);
        setMarginLeft(font.marginLeft);
        setMarginRight(font.marginRight);
        setBaseline(font.baseline);
        setAscent(font.ascent);
        setLine(font.line);
        setDescent(font.descent);

        setSymbols(font.getSymbols());

        width = oldWidth;
        height = oldHeight;
        setWidth(font.width);
        setHeight(font.height);
        fixsed = oldFix;
        setFixsed(font.fixsed);
    }

    String convertName(String name) {
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
        firePropertyChange(FONT_NAME, old, name);
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String s) {
        String old = this.prototype;
        prototype = convertName(s);
        firePropertyChange(FONT_PROTOTYPE, old, prototype);
    }

    public String getDescriptin() {
        return this.description;
    }

    public void setDescriptin(String s) {
        String old = description;
        description = s;
        firePropertyChange(FONT_DESCRIPTION, old, description);
    }

    public boolean isFixsed() {
        return this.fixsed;
    }

    public void setFixsed(boolean fixsed) {
        boolean old = this.fixsed;
        int max = getMaxWidth();
        this.fixsed = fixsed;

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

        firePropertyChange(FONT_FIXSED, old, this.fixsed);
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String cp) {
        String old = codePage;
        codePage = cp;

        firePropertyChange(FONT_CODE_PAGE, old, codePage);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String s) {
        String old = author;
        author = s;

        firePropertyChange(FONT_AUTHOR, old, author);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int w) {
        int oldWidth;

        if (!fixsed) return;

        oldWidth = width;
        validWidth = w;

        for (MSymbol sym : symbols) {
            try {
                if (document != null) document.nestedEdit(sym);
                sym.setWidth(w);
            } catch (DisallowOperationException e) {
                // XXX print
                System.out.println("bad width");
            }
        }

        width = w;

        firePropertyChange(FONT_WIDTH, oldWidth, width);
        setMarginLeft(marginLeft);
        setMarginRight(marginRight);
    }

    public int getMinWidth() {
        int ret = 0;

        for (MSymbol sym : symbols) {
            ret = ret < sym.getWidth() ? ret : sym.getWidth();
        }

        return ret;
    }

    public int getMaxWidth() {
        int ret = 0;

        for (MSymbol sym : symbols) {
            ret = ret < sym.getWidth() ? sym.getWidth() : ret;
        }

        return ret;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int h) {
        int old = this.height;

        if (h < 0) throw (new IllegalArgumentException("invalid height"));

        validHeight = h;

        if (old != h) {
            for (MSymbol sym : symbols) {
                try {
                    if (document != null) document.nestedEdit(sym);
                    sym.setHeight(this.height);
                } catch (DisallowOperationException e) {
                    // XXX print
                    System.out.println("bad height");
                }
            }
        }

        this.height = h;

        firePropertyChange(FONT_HEIGHT, old, height);

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

        // if (margin < 0) throw (new
        // IllegalArgumentException("invalid margin"));
        marginLeft = checkMarginLeft(margin);

        firePropertyChange(FONT_MARGIN_LEFT, old, marginLeft);
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public int checkMarginRight(int value) {
        int bound = (getMinWidth() * 3 + 5) / 10;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setMarginRight(int margin) {
        int old = this.marginRight;

        // if (margin < 0) throw (new
        // IllegalArgumentException("invalid margin"));
        this.marginRight = checkMarginRight(margin);

        firePropertyChange(FONT_MARGIN_RIGHT, old, this.marginRight);
    }

    public int getBaseline() {
        return this.baseline;
    }

    public int checkBaseline(int value) {
        int bound = (height * 6 + 6) / 10;
        if (value < bound) return bound;
        else if (value > height) return height;
        else return value;
    }

    public void setBaseline(int baseline) {
        int old = this.baseline;

        if (baseline < 0)
            throw (new IllegalArgumentException("invalid baseline"));
        this.baseline = checkBaseline(baseline);

        firePropertyChange(FONT_BASELINE, old, this.baseline);

        setAscent(ascent);
        setDescent(descent);
    }

    public int getAscent() {
        return this.ascent;
    }

    public int checkAscent(int value) {
        int bound = baseline / 2;
        if (value < bound) return bound;
        else if (value > baseline) return baseline;
        else return value;
    }

    public void setAscent(int ascent) {
        int old = this.ascent;

        if (ascent < 0) throw (new IllegalArgumentException("invalid ascent"));
        this.ascent = checkAscent(ascent);

        firePropertyChange(FONT_ASCENT, old, this.ascent);

        setLine(line);
    }

    public int getLine() {
        return this.line;
    }

    public int checkAscentCapital(int value) {
        int bound = ascent / 2;
        if (value < bound) return bound;
        else if (value > ascent) return ascent;
        else return value;
    }

    public void setLine(int ascentCapital) {
        int old = this.line;

        if (ascentCapital < 0)
            throw (new IllegalArgumentException("invalid line"));
        this.line = checkAscentCapital(ascentCapital);

        firePropertyChange(FONT_LINE, old, this.line);
    }

    public int getDescent() {
        return this.descent;
    }

    public int checkDescent(int value) {
        int bound = height - baseline;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setDescent(int descent) {
        int old = this.descent;

        if (descent < 0)
            throw (new IllegalArgumentException("invalid descent"));
        this.descent = checkDescent(descent);

        firePropertyChange(FONT_DESCENT, old, this.descent);
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
        return size <= 0;
    }

    /**
     * Return number of symbols, that contain font.
     * 
     * @see #isEmpty()
     */
    public int getSize() {
        return size;
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

    private void add(MSymbol symbol, boolean fire) {
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
            size++;
        } else {
            old.removeNotifyEventListener(this);
            old.removePixselMapListener(this);
            old.owner = null;
            symbols[i] = symbol;
        }

        if (fire) {
            firePropertyChange(old, symbol);
            if (old == null) firePropertyChange(FONT_SIZE, size - 1, size);
        }
    }

    private void remove(MSymbol symbol, boolean fire) {
        int i;

        if (symbol == null) return;
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

        size--;

        if (fire) {
            firePropertyChange(symbol, null);
            firePropertyChange(FONT_SIZE, size + 1, size);
        }
    }

    private void removeAll(boolean fire) {
        int oldSize;

        oldSize = size;

        for (MSymbol sym : symbols) {
            sym.removeNotifyEventListener(this);
            sym.removePixselMapListener(this);
            sym.owner = null;
        }

        symbols = new MSymbol[0];
        size = 0;

        if (fire) {
            firePropertyChange(FONT_SIZE, oldSize, size);
        }
    }

    public void add(MSymbol symbol) {
        add(symbol, true);
    }

    public void remove(MSymbol symbol) {
        remove(symbol, true);
    }

    public void removeAtIndex(int index) {
        remove(symbolByCode(index));
    }

    public void removeAll() {
        removeAll(true);
    }

    public MSymbol[] getSymbols() {
        MSymbol[] ret = new MSymbol[symbols.length];

        System.arraycopy(symbols, 0, ret, 0, symbols.length);
        return ret;
    }

    public void setSymbols(MSymbol[] symbols) {
        removeAll(false);
        for (MSymbol sym : symbols) {
            add(sym, false);
        }
        firePropertyChange(FONT_SIZE);
    }
}
