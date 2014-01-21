
package microfont;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;
import utils.event.ListenerChain;

/**
 * Базовое представление шрифта.
 * 
 * <h3>Набор символов и кодировка.</h3>
 * <p>
 * Шрифт имеет массив {@linkplain MSymbol символов}, отсортированных по
 * {@linkplain MSymbol#getCode() коду} в порядке возрастания. Не допускается
 * наличие символов с одинаковым кодом. При этом возможен "пропуск" символов.
 * Это значит, что первым символом шрифта может быть символ с не нулевым кодом.
 * Так же, разница между кодом последующего символа и предыдущего может быть
 * больше единицы.
 * <p>
 * Шрифт имеет два взаимосвязанных свойства - <i>кодовую страницу</i> и
 * <i>кодировку</i>. Кодовая страница - это название кодировки; как правило, она
 * имеет стандартное название, например <code>cp1251</code>. Кодировка - это
 * объект <code>Charset</code>, который соответствует кодовой странице. При
 * изменении кодовой страницы происходит автоматическая смена кодировки и
 * наоборот.
 * <p>
 * При смене кодировки шрифт может заново отсортировать символы. Это происходит,
 * если старая и новая кодировка не равна <code>null</code>. В случае, если
 * старая кодировка была <code>null</code>, символам просто присваивается
 * свойство <code>Unicode</code>. Если новая кодировка равна <code>null</code>,
 * то свойство символов <code>Unicode</code> сбрасывается.
 * 
 * <h3>Дополнительные свойства.</h3>
 * <p>
 * Шрифт имеет несколько дополнительных свойств: флаг моноширинного
 * (фиксированного) шрифта, высоту символов и их ширину.
 * <p>
 * Моноширинный шрифт содержит символы одинаковой ширины. При попытки добавить к
 * такому шрифту символ с другой шириной произойдёт коррекция ширины этого
 * символа. При установке флага моноширинного шрифта символы шрифта будут
 * приведены к ширине самого широкого символа. Сброс флага не меняет ширины
 * символов.
 * <p>
 * Поведение свойства ширины зависит от типа шрифта.
 * <ul>
 * <li>Если шрифт моноширинный, то изменение ширины шрифта влечёт за собой
 * изменение ширины символов.
 * <li>Если шрифт не моноширинный, то изменение ширины шрифта не имеет никакого
 * эффекта и возвращаемым значением является среднее арифметическое от ширин
 * символов.
 * </ul>
 * <p>
 * Высота шрифта влияет на высоту символов. При смене этого свойства меняется и
 * высота всех символов.
 * 
 * <h3>Запись в журнал.</h3>
 * <p>
 * Для записи в журнал используйте статический метод {@link #logger()}.
 */
public class AbstractMFont implements PixselMapListener, PropertyChangeListener {
    public static final String MFONT_LOGGER_NAME  = "methodius.microfont";
    public static final String PROPERTY_CODE_PAGE = "mf.CodePage";
    public static final String PROPERTY_CHARSET   = "mf.CharSet";
    public static final String PROPERTY_FIXSED    = "mf.fixsed";
    public static final String PROPERTY_HEIGHT    = "mf.height";
    public static final String PROPERTY_SYMBOLS   = "mf.symbols";
    public static final String PROPERTY_WIDTH     = "mf.width";

    private static Logger      log                = Logger.getLogger(MFONT_LOGGER_NAME);
    private MSymbol[]          symbols;
    private boolean            fixsed;
    private String             codePage;
    private Charset            charSet;
    protected int              width;
    protected int              validWidth;
    protected int              height;
    protected int              validHeight;
    protected ListenerChain    listeners;

    /**
     * Конструктор для пустого шрифта.
     */
    public AbstractMFont() {
        width = 0;
        height = 0;
        codePage = null;
        charSet = null;
        symbols = new MSymbol[0];
        listeners = new ListenerChain();
    }

    /**
     * Конструктор копирования. Получатели событий не копируются.
     * 
     * @param src Копируемый шрифт. Если равен <code>null</code>, то создаётся
     *            пустой шрифт.
     * @see #copy(AbstractMFont)
     */
    public AbstractMFont(AbstractMFont src) {
        this();
        if (src == null) return;
        synchronized (src.getLock()) {
            copy(src);
        }
    }

    /**
     * Возвращает объект для записи сообщений в журнал. Этот статичный объект,
     * который создаётся при загрузке класса.
     */
    public static Logger logger() {
        return log;
    }

    /**
     * Возвращает объект для синхронизации.
     */
    protected Object getLock() {
        return this;
    }

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
        for (int i = 0; i < symbols.length; i++) {
            result = prime + symbols[i].hashCode();
        }
        return result;
    }

    /**
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AbstractMFont)) return false;
        AbstractMFont other = (AbstractMFont) obj;
        if (charSet == null) {
            if (other.charSet != null) return false;
            else if (codePage == null) {
                if (other.codePage != null) return false;
            } else if (!codePage.equals(other.codePage)) return false;
        } else if (!charSet.equals(other.charSet)) return false;
        if (fixsed != other.fixsed) return false;
        if (height != other.height) return false;
        if (width != other.width) return false;

        if (symbols.length != other.symbols.length) return false;
        for (int i = 0; i < symbols.length; i++) {
            if (!symbols[i].equals(other.symbols[i])) return false;
        }
        return true;
    }

    /**
     * Проверка допустимости ширины символа. Используется в
     * {@link MSymbol#isValidWidth(int)}.
     * 
     * @param w тестируемая ширина.
     * @return Для отрицательного <code>w</code> всегда возвращает
     *         <code>false</code>.<br>
     *         Если шрифт не моноширинный, то для положительного <code>w</code>
     *         возвращает <code>true</code>.<br>
     *         Если шрифт моноширинный, то возвращает <code>true</code> при
     *         равенстве <code>w</code> значению, использованному при последнем
     *         вызове {@link #prepareWidth(int)}.
     * @see #isValidSymbolHeight(int)
     */
    protected boolean isValidSymbolWidth(int w) {
        if (w < 0) return false;
        if (!fixsed) return true;
        return w == validWidth;
    }

    /**
     * Проверка допустимости высоты символа. Используется в
     * {@link MSymbol#isValidHeight(int)}.
     * 
     * @param h тестируемая ширина.
     * @return Для отрицательного <code>h</code> всегда возвращает
     *         <code>false</code>.<br>
     *         При равенстве <code>h</code> значению, использованному при
     *         последнем вызове {@link #prepareHeight(int)} возвращает
     *         <code>true</code>.
     * @see #isValidSymbolWidth(int)
     */
    protected boolean isValidSymbolHeight(int h) {
        if (h < 0) return false;
        return h == validHeight;
    }

    /**
     * Добавление получателя события при изменении свойств шрифта или символов
     * шрифта.
     * 
     * @param listener Добавляемый получатель события.
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #firePropertyChange(PropertyChangeEvent)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(PropertyChangeListener.class, listener);
    }

    /**
     * Удаление получателя события при изменении свойств шрифта или символов
     * шрифта.
     * 
     * @param listener Удаляемый получатель события.
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(PropertyChangeListener.class, listener);
    }

    /**
     * Генерация события изменения свойств шрифта или символов шрифта.
     * Получатели добавляются функцией
     * {@link #addPropertyChangeListener(PropertyChangeListener)}.<br>
     * Кроме случаев изменения свойств шрифта, генерация событий происходит и
     * при изменении свойств символов, принадлежащих шрифту.
     * 
     * @param event событие при изменении свойств.
     * @see #firePropertyChange(String, boolean, boolean)
     * @see #firePropertyChange(String, int, int)
     * @see #firePropertyChange(String, Object, Object)
     * @see #firePropertyChange(String, String, String)
     * @see #firePropertyChange(String, MSymbol, MSymbol)
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

    /**
     * Генерация события изменения свойств шрифта. Метод события
     * {@link PropertyChangeEvent#getSource()} вернёт указатель на этот шрифт.
     * 
     * @param property Название изменившегося свойства. Константы с названиями
     *            начинаются на <code>PROPERTY_</code>.
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
    protected void firePropertyChange(String property, Object oldValue,
                    Object newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, oldValue,
                        newValue));
    }

    /**
     * Генерация события изменения свойств шрифта. Метод события
     * {@link PropertyChangeEvent#getSource()} вернёт указатель на этот шрифт.
     * 
     * @param property Название изменившегося свойства. Константы с названиями
     *            начинаются на <code>PROPERTY_</code>.
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
    protected void firePropertyChange(String property, MSymbol oldValue,
                    MSymbol newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, oldValue,
                        newValue));
    }

    /**
     * Генерация события изменения свойств шрифта. Метод события
     * {@link PropertyChangeEvent#getSource()} вернёт указатель на этот шрифт.
     * 
     * @param property Название изменившегося свойства. Константы с названиями
     *            начинаются на <code>PROPERTY_</code>.
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
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

    /**
     * Генерация события изменения свойств шрифта. Метод события
     * {@link PropertyChangeEvent#getSource()} вернёт указатель на этот шрифт.
     * 
     * @param property Название изменившегося свойства. Константы с названиями
     *            начинаются на <code>PROPERTY_</code>.
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
    protected void firePropertyChange(String property, int oldValue,
                    int newValue) {
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, new Integer(
                        oldValue), new Integer(newValue)));
    }

    /**
     * Генерация события изменения свойств шрифта. Метод события
     * {@link PropertyChangeEvent#getSource()} вернёт указатель на этот шрифт.
     * 
     * @param property Название изменившегося свойства. Константы с названиями
     *            начинаются на <code>PROPERTY_</code>.
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
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

    /**
     * Копирование шрифта. Получатели событий не копируются.
     * 
     * @param font Копируемый шрифт.
     * @see #AbstractMFont(AbstractMFont)
     */
    public void copy(AbstractMFont font) {
        synchronized (getLock()) {
            for (MSymbol sym : symbols) {
                remove(sym);
            }

            setFixsed(font.fixsed);
            setWidth(font.width);
            setHeight(font.height);
            setCodePage(font.codePage);
            setCharset(font.getCharset());

            for (int i = 0; i < font.length(); i++) {
                add(new MSymbol(font.symbolByIndex(i)));
            }
        }
    }

    /**
     * Возвращает <code>true</code> если шрифт моноширинный.
     * 
     * @see #setFixsed(boolean)
     */
    public boolean isFixsed() {
        return fixsed;
    }

    /**
     * Изменяет тип шрифта. Если <code>f==true</code>, то ширина символов
     * выравнивается по самому широкому символу.
     * 
     * @param f Тип шрифта. Если равен <code>true</code>, то шрифт моноширинный.
     * @see #isFixsed()
     */
    public void setFixsed(boolean f) {
        synchronized (getLock()) {
            boolean old = fixsed;
            fixsed = f;
            if (!old && fixsed) setWidth(getMaxWidth());
            firePropertyChange(PROPERTY_FIXSED, old, fixsed);
        }
    }

    /**
     * Преобразование из уникода в код символа для текущей кодировки.
     * 
     * @throws UnsupportedOperationException Текущая кодировка не поддерживает
     *             преобразований уникода в код.
     * @throws CharacterCodingException Если символ с таким <code>unicode</code>
     *             не входит в шрифт.
     */
    int toCode(int unicode) throws UnsupportedOperationException,
                    CharacterCodingException {
        int ret = 0;
        CharBuffer cb = CharBuffer.wrap(Character.toChars(unicode));
        CharsetEncoder cse = charSet.newEncoder();
        cse.onUnmappableCharacter(CodingErrorAction.REPORT);
        byte[] bts = cse.encode(cb).array();

        for (int i = bts.length - 1; i >= 0; i--) {
            // byte bts[i] расширяется до int с учётом знака!!! Поэтому надо
            // у этого значения обнулить старшие три байта.
            ret = (ret << 8) | (bts[i] & 0xff);
        }
        return ret;
    }

    /**
     * Преобразование из кода символа в уникод для текущей кодировки.
     * 
     * @throws CharacterCodingException Если символ с таким <code>code</code> не
     *             входит в шрифт.
     */
    int toUnicode(int code) throws CharacterCodingException {
        byte[] bts = new byte[4];
        /*
         * Это место доставило мне немало хлопот. А всё из-за попытки сделать
         * CharBuffer.putInt(code), такой способ не работал.
         */
        int byteNum = 1;
        for (int i = 0; i < 4; i++) {
            bts[i] = (byte) code;
            if (bts[i] != 0) byteNum = i + 1;
            code >>= 8;
        }

        ByteBuffer bb = ByteBuffer.wrap(bts, 0, byteNum);
        CharsetDecoder csd = charSet.newDecoder();
        csd.onUnmappableCharacter(CodingErrorAction.REPORT);

        char[] chars = csd.decode(bb).array();

        return Character.codePointAt(chars, 0);
    }

    /**
     * Перемещает символ в соответствии с новым кодом.
     * 
     * @param sym Перемещаемый символ
     * @param code Код, который будет присвоен символу.
     */
    private void replace(MSymbol sym, int code) {
        if (sym.getCode() == code) return;
        // FIXME метод не реализован!
    }

    /**
     * Метод вызывается из {@link MSymbol#setCode(int)} для уведомления о
     * предстоящих изменениях. Это позволяет шрифту попытаться изменить и уникод
     * символа.
     * 
     * @param sym Символ, в котором будет изменение.
     * @param newCode Новый код символа.
     */
    void preChangeCode(MSymbol sym, int newCode) {
        // Если изменился код символа нужно изменить и уникод.
        if (isUnicode()) {
            try {
                sym.changeUnicode(toUnicode(newCode));
            } catch (CharacterCodingException e) {
                logger().log(Level.WARNING,
                                "unmapped simbol's code : " + sym.getCode(), e);
                // XXX Спорный момент - оставлять ли символ шрифте.
                remove(sym);
                return;
            }
        }

        replace(sym, newCode);
    }

    /**
     * Метод вызывается из {@link MSymbol#setUnicode(int)} для уведомления о
     * предстоящих изменениях. Это позволяет шрифту попытаться изменить и код
     * символа.
     * 
     * @param sym Символ, в котором будет изменение.
     * @param newUnicode Новый уникод символа.
     */
    void preChangeUnicode(MSymbol sym, int newUnicode) {
        if (!isUnicode()) return;

        try {
            int code = toCode(newUnicode);
            replace(sym, code);
            sym.changeCode(code);
        } catch (UnsupportedOperationException e) {
            logger().log(Level.WARNING,
                            "This charset does not support encoding");
        } catch (CharacterCodingException e) {
            logger().log(Level.WARNING,
                            "unmapped simbol's code : " + sym.getCode(), e);
            // XXX Спорный момент - оставлять ли символ шрифте.
            remove(sym);
        }
    }

    /**
     * Возвращает название кодовой страницы шрифта. Возвращаемое значение может
     * быть <code>null</code>.
     * 
     * @see #setCodePage(String)
     */
    public String getCodePage() {
        return codePage;
    }

    /**
     * Устанавливает кодовую страницу шрифта. Если новое название не является
     * названием или синонимом текущей кодировки, то
     * {@linkplain #setCharset(Charset) кодировка} меняется в соответствии с
     * новым названием. В этом случае символы могут быть отсортированы заново,
     * возможно с частичной потерей символов.
     * 
     * @param cp Название кодовой страницы шрифта.
     * @see #getCodePage()
     * @see #setCharset(Charset)
     */
    public void setCodePage(String cp) {
        synchronized (getLock()) {
            String oldCP = codePage;
            Charset oldCS = charSet;
            codePage = cp;

            if (codePage == null) {
                charSet = null;
            } else {
                // if (cp.equals(codePage)) return;
                if (charSet == null || (!charSet.name().equals(codePage))
                                || (!charSet.aliases().contains(codePage))) {
                    try {
                        charSet = Charset.forName(codePage);
                    } catch (Exception e) {
                        logger().log(Level.WARNING,
                                        "apply charset in setCodePage", e);
                        charSet = null;
                    }
                }
            }

            applyCharset(oldCS);
            firePropertyChange(PROPERTY_CODE_PAGE, oldCP, codePage);
            firePropertyChange(PROPERTY_CHARSET, oldCS, charSet);
        }
    }

    /**
     * Распространяет изменение кодировки на символы шрифта.
     * 
     * @param old Старое значение кодировки.
     */
    void applyCharset(Charset old) {
        if (!isUnicode()) {
            // Сброс unicode для всех символов.
            for (MSymbol sym : symbols) {
                sym.clearUnicode();
            }
        } else if (old == null) {
            // Установить unicode для всех символов.
            for (MSymbol sym : symbols) {
                try {
                    sym.setUnicode(toUnicode(sym.getCode()));
                } catch (CharacterCodingException e) {
                    // Символ не входит в новую кодировку.
                    // XXX Спорный момент - оставлять ли символ шрифте.
                    remove(sym);
                }
            }
        } else {
            // Поменять code для всех символов и отсортировать.
            MSymbol[] st = new MSymbol[symbols.length];
            System.arraycopy(symbols, 0, st, 0, symbols.length);

            try {
                for (MSymbol sym : st) {
                    try {
                        if (sym.isUnicode())
                            sym.setCode(toCode(sym.getUnicode()));
                        if (!isBelong(sym)) add(sym);
                    } catch (CharacterCodingException e) {
                        // Символ не входит в новую кодировку.
                        remove(sym);
                    }
                }
            } catch (UnsupportedOperationException ex) {
                logger().log(Level.WARNING,
                                "This charset does not support encoding");
            }
        }
    }

    /**
     * Возвращает {@code true} если шрифт {@link #getCharset() имеет
     * возможность} кодировки в уникод.
     */
    public boolean isUnicode() {
        return charSet != null;
    }

    /**
     * Возвращает кодировку шрифта. Возвращаемое значение может быть
     * <code>null</code>.
     * 
     * @see #setCharset(Charset)
     */
    public Charset getCharset() {
        return charSet;
    }

    /**
     * Устанавливает новую кодировку шрифта. Результатом может быть:
     * <ul>
     * <li>Если <code>cs == null</code>, то у символов шрифта сбрасывается
     * признак {@linkplain MSymbol#isUnicode() уникода}.
     * <li>Если <code>cs</code> не <code>null</code> и старое значение кодировки
     * было <code>null</code>, то символам присваивается значение уникода.
     * Символы, к которым невозможно получить уникод, удаляются из шрифта.
     * <li>Если <code>cs</code> не <code>null</code> и старое значение кодировки
     * тоже было не <code>null</code>, то символам присваивается новое значение
     * кода. Символы, к которым невозможно получить код, удаляются из шрифта.
     * Оставшиеся символы сортируются.
     * </ul>
     * 
     * @param cs Новая кодировка.
     * @see #getCharset()
     * @see #setCodePage(String)
     */
    public void setCharset(Charset cs) {
        synchronized (getLock()) {
            String oldCP = codePage;
            Charset oldCS = charSet;
            charSet = cs;

            if (charSet == null) {
                codePage = null;
            } else if (!charSet.name().equals(codePage)
                            || !charSet.aliases().contains(codePage)) {
                codePage = charSet.name();
            }

            applyCharset(oldCS);
            firePropertyChange(PROPERTY_CHARSET, oldCS, charSet);
            firePropertyChange(PROPERTY_CODE_PAGE, oldCP, codePage);
        }
    }

    /**
     * Возвращает ширину символов для моноширинного шрифта и среднее
     * арифметическое значение ширин символов для не моноширинного шрифта.<br>
     * Для {@linkplain #isEmpty() пустого} шрифта возвращает значение,
     * установленное последним вызовом {@link #setWidth(int)}.
     * 
     * @see #getMinWidth()
     * @see #getMaxWidth()
     */
    public int getWidth() {
        synchronized (getLock()) {
            if (isFixsed() || isEmpty()) return width;

            long ret = 0;
            for (MSymbol sym : symbols) {
                ret += sym.getWidth();
            }
            return (int) (ret / symbols.length);
        }
    }

    /**
     * Подготавливает шрифт к изменению ширины. С момента вызова этого метода
     * {@link #isValidSymbolWidth(int)} вернёт <code>true</code> с параметром,
     * равным <code>w</code>, однако метод {@link #getWidth()} всё ещё будет
     * возвращать старое значение.
     * 
     * @param w Будущая ширина шрифта. Должна быть не отрицательным числом.
     * @see #applyWidth()
     * @throws IllegalArgumentException Если <code>w</code> меньше нуля.
     */
    protected void prepareWidth(int w) {
        if (w < 0) throw new IllegalArgumentException("invalid width " + w);
        validWidth = w;
    }

    /**
     * Завершает изменение ширины шрифта. Если шрифт моноширинный, то при
     * необходимости корректируется ширина всех символов. Метод
     * {@link #getWidth()} будет возвращать новое значение.
     * 
     * @see #prepareWidth(int)
     */
    protected void applyWidth() {
        int oldWidth = width;
        width = validWidth;

        if (isFixsed()) {
            for (MSymbol sym : symbols) {
                if (sym.getWidth() == width) continue;
                try {
                    sym.setWidth(width);
                } catch (DisallowOperationException e) {
                    logger().log(Level.SEVERE,
                                    "MSymbol.setWidth in applyWidth : ", e);
                }
            }
        }

        firePropertyChange(PROPERTY_WIDTH, oldWidth, width);
    }

    /**
     * Устанавливает новую ширину символов шрифта. Для моноширинного шрифта
     * символы корректируются в соответствии с новой шириной.<br>
     * Для не моноширинного шрифта имеет эффект только на результат,
     * возвращаемый методом {@link #getWidth()}.
     * 
     * @param w Новая ширина шрифта. Должна быть не отрицательным числом.
     * @throws IllegalArgumentException Если <code>w</code> меньше нуля.
     */
    public void setWidth(int w) {
        synchronized (getLock()) {
            prepareWidth(w);
            applyWidth();
        }
    }

    /**
     * Для моноширинного или {@linkplain #isEmpty() пустого} шрифта эквивалентно
     * вызову {@link #getWidth()}.<br>
     * Для не моноширинного шрифта возвращает ширину самого узкого символа.
     */
    public int getMinWidth() {
        synchronized (getLock()) {
            if (isEmpty() || isFixsed()) return getWidth();

            int ret = Integer.MAX_VALUE;
            int w;

            for (MSymbol sym : symbols) {
                w = sym.getWidth();
                ret = ret < w ? ret : w;
            }

            return ret;
        }
    }

    /**
     * Для моноширинного или {@linkplain #isEmpty() пустого} шрифта эквивалентно
     * вызову {@link #getWidth()}.<br>
     * Для не моноширинного шрифта возвращает ширину самого широкого символа.
     */
    public int getMaxWidth() {
        synchronized (getLock()) {
            if (isEmpty() || isFixsed()) return getWidth();

            int ret = 0;
            int w;

            for (MSymbol sym : symbols) {
                w = sym.getWidth();
                ret = ret < w ? w : ret;
            }

            return ret;
        }
    }

    /**
     * Возвращает высоту символов шрифта.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Подготавливает шрифт к изменению высоты. С момента вызова этого метода
     * {@link #isValidSymbolHeight(int)} вернёт <code>true</code> с параметром,
     * равным <code>h</code>, однако метод {@link #getHeight()} всё ещё будет
     * возвращать старое значение.
     * 
     * @param h Будущая высота шрифта. Должна быть не отрицательным числом.
     * @see #applyHeight()
     * @throws IllegalArgumentException Если <code>h</code> меньше нуля.
     */
    protected void prepareHeight(int h) {
        if (h < 0) throw new IllegalArgumentException("invalid height " + h);
        validHeight = h;
    }

    /**
     * Завершает изменение высоты шрифта. При необходимости корректируется
     * высота всех символов. Метод {@link #getHeight()} будет возвращать новое
     * значение.
     * 
     * @see #prepareHeight(int)
     */
    protected void applyHeight() {
        int old = height;
        height = validHeight;

        if (old != height) {
            for (MSymbol sym : symbols) {
                if (sym.getHeight() == height) continue;
                try {
                    sym.setHeight(height);
                } catch (DisallowOperationException e) {
                    logger().log(Level.SEVERE,
                                    "MSymbol.setHeight in applyHeight : ", e);
                }
            }
        }

        firePropertyChange(PROPERTY_HEIGHT, old, height);
    }

    /**
     * Устанавливает новую высоту символов. При необходимости корректируется
     * высота всех символов.
     * 
     * @param h Новая высота символов шрифта. Должна быть не отрицательным
     *            числом.
     * @throws IllegalArgumentException Если <code>h</code> меньше нуля.
     */
    public void setHeight(int h) {
        synchronized (getLock()) {
            prepareHeight(h);
            applyHeight();
        }
    }

    /**
     * Возвращает <code>true</code> если символ <code>ref</code> принадлежит
     * шрифту.
     */
    public boolean isBelong(MSymbol ref) {
        if (ref == null) return false;
        return ref.owner == this;
    }

    /**
     * Возвращает <code>true</code> если шрифт не имеет символов.
     * 
     * @see #length()
     */
    public boolean isEmpty() {
        return symbols.length <= 0;
    }

    /**
     * Возвращает число символов, которые содержит шрифт.
     * 
     * @see #isEmpty()
     */
    public int length() {
        return symbols.length;
    }

    /**
     * Возвращает индекс символа с указанным кодом в массиве символов шрифта.
     * 
     * @param code Код символа.
     * @return Индекс символа или {@code -1} если символа с таким кодом в шрифте
     *         нет.
     */
    protected int indexByCode(int code) {
        if (isEmpty()) return -1;
        // Подготовка к поиску методом последовательного приближения.
        int len = length();
        int step = len / 2;
        int index = step;
        // Реализация последовательного приближения.
        while (index >= 0 && index < len) {
            int scd = symbols[index].getCode();
            if (scd == code) return index;
            step /= 2;
            if (step == 0) step = 1;

            if (scd < code) index += step;
            else index -= step;
        }
        return -1;
    }

    /**
     * Возвращает индекс символа с указанным уникодом в массиве символов шрифта.
     * 
     * @param unicode Уникод символа.
     * @return Индекс символа или {@code -1} если символа с таким уникодом в
     *         шрифте нет.
     */
    protected int indexByUnicode(int unicode) {
        if (!isUnicode()) return -1;
        if (isEmpty()) return -1;

        int code;
        try {
            code = toCode(unicode);
        } catch (UnsupportedOperationException e) {
            // Если получение уникода невозможно, то приходится искать
            // перебором.
            for (int i = 0; i < length(); i++) {
                if (symbols[i].getUnicode() == unicode) return i;
            }
            return -1;
        } catch (CharacterCodingException e) {
            // Символа с таким уникодом в шрифте не может быть.
            return -1;
        }

        return indexByCode(code);
    }

    /**
     * Возвращает индекс символа в массиве символов шрифта.
     * 
     * @param sym Искомый символ.
     * @return Индекс символа или {@code -1} если символ не принадлежит шрифту.
     *         Для {@code sym == null} так же вернёт {@code -1}.
     */
    public int indexAt(MSymbol sym) {
        synchronized (getLock()) {
            if (sym == null) return -1;
            if (!isBelong(sym)) return -1;
            return indexByCode(sym.getCode());
        }
    }

    /**
     * Возвращает символ по порядковому номеру внутреннего хранилища шрифта.
     * 
     * @param index Индекс символа во внутреннем хранилище шрифта.
     * @return Символ или <code>null</code> если <code>index</code> меньше нуля
     *         или больше или равен {@link #length()}.
     * @see #symbolByCode(int)
     * @see #symbolByUnicode(int)
     */
    public MSymbol symbolByIndex(int index) {
        synchronized (getLock()) {
            if (index >= length() || index < 0) return null;
            return symbols[index];
        }
    }

    /**
     * Возвращает символ по его коду в кодировке шрифта.
     * 
     * @param code Код символа.
     * @return Символ или <code>null</code> если символа с таким кодом нет.
     */
    public MSymbol symbolByCode(int code) {
        synchronized (getLock()) {
            return symbolByIndex(indexByCode(code));
        }
    }

    /**
     * Возвращает символ по его уникоду.
     * 
     * @param unicode Уникод символа.
     * @return Символ или <code>null</code> если символа с таким уникодом нет.
     */
    public MSymbol symbolByUnicode(int unicode) {
        synchronized (getLock()) {
            if (!isUnicode()) return null;
            return symbolByIndex(indexByUnicode(unicode));
        }
    }

    /**
     * Добавляет символ в шрифт.
     * 
     * @param symbol Добавляемый символ.
     */
    public void add(MSymbol symbol) {
        synchronized (getLock()) {
            MSymbol old = null;
            int i;

            if (symbol == null) return;
            if (isBelong(symbol)) return;
            if (symbol.owner != null) symbol.owner.remove(symbol);
            // Преобразования свойств код и уникод символа.
            if (charSet == null) {
                symbol.clearUnicode();
            } else if (symbol.isUnicode()) {
                try {
                    symbol.setCode(toCode(symbol.getUnicode()));
                } catch (UnsupportedOperationException e) {
                    logger().log(Level.WARNING,
                                    "This charset does not support encoding");
                } catch (CharacterCodingException e) {
                    logger().log(Level.WARNING,
                                    "unmapped simbol's code : "
                                                    + symbol.getCode(), e);
                    // XXX Спорный момент - оставлять ли символ шрифте.
                    return;
                }
            } else {
                try {
                    symbol.setUnicode(toUnicode(symbol.getCode()));
                } catch (CharacterCodingException e) {
                    logger().log(Level.WARNING,
                                    "unmapped simbol's code : "
                                                    + symbol.getCode(), e);
                    // XXX Спорный момент - оставлять ли символ шрифте.
                    return;
                }
            }

            symbol.addPropertyChangeListener(this);
            symbol.addPixselMapListener(this);
            symbol.owner = this;

            try {
                symbol.setHeight(height);
            } catch (DisallowOperationException e) {
                // Это исключение не должно возникнуть никогда.
                logger().log(Level.SEVERE, "fail apply height", e);
            }
            if (fixsed) try {
                symbol.setWidth(width);
            } catch (DisallowOperationException e) {
                // Это исключение не должно возникнуть никогда.
                logger().log(Level.SEVERE, "fail apply width", e);
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

    /**
     * Удаляет указанный символ из шрифта.
     * 
     * @param symbol Удаляемый символ.
     */
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
