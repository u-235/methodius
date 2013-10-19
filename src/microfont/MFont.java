package microfont;

import utils.event.DataEventListener;
import utils.event.ListenerChain;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;
import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

/**
 * 
 * @author Николай
 * 
 */
public class MFont extends Object implements MSymbolListener
{
    class Lock
    {
    }

    Lock            lockFont = new Lock();
    private MSymbol firstSymbol;
    private int     size;
    private String  name;
    private String  prototype;
    private String  description;
    private boolean fixsed;
    private String  charset;
    private String  authorName;
    private String  authorMail;
    private int     width;
    private int     minWidth;
    private int     maxWidth;
    private int     height;
    private int     marginLeft;
    private int     marginRight;
    private int     baseline;
    private int     ascent;
    private int     ascentCapital;
    private int     descent;

    private class Chain extends ListenerChain<MFontEvent>
    {
        @Override
        protected void listenerCall(DataEventListener listener, MFontEvent event) {
            ((MFontListener) listener).mFontEvent(event);
        }

    }

    private Chain listeners = new Chain();

    public MFont(int width, int height, String charset) {
        this.charset = charset;
        this.width = width;
        this.height = height;
    }

    public MFont(int width, int height) {
        this(width, height, null);
    }

    public MFont() {
        this(8, 8);
    }

    @Override
    public MFont clone() {
        MFont ret = new MFont();

        ret.name = this.name;
        ret.prototype = this.prototype;
        ret.fixsed = this.fixsed;
        ret.charset = this.charset;
        ret.authorName = this.authorName;
        ret.authorMail = this.authorMail;
        ret.width = this.width;
        ret.height = this.height;
        ret.setSymbols(this.getSymbols());
        ret.marginLeft = this.marginLeft;
        ret.marginRight = this.marginRight;
        ret.baseline = this.baseline;
        ret.ascent = this.ascent;
        ret.ascentCapital = this.ascentCapital;
        ret.descent = this.descent;

        return ret;
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

        setName(font.name);
        setPrototype(font.prototype);
        setCharset(font.charset);
        setAuthorName(font.authorName);
        setAuthorMail(font.authorMail);
        setMarginLeft(font.marginLeft);
        setMarginRight(font.marginRight);
        setBaseline(font.baseline);
        setAscent(font.ascent);
        setAscentCapital(font.ascentCapital);
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
        fireEvent(old, this.name, MFontEvent.FONT_NAME);
    }

    public String getPrototype() {
        return this.prototype;
    }

    public void setPrototype(String prototipe) {
        String old = this.prototype;
        this.prototype = convertName(prototipe);
        fireEvent(old, this.prototype, MFontEvent.FONT_PROTOTYPE);
    }

    public String getDescriptin() {
        return this.description;
    }

    public void setDescriptin(String description) {
        String old = this.description;
        this.description = description;
        fireEvent(old, this.description, MFontEvent.FONT_DESCRIPTION);
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
                turn.setWidth(this.maxWidth);
                turn = turn.nextSymbol;
            }
            updateWidth();
        }

        fireEvent(old, this.fixsed, MFontEvent.FONT_FIXSED);
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        String old = this.charset;
        this.charset = charset;

        fireEvent(old, this.charset, MFontEvent.FONT_CHARSET);
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        String old = this.authorName;
        this.authorName = authorName;

        fireEvent(old, this.authorName, MFontEvent.FONT_AUTHOR_NAME);
    }

    public String getAuthorMail() {
        return this.authorMail;
    }

    public void setAuthorMail(String authorMail) {
        String old = this.authorMail;
        this.authorMail = authorMail;

        fireEvent(old, this.authorMail, MFontEvent.FONT_AUTHOR_MAIL);
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

        if (this.fixsed) {
            while (turn != null) {
                turn.setWidth(width);
                turn = turn.nextSymbol;
            }

            this.width = width;
            this.maxWidth = width;
            this.minWidth = width;
        }
        else {
            w = 0;
            max = 0;
            min = Integer.MAX_VALUE;
            i = 0;

            turn = firstSymbol;
            while (turn != null) {
                w += turn.pixsels.width;
                min = (min < turn.pixsels.width) ? min : turn.pixsels.width;
                max = (max > turn.pixsels.width) ? max : turn.pixsels.width;
                turn = turn.nextSymbol;

                if (i == 0) {
                    w = 0;
                    min = 0;
                    max = 0;
                }
                else w /= i;
            }

            this.width = w;
            this.minWidth = min;
            this.maxWidth = max;
        }

        fireEvent(oldWidth, this.width, MFontEvent.FONT_WIDTH);
        fireEvent(oldMin, this.minWidth, MFontEvent.FONT_WIDTH_MIN);
        fireEvent(oldMax, this.maxWidth, MFontEvent.FONT_WIDTH_MAX);

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

        if (old != this.height) {
            MSymbol turn = firstSymbol;
            while (turn != null) {
                turn.setHeight(this.height);
                turn = turn.nextSymbol;
            }
        }

        fireEvent(old, this.height, MFontEvent.FONT_HEIGHT);

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

        fireEvent(old, this.marginLeft, MFontEvent.FONT_MARGIN_LEFT);
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

        fireEvent(old, this.marginRight, MFontEvent.FONT_MARGIN_RIGHT);
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

        fireEvent(old, this.baseline, MFontEvent.FONT_BASELINE);

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

        fireEvent(old, this.ascent, MFontEvent.FONT_ASCENT);

        setAscentCapital(ascentCapital);
    }

    public int getAscentCapital() {
        return this.ascentCapital;
    }

    public int checkAscentCapital(int value) {
        int bound = ascent / 2;
        if (value < bound) return bound;
        else if (value > ascent) return ascent;
        else return value;
    }

    public void setAscentCapital(int ascentCapital) {
        int old = this.ascentCapital;

        if (ascentCapital < 0)
            throw (new IllegalArgumentException("invalid ascent"));
        this.ascentCapital = checkAscentCapital(ascentCapital);

        fireEvent(old, this.ascentCapital, MFontEvent.FONT_ASCENT_CAPITAL);
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

        fireEvent(old, this.descent, MFontEvent.FONT_DESCENT);
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

        synchronized (lockFont) {
            while (ret != null) {
                if (i == index) break;
                i++;
                ret = ret.nextSymbol;
            }
        }
        return ret;
    }

    public MSymbol symbolAtIndex(int index) {
        MSymbol ret = firstSymbol;

        synchronized (lockFont) {
            while (ret != null) {
                if (ret.index == index) break;
                ret = ret.nextSymbol;
            }
        }
        return ret;
    }

    private void add(MSymbol symbol, boolean fire) {
        MSymbol prev = null;
        MSymbol next = null;
        MSymbol curr;

        if (symbol == null) return;
        if (isBelong(symbol)) return;

        symbol.setHeight(height);
        if (fixsed) symbol.setWidth(width);

        synchronized (lockFont) {
            curr = firstSymbol;

            while (curr != null) {
                next = curr.nextSymbol;
                prev = curr.prevSymbol;
                if (curr.index == symbol.index) break;
                if (curr.index > symbol.index) {
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
            symbol.parent = this;
            symbol.addListener(this);

            if (prev == null) firstSymbol = symbol;
            else prev.nextSymbol = symbol;

            if (next != null) next.prevSymbol = symbol;

            if (curr == null) size++;
        }

        if (fire) {
            fireEvent(curr, symbol);
            if (curr == null) fireEvent(size - 1, size, MFontEvent.FONT_SIZE);
            updateWidth();
        }
    }

    private void remove(MSymbol symbol, boolean fire) {
        MSymbol prev, next;

        if (!isBelong(symbol)) return;

        synchronized (lockFont) {
            prev = symbol.prevSymbol;
            next = symbol.nextSymbol;
            symbol.removeListener(this);
            symbol.prevSymbol = null;
            symbol.nextSymbol = null;
            symbol.parent = null;
            if (prev != null) prev.nextSymbol = next;
            if (next != null) next.prevSymbol = prev;
            size--;
        }

        if (fire) {
            fireEvent(symbol, null, MFontEvent.FONT_SYMBOL_REMOVE);
            fireEvent(size + 1, size, MFontEvent.FONT_SIZE);
            updateWidth();
        }
    }

    private void removeAll(boolean fire) {
        MSymbol turn;
        MSymbol oldSym;
        int oldSize;

        synchronized (lockFont) {
            turn = firstSymbol;
            oldSym = firstSymbol;
            oldSize = size;

            firstSymbol = null;
            size = 0;

            while (turn != null) {
                turn.removeListener(this);
                turn.parent = null;
                turn = turn.nextSymbol;
            }
        }

        if (fire) {
            fireEvent(oldSym, null, MFontEvent.FONT_REMOVE_ALL);
            fireEvent(oldSize, size, MFontEvent.FONT_SIZE);
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
        remove(symbolAtIndex(index));
    }

    public void removeAll() {
        removeAll(true);
    }

    public MSymbol[] getSymbols() {
        MSymbol[] ret = new MSymbol[size];
        MSymbol turn = firstSymbol;
        int i = 0;

        while (turn != null) {
            ret[i] = turn.clone();
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
        fireEvent(0, size, MFontEvent.FONT_SIZE);
        updateWidth();
    }

    /**
     * Добавление получателя события.
     * 
     * @param toAdd Добавляемый получатель события.
     */
    public void addListener(MFontListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Удаление получателя события.
     * 
     * @param toRemove Удаляемый получатель события.
     */
    public void removeListener(MFontListener toRemove) {
        listeners.remove(toRemove);
    }

    protected void fireEvent(MFontEvent change) {
        listeners.fire(change);
    }

    protected void fireEvent(MSymbol oldValue, MSymbol newValue) {
        int reason;

        if (oldValue == null) {
            if (newValue == null) return;
            reason = MFontEvent.FONT_SYMBOL_ADDED;
        }
        else {
            if (newValue == null) reason = MFontEvent.FONT_SYMBOL_REMOVE;
            else if (oldValue.equals(newValue)) return;
            else reason = MFontEvent.FONT_SYMBOL_REPLACE;
        }

        fireEvent(new MFontEvent(this, reason, oldValue, newValue));

    }

    private void fireEvent(MSymbol oldValue, MSymbol newValue, int reason) {
        // TODO Auto-generated method stub
        fireEvent(new MFontEvent(this, reason, oldValue, newValue));
    }

    protected void fireEvent(String oldValue, String newValue, int reason) {
        if (oldValue == null) {
            if (newValue == null) return;
        }
        else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        fireEvent(new MFontEvent(this, reason, oldValue, newValue));
    }

    protected void fireEvent(int oldValue, int newValue, int reason) {
        if (oldValue == newValue) return;

        fireEvent(new MFontEvent(this, reason, oldValue, newValue));
    }

    protected void fireEvent(boolean oldValue, boolean newValue, int reason) {
        if (oldValue == newValue) return;

        fireEvent(new MFontEvent(this, reason, oldValue, newValue));
    }

    @Override
    public void mSymbolEvent(MSymbolEvent change) {
        fireEvent(new MFontEvent(this, change));
    }
}
