
package microfont.render;

/**
 * Названия свойств отображения символа.
 */
public interface StylePropertyName {
    /** Высота надстрочной части символа. */
    public static final String STYLE_PROPERTY_ASCENT              = "style.ascent";
    /** Базовая линия строки. */
    public static final String STYLE_PROPERTY_BASELINE            = "style.baseline";
    /** Цвет бумаги. */
    public static final String STYLE_PROPERTY_COLOR_PAPER         = "style.color.paper";
    /** Цвет чернил. */
    public static final String STYLE_PROPERTY_COLOR_INK           = "style.color.ink";
    /** Цвет бумаги для полей. */
    public static final String STYLE_PROPERTY_COLOR_PAPER_MARGINS = "style.color.paper.margins";
    /** Цвет чернил для полей. */
    public static final String STYLE_PROPERTY_COLOR_INK_MARGINS   = "style.color.ink.margins";
    /** Цвет бумаги для надстрочной символа. */
    public static final String STYLE_PROPERTY_COLOR_PAPER_ASCENT  = "style.color.paper.ascent";
    /** Цвет чернил для надстрочной символа. */
    public static final String STYLE_PROPERTY_COLOR_INK_ASCENT    = "style.color.ink.ascent";
    /** Цвет бумаги для подстрочной части символа. */
    public static final String STYLE_PROPERTY_COLOR_PAPER_DESCENT = "style.color.paper.descent";
    /** Цвет чернил для подстрочной части символа. */
    public static final String STYLE_PROPERTY_COLOR_INK_DESCENT   = "style.color.ink.descrnt";
    /** Цвет зазора между пикселями. */
    public static final String STYLE_PROPERTY_COLOR_SPACE         = "style.color.space";
    /** Цвет сетки. */
    public static final String STYLE_PROPERTY_COLOR_GRID          = "style.color.grid";
    /** Подстрочная часть символа. */
    public static final String STYLE_PROPERTY_DESCENT             = "style.descent";
    /** Размер сетки. */
    public static final String STYLE_PROPERTY_GRID_SIZE           = "style.grid.size";
    /** Толщина сетки. */
    public static final String STYLE_PROPERTY_GRID_THICKNESS      = "style.grid.thickness";
    /** Высота строки символов. */
    public static final String STYLE_PROPERTY_LINE                = "style.line";
    /** Левое поле. */
    public static final String STYLE_PROPERTY_MARGIN_LEFT         = "style.magrin.left";
    /** Правое поле. */
    public static final String STYLE_PROPERTY_MARGIN_RIGHT        = "style.margin.right";
    /** Зазор между пикселями. */
    public static final String STYLE_PROPERTY_SPACE               = "style.space";
}
