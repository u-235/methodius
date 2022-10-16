
package microfont.render;

/**
 * Константы для обозначения индекса цвета в {@link PixselMapRender}.
 */
public interface ColorIndex {
    /** Индекс цвета бумаги. */
    public final static int COLOR_PAPER         = 0;
    /** Индекс цвета чернил. */
    public final static int COLOR_INK           = 1;
    /** Индекс цвета бумаги для полей. */
    public final static int COLOR_PAPER_MARGINS = 2;
    /** Индекс цвета чернил для полей. */
    public final static int COLOR_INK_MARGINS   = 3;
    /** Индекс цвета бумаги для надстрочной символа. */
    public final static int COLOR_PAPER_ASCENT  = 4;
    /** Индекс цвета чернил для надстрочной символа. */
    public final static int COLOR_INK_ASCENT    = 5;
    /** Индекс цвета бумаги для подстрочной части символа. */
    public final static int COLOR_PAPER_DESCENT = 6;
    /** Индекс цвета чернил для подстрочной части символа. */
    public final static int COLOR_INK_DESCENT   = 7;
    /** Индекс цвета зазора между пикселями. */
    public final static int COLOR_SPACE         = 8;
    /** Индекс цвета сетки. */
    public final static int COLOR_GRID          = 9;
    /** Максимальный индекс цвета. */
    public final static int COLOR_MAX           = 9;
}
