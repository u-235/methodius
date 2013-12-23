
package microfont;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;
import utils.event.ListenerChain;

/**
 * 
 */
public class AbstractMFont extends Object implements PixselMapListener,
                PropertyChangeListener {
    public static final String MFONT_LOGGER_NAME  = "mf.logger";
    public static final String PROPERTY_CODE_PAGE = "mf.CodePage";
    public static final String PROPERTY_FIXSED    = "mf.fixsed";
    public static final String PROPERTY_HEIGHT    = "mf.height";
    public static final String PROPERTY_SYMBOLS   = "mf.symbols";
    public static final String PROPERTY_WIDTH     = "mf.width";

    private static Logger      log                = Logger.getLogger(MFONT_LOGGER_NAME);
    private MSymbol[]          symbols            = new MSymbol[0];
    private boolean            fixsed;
    private String             codePage;
    private Charset            charSet;
    protected int              width;
    protected int              validWidth;
    protected int              height;
    protected int              validHeight;
    protected ListenerChain    listeners          = new ListenerChain();

    public AbstractMFont() {
        codePage = null;
        width = 0;
        height = 0;
        validWidth = 0;
        validHeight = 0;
    }

    public AbstractMFont(AbstractMFont src) {
        synchronized (src) {
            fixsed = src.fixsed;
            codePage = src.codePage;
            width = src.width;
            height = src.height;
            validWidth = src.validWidth;
            validHeight = src.validHeight;
            setSymbols(src.getSymbols());
        }
    }

    public static Logger getLogger() {
        return log;
    }

    public static void setLogger(Logger l) {
        if (l == null) {
            log = Logger.getLogger(MFONT_LOGGER_NAME);
        } else {
            log = l;
        }
    }

    /**
     * Возвращает объект для синхронизации.
     */
    protected Object getLock() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charSet == null) ? 0 : charSet.hashCode());
        result = prime * result
                        + ((codePage == null) ? 0 : codePage.hashCode());
        result = prime * result + (fixsed ? 1231 : 1237);
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AbstractMFont)) return false;
        AbstractMFont other = (AbstractMFont) obj;
        if (charSet == null) {
            if (other.charSet != null) return false;
        } else if (!charSet.equals(other.charSet)) return false;
        if (codePage == null) {
            if (other.codePage != null) return false;
        } else if (!codePage.equals(other.codePage)) return false;
        if (fixsed != other.fixsed) return false;
        if (height != other.height) return false;
        if (width != other.width) return false;
        return true;
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
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #propertyChange(PropertyChangeEvent)
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

    protected void firePropertyChange(String property, Object oldValue,
                    Object newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property,
                        oldValue, newValue));
    }

    protected void firePropertyChange(String property, MSymbol oldValue,
                    MSymbol newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property,
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
     * @see #removePixselMapListener(PixselMapListener)
     * @see #pixselChanged(PixselMapEvent)
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
     * 
     * @see #pixselChanged(PixselMapEvent)
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
     * Получение события при свойств одного из символов шрифта. Это событие
     * {@linkplain #firePixselEvent(PixselMapEvent) транслируется} получателям
     * шрифта.
     * 
     * @see #addPixselMapListener(PixselMapListener)
     * @see #propertyChange(PropertyChangeEvent)
     */
    @Override
    public void pixselChanged(PixselMapEvent change) {
        firePixselEvent(change);
    }

    /**
     * Получение уведомляющего события от одного из символов. Это событие
     * {@linkplain #firePropertyChange(PropertyChangeEvent) транслируется}
     * получателям шрифта.
     * 
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #pixselChanged(PixselMapEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        firePropertyChange(event);
    }

    public void copy(AbstractMFont font) {
        synchronized (getLock()) {
            removeAll();
            setWidth(font.width);
            setHeight(font.height);
            setFixsed(font.fixsed);
            setSymbols(font.getSymbols());
            setCodePage(font.codePage);
        }
    }

    public boolean isFixsed() {
        return fixsed;
    }

    public void setFixsed(boolean f) {
        synchronized (getLock()) {
            boolean old = fixsed;
            int max = getMaxWidth();
            fixsed = f;

            firePropertyChange(PROPERTY_FIXSED, old, fixsed);

            if (!old && fixsed) {
                prepareWidth(max);
                for (MSymbol sym : symbols) {
                    try {
                        sym.setWidth(max);
                    } catch (DisallowOperationException e) {
                        log.log(Level.SEVERE, "apply width in setFixsed", e);
                    }
                }
                applyWidth();
            }
        }
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String cp) {
        synchronized (getLock()) {
            String old = codePage;
            codePage = cp;
            try {
                setCharset(Charset.forName(codePage));
            } catch (Exception e) {
                log.log(Level.WARNING, "apply charset in setCodePage", e);
                setCharset(null);
            }

            firePropertyChange(PROPERTY_CODE_PAGE, old, codePage);
        }
    }

    /**
     * @return the charSet
     */
    public Charset getCharset() {
        return charSet;
    }

    /**
     * @param charSet the charSet to set
     */
    public void setCharset(Charset cs) {
        synchronized (getLock()) {
            Charset old = charSet;
            charSet = cs;

            firePropertyChange(PROPERTY_CODE_PAGE, old, charSet);
            
            
        }
    }

    public int getWidth() {
        return width;
    }

    /**
     * @param w
     * @see #applyWidth()
     */
    protected void prepareWidth(int w) {
        if (w < 0) throw new IllegalArgumentException("invalid width " + w);
        validWidth = w;
    }

    /**
     * @see #prepareWidth(int)
     */
    protected void applyWidth() {
        int oldWidth;

        oldWidth = width;
        width = validWidth;

        firePropertyChange(PROPERTY_WIDTH, oldWidth, width);

        if (isFixsed()) {
            for (MSymbol sym : symbols) {
                try {
                    sym.setWidth(width);
                } catch (DisallowOperationException e) {
                    log.log(Level.SEVERE, "MSymbol.setWidth in applyWidth : ",
                                    e);
                }
            }
        }
    }

    public void setWidth(int w) {
        synchronized (getLock()) {
            prepareWidth(w);
            applyWidth();
        }
    }

    public int getMinWidth() {
        synchronized (getLock()) {
            if (symbols.length == 0) return getWidth();

            int ret = Integer.MAX_VALUE;
            int w;

            for (MSymbol sym : symbols) {
                w = sym.getWidth();
                ret = ret < w ? ret : w;
            }

            return ret;
        }
    }

    public int getMaxWidth() {
        synchronized (getLock()) {
            if (isEmpty()) return getWidth();

            int ret = 0;
            int w;

            for (MSymbol sym : symbols) {
                w = sym.getWidth();
                ret = ret < w ? w : ret;
            }

            return ret;
        }
    }

    public int getHeight() {
        return height;
    }

    /**
     * @param w
     * @see #applyHeight()
     */
    protected void prepareHeight(int h) {
        if (h < 0) throw new IllegalArgumentException("invalid height " + h);
        validHeight = h;
    }

    /**
     * @see #prepareHeight(int)
     */
    protected void applyHeight() {
        int old = height;
        height = validHeight;

        firePropertyChange(PROPERTY_HEIGHT, old, height);

        if (old != height) {
            for (MSymbol sym : symbols) {
                try {
                    sym.setHeight(height);
                } catch (DisallowOperationException e) {
                    log.log(Level.SEVERE,
                                    "MSymbol.setHeight in applyHeight : ", e);
                }
            }
        }
    }

    public void setHeight(int h) {
        synchronized (getLock()) {
            prepareHeight(h);
            applyHeight();
        }
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
     * @see #length()
     */
    public boolean isEmpty() {
        return symbols.length <= 0;
    }

    /**
     * Return number of symbols, that contain font.
     * 
     * @see #isEmpty()
     */
    public int length() {
        return symbols.length;
    }

    public MSymbol symbolByIndex(int index) {
        synchronized (getLock()) {
            if (index >= symbols.length) return null;

            return symbols[index];
        }
    }

    public MSymbol symbolByCode(int code) {
        synchronized (getLock()) {
            for (MSymbol sym : symbols) {
                if (sym.getCode() == code) return sym;
            }
            return null;
        }
    }

    public MSymbol symbolByUnicode(int code) {
        synchronized (getLock()) {
            if (charSet == null) return null;

            for (MSymbol sym : symbols) {
                if (sym.getUnicode() == code) return sym;
            }
            return null;
        }
    }

    public void add(MSymbol symbol) {
        synchronized (getLock()) {
            MSymbol old = null;
            int i;

            if (symbol == null) return;
            if (isBelong(symbol)) return;

            symbol.removePropertyChangeListener(symbol.owner);
            symbol.removePixselMapListener(symbol.owner);
            symbol.addPropertyChangeListener(this);
            symbol.addPixselMapListener(this);
            symbol.owner = this;

            try {
                symbol.setHeight(height);
            } catch (DisallowOperationException e) {
            }
            if (fixsed) try {
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
                old.removePropertyChangeListener(this);
                old.removePixselMapListener(this);
                old.owner = null;
                symbols[i] = symbol;
            }

            firePropertyChange(PROPERTY_SYMBOLS, old, symbol);
        }
    }

    public void remove(MSymbol symbol) {
        synchronized (getLock()) {
            int i;

            if (!isBelong(symbol)) return;

            i = 0;
            for (MSymbol sym : symbols) {
                if (sym == symbol) {
                    sym.removePropertyChangeListener(this);
                    sym.removePixselMapListener(this);
                    sym.owner = null;
                    break;
                }
                i++;
            }

            MSymbol[] t = new MSymbol[symbols.length - 1];
            System.arraycopy(symbols, 0, t, 0, i);
            System.arraycopy(symbols, i + 1, t, i, symbols.length - i - 1);
            symbols = t;

            firePropertyChange(PROPERTY_SYMBOLS, symbol, null);
        }
    }

    public void removeByCode(int index) {
        synchronized (getLock()) {
            remove(symbolByCode(index));
        }
    }

    public void removeAll() {
        synchronized (getLock()) {
            for (MSymbol sym : symbols) {
                remove(sym);
            }
        }
    }

    public MSymbol[] getSymbols() {
        synchronized (getLock()) {
            MSymbol[] ret = new MSymbol[symbols.length];

            for (int i = 0; i < symbols.length; i++) {
                ret[i] = new MSymbol(symbols[i]);
            }

            return ret;
        }
    }

    public void setSymbols(MSymbol[] ss) {
        synchronized (getLock()) {
            removeAll();

            for (MSymbol sym : ss) {
                add(new MSymbol(sym));
            }
        }
    }

    public Object getProperty(String property) {
        if (property.equals(PROPERTY_CODE_PAGE)) return getCodePage();
        else if (property.equals(PROPERTY_FIXSED)) return new Boolean(
                        isFixsed());
        else if (property.equals(PROPERTY_HEIGHT)) return new Integer(
                        getHeight());
        else if (property.equals(PROPERTY_WIDTH))
            return new Integer(getWidth());
        return null;
    }

    public void setProperty(String property, Object value) {
        if (value instanceof Integer) {
            int i = ((Integer) value).intValue();

            if (property.equals(PROPERTY_HEIGHT)) setHeight(i);
            else if (property.equals(PROPERTY_WIDTH)) setWidth(i);
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();

            if (property.equals(PROPERTY_FIXSED)) setFixsed(b);
        } else if (value instanceof String) {
            String s = (String) value;

            if (property.equals(PROPERTY_CODE_PAGE)) setCodePage(s);
        }
    }
}
