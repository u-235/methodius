package microfont.events;

import utils.event.DataEvent;

import microfont.MFont;
import microfont.MSymbol;

@SuppressWarnings("serial")
public class MFontEvent extends DataEvent
{
    /** {@linkplain MSymbolEvent} */
    public static final int FONT_ASCENT         = 1;
    public static final int FONT_ASCENT_CAPITAL = 2;
    public static final int FONT_AUTHOR_MAIL    = 3;
    public static final int FONT_AUTHOR_NAME    = 4;
    public static final int FONT_BASELINE       = 5;
    public static final int FONT_CHARSET        = 6;
    public static final int FONT_DESCENT        = 7;
    public static final int FONT_FIXSED         = 8;
    public static final int FONT_HEIGHT         = 9;
    public static final int FONT_MARGIN_LEFT    = 10;
    public static final int FONT_MARGIN_RIGHT   = 11;
    public static final int FONT_NAME           = 12;
    public static final int FONT_PROTOTYPE      = 13;
    public static final int FONT_REMOVE_ALL     = 14;
    public static final int FONT_SIZE           = 15;
    public static final int FONT_SYMBOL_ADDED   = 16;
    public static final int FONT_SYMBOL_CHANGED = 17;
    public static final int FONT_SYMBOL_REMOVE  = 18;
    public static final int FONT_SYMBOL_REPLACE = 19;
    public static final int FONT_WIDTH          = 20;
    public static final int FONT_WIDTH_MAX      = 21;
    public static final int FONT_WIDTH_MIN      = 22;
    public static final int FONT_DESCRIPTION    = 23;

    private String          reasonName;
    private int             index;

    /**
     * Конструктор при удалении символа из шрифта.
     * 
     * @param source Шрифт, в котором произошли изменения.
     * @param index Индекс символа в шрифте.
     * @param symbol Удаляемый символ.
     */
    public MFontEvent(MFont source, int index, MSymbol symbol) {
        this(source, FONT_SYMBOL_REMOVE, symbol, null, index);
    }

    /**
     * Конструктор при изменениях в шрифте.
     * 
     * @param source Шрифт, в котором произошли изменения.
     * @param reason Причина изменений.
     * @param oldValue Старое значение параметра.
     * @param newValue Новое значение параметра.
     * @see #MFontEvent(Object, int, Object, Object, int)
     */
    public MFontEvent(MFont source, int reason, Object oldValue, Object newValue) {
        this(source, reason, oldValue, newValue, 0);
    }

    /**
     * Конструктор для внутренних изменений символов шрифта.
     * 
     * @param symbolChange
     */
    public MFontEvent(MFont source, MSymbolEvent symbolChange) {
        this(source, FONT_SYMBOL_CHANGED, symbolChange, symbolChange);
    }

    /**
     * Общий конструктор.
     * 
     * @param source Источник изменений. Обычно или {@linkplain MFont} или
     *            {@linkplain MSymbolEvent}.
     * @param reason Причина изменений. Может быть одним из
     *            <ul>
     *            <li>{@linkplain #FONT_SYMBOL_CHANGED}
     *            <li>{@linkplain #FONT_SYMBOL_ADDED}
     *            <li>{@linkplain #FONT_SYMBOL_REMOVE}
     *            <li>{@linkplain #FONT_SYMBOL_REPLACE}
     *            <li>{@linkplain #FONT_NAME}
     *            <li>{@linkplain #FONT_PROTOTYPE}
     *            <li>{@linkplain #FONT_FIXSED}
     *            <li>{@linkplain #FONT_CHARSET}
     *            <li>{@linkplain #FONT_AUTHOR_NAME}
     *            <li>{@linkplain #FONT_AUTHOR_MAIL}
     *            <li>{@linkplain #FONT_WIDTH}
     *            <li>{@linkplain #FONT_WIDTH_MIN}
     *            <li>{@linkplain #FONT_WIDTH_MAX}
     *            <li>{@linkplain #FONT_HEIGHT}
     *            <li>{@linkplain #FONT_MARGIN_LEFT}
     *            <li>{@linkplain #FONT_MARGIN_RIGHT}
     *            <li>{@linkplain #FONT_BASELINE}
     *            <li>{@linkplain #FONT_ASCENT}
     *            <li>{@linkplain #FONT_ASCENT_CAPITAL}
     *            <li>{@linkplain #FONT_DESCENT}
     *            </ul>
     * @param oldValue
     * @param newValue
     * @param index Индекс изменённого символа в шрифте. Этот параметр должен
     *            быть действительным при <b>reason</b>
     *            {@linkplain #FONT_SYMBOL_CHANGED},
     *            {@linkplain #FONT_SYMBOL_ADDED} или
     *            {@linkplain #FONT_SYMBOL_REMOVE}
     */
    public MFontEvent(Object source, int reason, Object oldValue,
                    Object newValue, int index) {
        super(source, reason, oldValue, newValue);

        switch (reason) {
        case FONT_SYMBOL_CHANGED:
            this.reasonName = "Symbol has internal change.";
            break;
        case FONT_SYMBOL_ADDED:
            this.reasonName = "New symbol added to font.";
            break;
        case FONT_SYMBOL_REMOVE:
            this.reasonName = "Font's symbol deleted.";
            break;
        case FONT_SYMBOL_REPLACE:
            this.reasonName = "Font's symbol replaced.";
            break;
        case FONT_NAME:
            this.reasonName = "Font's reasonName changed.";
            break;
        case FONT_PROTOTYPE:
            this.reasonName = "Font's prototype changed.";
            break;
        case FONT_FIXSED:
            this.reasonName = "Font's properties \"fixsed\" changed.";
            break;
        case FONT_CHARSET:
            this.reasonName = "Font's charset changed.";
            break;
        case FONT_AUTHOR_NAME:
            this.reasonName = "Font's author reasonName changed.";
            break;
        case FONT_AUTHOR_MAIL:
            this.reasonName = "Font's author contacts changed.";
            break;
        case FONT_WIDTH:
            this.reasonName = "Font's width changed.";
            break;
        case FONT_WIDTH_MIN:
            this.reasonName = "Font's minimal width changed.";
            break;
        case FONT_WIDTH_MAX:
            this.reasonName = "Font's maxsimal width changed.";
            break;
        case FONT_HEIGHT:
            this.reasonName = "Font's height changed.";
            break;
        case FONT_MARGIN_LEFT:
            this.reasonName = "Font's left margin changed.";
            break;
        case FONT_MARGIN_RIGHT:
            this.reasonName = "Font's right margin changed.";
            break;
        case FONT_BASELINE:
            this.reasonName = "Font's baseline changed.";
            break;
        case FONT_ASCENT:
            this.reasonName = "Font's ascent changed.";
            break;
        case FONT_ASCENT_CAPITAL:
            this.reasonName = "Font's ascent for capital letter changed.";
            break;
        case FONT_DESCENT:
            this.reasonName = "Font's descent changed.";
            break;
        default:
            // throw (new IllegalArgumentException("invalid reason."));
        }

        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * @return Текстовое представление причины изменений.
     */
    public String getReasonString() {
        return this.reasonName;
    }

    @Override
    public String toString() {
        return "MFont change event.";
    }
}
