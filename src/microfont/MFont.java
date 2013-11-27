
package microfont;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import microfont.events.NotifyEvent;
import microfont.events.NotifyEventListener;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;
import microfont.undo.MFontUndo;
import utils.event.ListenerChain;

/**
 * 
 * @author Николай
 * 
 */
public class MFont extends Object implements PixselMapListener,
                NotifyEventListener, UndoableEditListener {
    public static final String FONT_ASCENT         = "mf.ascent";
    public static final String FONT_ASCENT_CAPITAL = "mf.line";
    public static final String FONT_AUTHOR_MAIL    = "mf.author.mail";
    public static final String FONT_AUTHOR_NAME    = "mf.author.name";
    public static final String FONT_BASELINE       = "mf.baseline";
    public static final String FONT_CHARSET        = "mf.charset";
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

    private MSymbol            firstSymbol;
    private int                size;
    private String             name;
    private String             prototype;
    private String             description;
    private boolean            fixsed;
    private String             charset;
    private String             authorName;
    private String             authorMail;
    private int                width;
    private int                validWidth;
    private int                minWidth;
    private int                maxWidth;
    private int                height;
    private int                validHeight;
    private int                marginLeft;
    private int                marginRight;
    private int                baseline;
    private int                ascent;
    private int                line;
    private int                descent;
    private ListenerChain      listeners           = new ListenerChain();
    private MFontUndo          undo;

    public MFont() {
        charset = null;
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
            charset = src.charset;
            authorName = src.authorName;
            authorMail = src.authorMail;
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
     * 
     * @param index
     * @return
     */
    protected boolean isValidCode(int index) {
        return symbolByCode(index) == null;
    }

    /**
     * Добавление получателя события изменения свойств символов.
     * 
     * @param listener Добавляемый получатель события.
     */
    public void addNotifyEventListener(NotifyEventListener listener) {
        listeners.add(NotifyEventListener.class, listener);
    }

    /**
     * Удаление получателя события изменения свойств символов.
     * 
     * @param listener Удаляемый получатель события.
     */
    public void removeNotifyEventListener(NotifyEventListener listener) {
        listeners.remove(NotifyEventListener.class, listener);
    }

    /**
     * Генерация события изменения свойств символов. Получатели добавляются
     * функцией {@link #addNotifyEventListener(NotifyEventListener)}.
     */
    protected void fireNotifyEvent(NotifyEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == NotifyEventListener.class)
                ((NotifyEventListener) listenerArray[i + 1])
                                .notifyHappened(event);
        }
    }

    protected void fireNotifyEvent(String message) {
        fireNotifyEvent(new NotifyEvent(this, message));
    }

    protected void fireNotifyEvent(MSymbol oldValue, MSymbol newValue) {
        String reason;

        if (oldValue == null) {
            if (newValue == null) return;
            reason = FONT_SYMBOL_ADDED;
        } else {
            if (newValue == null) reason = FONT_SYMBOL_REMOVE;
            else if (oldValue.equals(newValue)) return;
            else reason = FONT_SYMBOL_REPLACE;
        }

        fireNotifyEvent(reason);
    }

    protected void fireNotifyEvent(String oldValue, String newValue,
                    String message) {
        if (oldValue == null) {
            if (newValue == null) return;
        } else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        fireNotifyEvent(message);
    }

    protected void fireNotifyEvent(int oldValue, int newValue, String message) {
        if (oldValue == newValue) return;

        fireNotifyEvent(message);
    }

    protected void fireNotifyEvent(boolean oldValue, boolean newValue,
                    String message) {
        if (oldValue == newValue) return;

        fireNotifyEvent(message);
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
    public void notifyHappened(NotifyEvent event) {
        fireNotifyEvent(event);
    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        listeners.add(UndoableEditListener.class, listener);
    }

    public void removeUndoableEditListener(UndoableEditListener listener) {
        listeners.remove(UndoableEditListener.class, listener);
    }

    protected void fireUndoEvent(UndoableEditEvent change) {
        Object[] listenerArray;

        if (listeners == null) return;
        System.out.println("MFont: fire undo event");

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == UndoableEditListener.class)
                ((UndoableEditListener) listenerArray[i + 1])
                                .undoableEditHappened(change);
        }
    }

    public synchronized void beginChange(String operation) {
        if (undo != null) return;

        undo = new MFontUndo(this, operation);
    }

    public synchronized void endChange() {
        if (undo == null) return;

        undo.end();

        if (undo.canUndo()) fireUndoEvent(new UndoableEditEvent(this, undo));
        undo = null;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent event) {
        // TODO Здесь надо проверить, нужно ли добавить event к MFontUndo
        fireUndoEvent(event);
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
        setCharset(font.charset);
        setAuthorName(font.authorName);
        setAuthorMail(font.authorMail);
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

    public void setName(String name) {
        String old = this.name;
        this.name = convertName(name);
        fireNotifyEvent(old, this.name, FONT_NAME);
    }

    public String getPrototype() {
        return this.prototype;
    }

    public void setPrototype(String prototipe) {
        String old = this.prototype;
        this.prototype = convertName(prototipe);
        fireNotifyEvent(old, this.prototype, FONT_PROTOTYPE);
    }

    public String getDescriptin() {
        return this.description;
    }

    public void setDescriptin(String description) {
        String old = this.description;
        this.description = description;
        fireNotifyEvent(old, this.description, FONT_DESCRIPTION);
    }

    public boolean isFixsed() {
        return this.fixsed;
    }

    public void setFixsed(boolean fixsed) {
        MSymbol turn = firstSymbol;
        boolean old = this.fixsed;
        this.fixsed = fixsed;

        if (!old && this.fixsed) {
            while (turn != null) {
                try {
                    turn.setWidth(this.maxWidth);
                } catch (DisallowOperationException e) {
                }
                turn = turn.nextSymbol;
            }
            updateWidth();
        }

        fireNotifyEvent(old, this.fixsed, FONT_FIXSED);
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        String old = this.charset;
        this.charset = charset;

        fireNotifyEvent(old, this.charset, FONT_CHARSET);
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        String old = this.authorName;
        this.authorName = authorName;

        fireNotifyEvent(old, this.authorName, FONT_AUTHOR_NAME);
    }

    public String getAuthorMail() {
        return this.authorMail;
    }

    public void setAuthorMail(String authorMail) {
        String old = this.authorMail;
        this.authorMail = authorMail;

        fireNotifyEvent(old, this.authorMail, FONT_AUTHOR_MAIL);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        MSymbol turn = firstSymbol;
        int w, min, max, i, oldWidth, oldMax, oldMin;

        oldWidth = this.width;
        oldMax = this.maxWidth;
        oldMin = this.minWidth;
        validWidth = width;

        if (this.fixsed) {
            while (turn != null) {
                try {
                    turn.setWidth(width);
                } catch (DisallowOperationException e) {
                    System.out.println("bad width");
                }
                turn = turn.nextSymbol;
            }

            this.width = width;
            this.maxWidth = width;
            this.minWidth = width;
        } else {
            w = 0;
            max = 0;
            min = Integer.MAX_VALUE;
            i = 0;

            turn = firstSymbol;
            while (turn != null) {
                w += turn.getWidth();
                min = (min < turn.getWidth()) ? min : turn.getWidth();
                max = (max > turn.getWidth()) ? max : turn.getWidth();
                turn = turn.nextSymbol;

                if (i == 0) {
                    w = 0;
                    min = 0;
                    max = 0;
                } else w /= i;
                this.width = w;
                this.minWidth = min;
                this.maxWidth = max;
            }

        }

        fireNotifyEvent(oldWidth, this.width, FONT_WIDTH);
        fireNotifyEvent(oldMin, this.minWidth, FONT_WIDTH_MIN);
        fireNotifyEvent(oldMax, this.maxWidth, FONT_WIDTH_MAX);

        setMarginLeft(marginLeft);
        setMarginRight(marginRight);
    }

    protected void updateWidth() {
        setWidth(width);
    }

    public int getMinWidth() {
        return this.minWidth;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        int old = this.height;

        if (height < 0) throw (new IllegalArgumentException("invalid height"));

        this.height = height;
        validHeight = height;

        if (old != this.height) {
            MSymbol turn = firstSymbol;
            while (turn != null) {
                try {
                    turn.setHeight(this.height);
                } catch (DisallowOperationException e) {
                    System.out.println("bad height");
                }
                turn = turn.nextSymbol;
            }
        }

        fireNotifyEvent(old, this.height, FONT_HEIGHT);

        setBaseline(baseline);
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public int checkMarginLeft(int value) {
        int bound = (minWidth * 3 + 5) / 10;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setMarginLeft(int margin) {
        int old = this.marginLeft;

        // if (margin < 0) throw (new
        // IllegalArgumentException("invalid margin"));
        this.marginLeft = checkMarginLeft(margin);

        fireNotifyEvent(old, this.marginLeft, FONT_MARGIN_LEFT);
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public int checkMarginRight(int value) {
        int bound = (minWidth * 3 + 5) / 10;
        if (value < 0) return 0;
        else if (value > bound) return bound;
        else return value;
    }

    public void setMarginRight(int margin) {
        int old = this.marginRight;

        // if (margin < 0) throw (new
        // IllegalArgumentException("invalid margin"));
        this.marginRight = checkMarginRight(margin);

        fireNotifyEvent(old, this.marginRight, FONT_MARGIN_RIGHT);
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

        fireNotifyEvent(old, this.baseline, FONT_BASELINE);

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

        fireNotifyEvent(old, this.ascent, FONT_ASCENT);

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

        fireNotifyEvent(old, this.line, FONT_ASCENT_CAPITAL);
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

        fireNotifyEvent(old, this.descent, FONT_DESCENT);
    }

    /**
     * Return <code>true</code> if <code>ref</code> belong to font.
     */
    public boolean isBelong(MSymbol ref) {
        if (ref == null) return false;
        return ref.parent == this;
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
        MSymbol ret = firstSymbol;
        int i = 0;

        while (ret != null) {
            if (i == index) break;
            i++;
            ret = ret.nextSymbol;
        }
        return ret;
    }

    public MSymbol symbolByCode(int code) {
        MSymbol ret = firstSymbol;

        while (ret != null) {
            if (ret.getCode() == code) break;
            ret = ret.nextSymbol;
        }
        return ret;
    }

    private void add(MSymbol symbol, boolean fire) {
        MSymbol prev = null;
        MSymbol next = null;
        MSymbol curr;

        if (symbol == null) return;
        if (isBelong(symbol)) return;

        symbol.removePixselMapListener(symbol.parent);
        symbol.removeUndoableEditListener(symbol.parent);
        symbol.parent = this;
        try {
            symbol.setHeight(height);
        } catch (DisallowOperationException e) {
        }
        if (fixsed) try {
            symbol.setWidth(width);
        } catch (DisallowOperationException e) {
        }

        curr = firstSymbol;

        while (curr != null) {
            next = curr.nextSymbol;
            prev = curr.prevSymbol;
            if (curr.getCode() == symbol.getCode()) break;
            if (curr.getCode() > symbol.getCode()) {
                next = curr;
                curr = null;
                break;
            }
            prev = curr;
            curr = next;
            next = null;
        }

        symbol.prevSymbol = prev;
        symbol.nextSymbol = next;
        symbol.addPixselMapListener(this);
        symbol.addUndoableEditListener(this);

        if (prev == null) firstSymbol = symbol;
        else prev.nextSymbol = symbol;

        if (next != null) next.prevSymbol = symbol;

        if (curr == null) size++;

        if (fire) {
            fireNotifyEvent(curr, symbol);
            if (curr == null) fireNotifyEvent(size - 1, size, FONT_SIZE);
            updateWidth();
        }
    }

    private void remove(MSymbol symbol, boolean fire) {
        MSymbol prev, next;

        if (!isBelong(symbol)) return;

        prev = symbol.prevSymbol;
        next = symbol.nextSymbol;
        symbol.removePixselMapListener(this);
        symbol.prevSymbol = null;
        symbol.nextSymbol = null;
        symbol.parent = null;
        if (prev != null) prev.nextSymbol = next;
        if (next != null) next.prevSymbol = prev;
        size--;

        if (fire) {
            fireNotifyEvent(symbol, null);
            fireNotifyEvent(size + 1, size, FONT_SIZE);
            updateWidth();
        }
    }

    private void removeAll(boolean fire) {
        MSymbol turn;
        int oldSize;

        turn = firstSymbol;
        oldSize = size;

        firstSymbol = null;
        size = 0;

        while (turn != null) {
            turn.removePixselMapListener(this);
            turn.parent = null;
            turn = turn.nextSymbol;
        }

        if (fire) {
            fireNotifyEvent(oldSize, size, FONT_SIZE);
            updateWidth();
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
        MSymbol[] ret = new MSymbol[size];
        MSymbol turn = firstSymbol;
        int i = 0;

        while (turn != null) {
            ret[i] = new MSymbol(turn);
            i++;
            turn = turn.nextSymbol;
        }
        return ret;
    }

    public void setSymbols(MSymbol[] symbols) {
        removeAll(false);
        for (MSymbol sym : symbols) {
            add(sym, false);
        }
        fireNotifyEvent(FONT_SIZE);
        updateWidth();
    }
}
