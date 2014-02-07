
package microfont;

import java.util.logging.Level;

/**
 * Класс MSymbol для хранения и изменения символа {@link MFont шрифта}.<br>
 * Важно знать, что хотя символ и является разделяемым ресурсом, но один и тот
 * же символ не может принадлежать различным шрифтам.
 * 
 * <h3>Код символа.</h3>
 * <p>
 * Код символа - это индекс символа в кодовой странице шрифта. Для большинства
 * кодовых страниц это число в диаппазоне от 0 до 255. И для разных кодировок
 * один и тот же символ может иметь различный код. Это может стать причиной
 * затруднений при преобразовании шрифта из одной кодировки в другую.
 * <p>
 * Поэтому символ имеет два свойства:
 * <ol>
 * <li>{@link #code} - соответствует коду символа в кодовой странице шрифта. Это
 * свойство доступно через {@link #getCode()} {@link #setCode(int)}
 * <li>{@link #unicode} - код символа в UTF-16, независящий от кодировки.
 * MSymbol может изменять это свойство вызвав {@link #setUnicode(int)}, при этом
 * переменная {@link #hasUnicode} устанавливается в <code>true</code>, получение
 * значения возможно методом {@link #getUnicode()}. Это свойство может быть
 * неактуальным, что можно проверить при помощи {@link #isUnicode()}. *
 * </ol>
 * <p>
 */
public class MSymbol extends PixselMap {
    /** Шрифт, к которому принадлежит символ. */
    AbstractMFont              owner;
    /** Индекс символа в шрифте. Индекс зависит от кодовой страницы шрифта. */
    private int                code;
    /** Код символа в UTF-16 */
    private int                unicode;
    /** Был ли установлен код символа. */
    private boolean            hasUnicode;

    /**
     * Название свойства кода символа.
     * 
     * @see #firePropertyChange(String, int, int)
     */
    public final static String PROPERTY_CODE        = "MSymbol.code";
    /**
     * Название свойства уникода символа.
     * 
     * @see #firePropertyChange(String, int, int)
     */
    public final static String PROPERTY_UNICODE     = "MSymbol.unicode";
    /**
     * Название свойства символа "есть ли уникод".
     * 
     * @see #firePropertyChange(String, boolean, boolean)
     */
    public final static String PROPERTY_HAS_UNICODE = "MSymbol.HasUnicode";

    /**
     * Конструктор символа с заданными размерами, индексом и копированием
     * массива <b>byte</b>. <br>
     * Если копируемый массив меньше количества пикселей, то оставшиеся пиксели
     * будут установлены в ноль.
     * 
     * @param i Индекс символа в шрифте.
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     * @param a Массив для копирования.
     */
    public MSymbol(int i, int w, int h, byte[] a) {
        super(w, h, a);
        code = i;
    }

    /**
     * Конструктор символа с заданными размерами и индексом.<br>
     * 
     * @param i Индекс символа в шрифте.
     * @param w Ширина нового символа.
     * @param h Высота нового символа.
     */
    public MSymbol(int i, int w, int h) {
        this(i, w, h, null);
    }

    /**
     * Создание копии символа.<br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.
     * 
     * @see #copy(MSymbol)
     */
    @Override
    public MSymbol clone() {
        MSymbol ret = new MSymbol(getCode(), getWidth(), getHeight());
        try {
            ret.copy(this);
        } catch (DisallowOperationException e) {
            /*
             * Исключения не должно быть в принципе.
             */
            AbstractMFont.logger().log(Level.SEVERE,
                            "copy in PixselMap(PixselMap)", e);
        }
        return ret;
    }

    /**
     * Если символ принадлежит шрифту, то возвращается объект синхронизации
     * шрифта. Иначе возвращается объект синхронизации символа.
     */
    @Override
    protected Object writeLock() {
        if (owner == null) return super.writeLock();
        return owner.getLock();
    }

    /**
     * Результат проверки допустимости высоты зависит от того, принадлежит ли
     * символ шрифту или нет.<br>
     * Если символ принадлежит шрифту, то возвращается значение метода
     * {@link MFont#isValidSymbolHeight(int)}. Фактически это значит, что высота
     * может быть изменена <b>только методами шрифта</b>.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidHeight(int)}.
     */
    @Override
    protected boolean isValidHeight(int h) {
        if (owner != null) return owner.isValidSymbolHeight(h);
        return super.isValidHeight(h);
    }

    /**
     * Результат проверки допустимости ширины зависит от того, принадлежит ли
     * символ шрифту или нет.<br>
     * Если символ принадлежит шрифту, то возвращается значение метода
     * {@link MFont#isValidSymbolWidth(int)}.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidWidth(int)}.
     */
    @Override
    protected boolean isValidWidth(int w) {
        if (owner != null) return owner.isValidSymbolWidth(w);
        return super.isValidWidth(w);
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     * 
     * @see #setCode(int)
     */
    public int getCode() {
        return code;
    }

    /**
     * Метод изменяет код символа. Никаких других действий не производится.
     */
    void changeCode(int c) {
        code = c;
    }

    /**
     * Метод устанавливает индекс символа в шрифте.<br>
     * {@linkplain PixselMap#firePropertyChange(String, int, int) Выпускается
     * сообщение} {@link #PROPERTY_CODE}.
     * 
     * @param c Новый индекс.
     * @see #getCode()
     */
    public void setCode(int c) {
        synchronized (writeLock()) {
            if (code == c) return;
            int oldCode = code;
            int oldUnicode = unicode;
            boolean oldHasUnicode = hasUnicode;
            // Первым об изменении должен узнать шрифт.
            if (owner != null) owner.preChangeCode(this, c);
            changeCode(c);
            firePropertyChange(PROPERTY_CODE, oldCode, code);
            firePropertyChange(PROPERTY_HAS_UNICODE, oldHasUnicode, hasUnicode);
            firePropertyChange(PROPERTY_UNICODE, oldUnicode, unicode);
        }
    }

    /**
     * Возвращает <code>true</code> если символ имеет код UTF-16.
     * 
     * @see #setUnicode(int)
     * @see #getUnicode()
     */
    public boolean isUnicode() {
        return hasUnicode;
    }

    /**
     * Метод возвращает код символа в UTF-16.
     * 
     * @see #isUnicode()
     * @see #setUnicode(int)
     */
    public int getUnicode() {
        return unicode;
    }

    /**
     * Сбрасывает признак, что символ имеет действительный unicode.
     */
    public void clearUnicode() {
        synchronized (writeLock()) {
            hasUnicode = false;
            firePropertyChange(PROPERTY_HAS_UNICODE, true, false);
        }
    }

    /**
     * Метод изменяет уникод символа. Никаких других действий не производится.
     */
    void changeUnicode(int u) {
        unicode = u;
        hasUnicode = true;
    }

    /**
     * Метод устанавливает код символа в UTF-16.<br>
     * {@linkplain PixselMap#firePropertyChange(String, int, int) Выпускается
     * сообщение} {@link #PROPERTY_UNICODE}.
     * 
     * @param u Новый код символа.
     * @see #isUnicode()
     * @see #getUnicode()
     */
    public void setUnicode(int u) {
        synchronized (writeLock()) {
            if (u == unicode && hasUnicode) return;
            int oldCode = code;
            int oldUnicode = unicode;
            boolean oldHasUnicode = hasUnicode;
            // Первым об изменении должен узнать шрифт.
            if (owner != null) owner.preChangeUnicode(this, u);
            changeUnicode(u);
            firePropertyChange(PROPERTY_HAS_UNICODE, oldHasUnicode, hasUnicode);
            firePropertyChange(PROPERTY_UNICODE, oldUnicode, unicode);
            firePropertyChange(PROPERTY_CODE, oldCode, code);
        }
    }

    /**
     * Возвращает шрифт, к которому принадлежит символ.
     * 
     * @return Шрифт или <b>null</b> если символ не принадлежит ни какому
     *         шрифту.
     */
    public AbstractMFont getOwner() {
        return owner;
    }

    /**
     * Копирование массива точек из символа <code>sym</code>. Так же изменяется
     * переменные {@link #code} и {@link #unicode}.<br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.<br>
     * Могут {@linkplain PixselMap#firePropertyChange(String, int, int)
     * выпускаться сообщения} {@link #PROPERTY_CODE} и {@link #PROPERTY_UNICODE}
     * .
     * 
     * @param sym Источник копирования.
     * @throws DisallowOperationException Если изменение размеров запрещено
     *             конфигурацией объекта и размер копируемого символа не
     *             совпадает с размером текущего символа.
     * @throws DisallowOperationException Если символ принадлежит шрифту и
     *             изменение индекса в шрифте невозможно.
     * @throws NullPointerException Если <code>sym</code> равен
     *             <code>null</code>.
     * @see #clone()
     */
    public void copy(MSymbol sym) throws DisallowOperationException {
        super.copy(sym);
        if (sym.isUnicode()) setUnicode(sym.getUnicode());
        setCode(sym.getCode());
    }

    /**
     * Сравнение символов. Символы считаются равными, если у них совпадают
     * индекс, ширина, высота и содержимое массивов пикселей.
     * 
     * @param s Символ для сравнения.
     * @return <b>true</b> если символы равны.
     */
    @Override
    public boolean equals(Object s) {
        if (this == s) return true;
        if (!(s instanceof MSymbol)) return false;
        MSymbol sym = (MSymbol) s;
        if (!super.equals(sym)) return false;
        if (isUnicode() != sym.isUnicode()) return false;
        if (isUnicode()) return unicode == sym.unicode;
        return code == sym.code;
    }
}
