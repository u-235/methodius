
package microfont;

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
     * Название свойства size.
     * 
     * @see #firePropertyChange(String, int, int)
     */
    public final static String PROPERTY_CODE    = "MSymbolCode";
    /**
     * Название свойства size.
     * 
     * @see #firePropertyChange(String, int, int)
     */
    public final static String PROPERTY_UNICODE = "MSymbolUnicode";

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
        if (i < 0) throw (new IllegalArgumentException("Invalid code"));
        code = i;
    }

    /**
     * Создание копии символа.<br>
     * Важно знать, что <b>списки получателей сообщений не копируются</b>.
     * 
     * @param src Копируемый символ.
     */
    public MSymbol(MSymbol src) {
        super(src);
        code = src.code;
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
     * {@link MFont#isValidHeight(int)}. Фактически это значит, что высота может
     * быть изменена <b>только методами шрифта</b>.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidHeight(int)}.
     */
    @Override
    protected boolean isValidHeight(int h) {
        if (owner != null) return owner.isValidHeight(h);
        return super.isValidHeight(h);
    }

    /**
     * Результат проверки допустимости ширины зависит от того, принадлежит ли
     * символ шрифту или нет.<br>
     * Если символ принадлежит шрифту, то возвращается значение метода
     * {@link MFont#isValidWidth(int)}.<br>
     * Если символ не принадлежит шрифту, то возвращается
     * {@link PixselMap#isValidWidth(int)}.
     */
    @Override
    protected boolean isValidWidth(int w) {
        if (owner != null) return owner.isValidWidth(w);
        return super.isValidWidth(w);
    }

    /**
     * Метод возвращает индекс символа в шрифте.
     * 
     * @see #setCode(int)
     */
    public synchronized int getCode() {
        return code;
    }

    /**
     * Метод устанавливает индекс символа в шрифте.<br>
     * {@linkplain PixselMap#firePropertyChange(String, int, int) Выпускается
     * сообщение} {@link PROPERTY_CODE}.
     * 
     * @param c Новый индекс.
     * @see #getCode()
     */
    public synchronized void setCode(int c) {
        if (code == c) return;

        int old = code;
        code = c;
        firePropertyChange(PROPERTY_CODE, old, code);
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
    public synchronized int getUnicode() {
        return code;
    }

    /**
     * Метод устанавливает код символа в UTF-16.<br>
     * {@linkplain PixselMap#firePropertyChange(String, int, int) Выпускается
     * сообщение} {@link PROPERTY_UNICODE}.
     * 
     * @param u Новый код символа.
     * @see #isUnicode()
     * @see #getUnicode()
     */
    public synchronized void setUnicode(int u) {
        if (unicode == u) return;

        int old = unicode;
        unicode = u;
        firePropertyChange(PROPERTY_UNICODE, old, unicode);
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
     * выпускаться сообщения} {@link PROPERTY_CODE} и {@link PROPERTY_UNICODE}.
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
    public synchronized void copy(MSymbol sym)
                    throws DisallowOperationException {
        super.copy(sym);
        synchronized (sym) {
            if (sym.isUnicode()) setUnicode(sym.getUnicode());
            setCode(sym.getCode());
        }
    }

    /**
     * Сравнение символов. Символы считаются равными, если у них совпадают
     * индекс, ширина, высота и содержимое массивов пикселей.
     * 
     * @param s Символ для сравнения.
     * @return <b>true</b> если символы равны.
     */
    @Override
    public synchronized boolean equals(Object s) {
        if (!(s instanceof MSymbol)) return false;
        MSymbol sym = (MSymbol) s;
        if (isUnicode() != sym.isUnicode()) return false;
        if (isUnicode()) return super.equals(sym) && unicode == sym.unicode;
        return super.equals(sym) && code == sym.code;

    }
}
