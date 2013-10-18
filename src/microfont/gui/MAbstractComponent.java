package microfont.gui;

import gui.ScrollableWindow;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import microfont.MFont;
import microfont.MSymbol;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;
import microfont.events.MSymbolEvent;
import microfont.events.MSymbolListener;

/**
 * Отрисовщик {@link MSymbol символа} на экране.<img
 * src="../doc-files/render.png" align=right>
 * <p>
 * Этот класс на самом деле ничего не отображает, но предоставляет набор
 * возможностей для разнообразного отображения символов.<br>
 * На рисунке показан символ <b>Д</b> с шириной 12 и высотой 14 пикселей с
 * сеткой и полями.
 * <p>
 * <b>Символ по умолчанию.</b><br>
 * Многие методы используют {@linkplain #symbol символ по умолчанию}. При
 * изменении этого символа вызывается метод
 * {@link #mSymbolEvent(MSymbolEvent)}, что позволяет оперативно обновлять
 * отображение символа.
 * <p>
 * <b>Отрисовка символа.</b><br>
 * <img src="../doc-files/source-elements.png"><br>
 * Метод
 * {@link #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color, Color)}
 * является базовым, другие методы отрисовки символа вызывают его, подставляя
 * переменные класса. При этом могут использоваться переменные
 * {@link #symbol} , {@link #ink}, {@link #paper},
 * {@link #pixselSize}.
 * <p>
 * <b>Отрисовка сетки.</b><br>
 * <p>
 * <b>Отрисовка полей.</b><br>
 * <p>
 * <b>Вспомогательные методы.</b><br>
 * Для определения попадания курсора используется метод
 * {@link #hit(MSymbolHit, int, int, microfont.MSymbol, int, int)}
 */
public class MAbstractComponent extends ScrollableWindow implements
                MSymbolListener, MFontListener
{
    /**/
    private static final long serialVersionUID = 1L;
    /**
     * Символ для отрисовки, используемый по умолчанию.
     * 
     * @see #setSymbol(MSymbol)
     * @see #getSymbol()
     */
    protected MSymbol         symbol;
    /** Имя шрифта. */
    protected String          charset          = "cp1251";
    /** Ширина пикселя символа при отображении на экране. */
    protected int             pixselSize      = 2;
    /** Ширина левого поля. */
    protected int             marginLeft;
    /** Ширина правого поля. */
    protected int             marginRight;
    protected int             baseline;
    protected int             ascent;
    protected int             ascentCapital;
    protected int             descent;
    /** Нужно ли отображать поля. */
    protected boolean         marginEnable;
    /** Цвет полей. */
    protected Color           margin           = new Color(0, 0, 255);
    protected Color           grayed           = new Color(128, 128, 128);
    /** Прозрачность полей. */
    float                     marginOpaque     = (float) 0.3;
    /** Толщина сетки для разграничения пикселей символа. */
    protected int             gridObesity      = 1;
    /** Нужно ли отображать сетку. */
    protected boolean         gridEnable       = false;
    /**  */
    protected Color           paper            = new Color(255, 255, 255);
    /**  */
    protected Color           ink              = new Color(0, 0, 96);
    /** Цвет сетки. */
    protected Color           gridColor        = new Color(0, 192, 0);
    /** Ширина "мёртвой зоны" в процентах. */
    protected int             deadzone         = 40;

    @SuppressWarnings("serial")
    public class RenderError extends Exception
    {
        public RenderError(String s) {
            super(s);
        }

    }

    /**
     * Создание объекта с установленными символом по умолчанию.
     * 
     * @param symbol Символ по умолчанию.
     */
    public MAbstractComponent(MSymbol symbol) {
        this.setSymbol(symbol);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isFocusable()) requestFocus();
            }
        });
        
        this.addKeyListener(new KeyAdapter() {          
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("typed");
                if (e.getKeyChar() == '-') ScalleMinus();
                if (e.getKeyChar() == '+') ScallePlus();
            }
        });
    }

    public MAbstractComponent() {
        this(null);
    }

    /**
     * Метод возвращает текущий {@linkplain #symbol символ} по умолчанию.
     * 
     * @return Текущий символ.
     * 
     * @see #setSymbol(MSymbol)
     */
    public MSymbol getSymbol() {
        return this.symbol;
    }

    /**
     * Метод устанавливает {@linkplain #symbol символ} по умолчанию. Если символ
     * принадлежит {@linkplain MFont шрифту}, то обновляются переменные
     * {@link #marginLeft}, {@link #marginRight},
     * {@link #marginTop}, {@link #marginBottom} и
     * {@link #charset}. Вызывается метод
     * {@link ScrollableWindow#revalidate() revalidate()}.
     * 
     * @param s Новый символ.
     * 
     * @see #getSymbol()
     */
    public void setSymbol(MSymbol s) {
        MFont parent;

        if (symbol != null) {
            symbol.removeListener(this);
            parent = symbol.getParent();
            if (parent != null) parent.removeListener(this);
        }

        this.symbol = s;
        if (symbol != null) {
            symbol.addListener(this);
            parent = s.getParent();

            if (parent != null) {
                parent.addListener(this);
                updateMargins();
                charset = parent.getCharset();
            }
        }

        revalidate();
        repaint();
    }

    void updateMargins() {
        MFont parent;

        if (symbol == null) return;
        parent = symbol.getParent();
        if (parent == null) return;

        marginLeft = parent.getMarginLeft();
        marginRight = parent.getMarginRight();
        baseline = parent.getBaseline();
        ascent = parent.getAscent();
        ascentCapital = parent.getAscentCapital();
        descent = parent.getDescent();
    }

    /**
     * Метод возвращает текущую высоту пикселя символа. Иными словами,
     * количество точек на экране, занимаемых по вертикали при отрисовке
     * пикселя.
     * 
     * @see #setPixselSize(int)
     */
    public int getPixselSize() {
        return pixselSize;
    }

    /**
     * 
     * @param width
     * @throws RenderError
     * @see #getPixselSize()
     */
    public void setPixselSize(int width) throws RenderError {
        int old;

        if (width < 1)
            throw (new RenderError("Invalid pixsel width =" + width));

        old = pixselSize;
        pixselSize = width;

        if (old != pixselSize) {
            revalidate();
            repaint();
        }
    }

    /**
     * 
     * @return цвет "чернил"
     */
    public Color getInkColor() {
        return ink;
    }

    /**
     * 
     * @param c
     */
    public void setInkColor(Color c) {
        ink = c;
        repaint();
    }

    /**
     * 
     * @return цвет "бумаги"
     */
    public Color getPaperColor() {
        return paper;
    }

    /**
     * 
     * @param c
     */
    public void setPaperColor(Color c) {
        paper = c;
        repaint();
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int width) throws RenderError {
        int old;

        if (width < 0)
            throw (new RenderError("Invalid left margin width =" + width));

        old = marginLeft;
        marginLeft = width;

        if (marginEnable && old != marginLeft) repaint();
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int width) throws RenderError {
        int old;

        if (width < 0)
            throw (new RenderError("Invalid right margin width =" + width));

        old = marginRight;
        marginRight = width;

        if (marginEnable && old != marginRight) repaint();
    }

    public boolean isMarginEnable() {
        return marginEnable;
    }

    public void setMarginEnable(boolean enable) {
        boolean old;

        old = marginEnable;
        marginEnable = enable;

        if (marginEnable && old != marginEnable) repaint();
    }

    public Color getMargin() {
        return margin;
    }

    public void setMargin(Color color) {
        if (color == null) return;

        margin = color;
        if (marginEnable) repaint();
    }

    public float getMarginOpaque() {
        return marginOpaque;
    }

    public void setMarginOpaque(float opaque) throws RenderError {
        if (opaque < 0 || opaque > 1)
            throw (new RenderError("Invalid  margin opaque =" + opaque));

        marginOpaque = opaque;
        repaint();
    }

    public int getGridObesity() {
        return gridObesity;
    }

    public void setGridObesity(int obesity) throws RenderError {
        if (obesity < 0)
            throw (new RenderError("Invalid  grid obesity =" + obesity));

        gridObesity = obesity;
        if (gridEnable) repaint();
    }

    public boolean isGidEnable() {
        return gridEnable;
    }

    public void setGridEnable(boolean enable) {
        boolean old;

        old = gridEnable;
        gridEnable = enable;

        if (gridEnable && old != gridEnable) repaint();
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color c) {
        gridColor = c;
        if (gridEnable) repaint();
    }

    /**
     * Метод вычисляет размер, необходимый для отбражения символа. Используются
     * {@linkplain #symbol символ} по умолчанию.
     * 
     * @return Возвращает <b>d</b> с вычисленными размерами. Если <b>d</b> равно
     *         <b>null</b>, то создаётся и возвращается новый объект.
     */
    public Dimension getSymbolSize() {
        return this.getSymbolSize(null, symbol);
    }

    /**
     * Метод вычисляет размер, необходимый для отбражения символа. Используются
     * {@linkplain #symbol символ} по умолчанию.
     * 
     * @param d Объект, в котором будет возвращён размер символа.
     * @return Возвращает <b>d</b> с вычисленными размерами. Если <b>d</b> равно
     *         <b>null</b>, то создаётся и возвращается новый объект.
     */
    public Dimension getSymbolSize(Dimension d) {
        return this.getSymbolSize(d, symbol);
    }

    /**
     * Метод вычисляет размер, необходимый для отбражения символа.
     * 
     * @param symbol Символ, для которого вычисляется размер.
     * @return Вычисленные размеры.
     */
    public Dimension getSymbolSize(MSymbol symbol) {
        return this.getSymbolSize(null, symbol);
    }

    /**
     * Метод вычисляет размер, необходимый для отбражения символа.
     * 
     * @param rv Объект, в котором будет возвращён размер символа.
     * @param symbol Символ, для которого вычисляется размер.
     * @return Возвращает <b>d</b> с вычисленными размерами. Если <b>d</b> равно
     *         <b>null</b>, то создаётся и возвращается новый объект.
     */
    public Dimension getSymbolSize(Dimension rv, MSymbol symbol) {
        Dimension ret;

        if (rv == null) ret = new Dimension();
        else ret = rv;

        if (symbol == null) ret.setSize(0, 0);
        else {
            ret.width = symbol.getWidth() * pixselSize;
            ret.height = symbol.getHeight() * pixselSize;
        }

        return ret;
    }

    /**
     * Отрисовка символа.
     * 
     * @param g Графический контекст отрисовки.
     * @param symbol Рисуемый символ.
     * @param onlyInk Режим отрисовки. Если задан <b>true</b>, то рисуются
     *            только пиксели, установленные в единицу. Если задан
     *            <b>false</b>, то отрисовываются все пиксели.
     * @param offsetX Смещение начальной точки отрисовки по горизонтали.
     * @param offsetY Смещение начальной точки по вертикали.
     * @param pixselWidth Ширина пикселя символа в экранных точках.
     * @param pixselHeight Высота пикселя символа в экранных точках.
     * @param paper Цвет "бумаги".
     * @param ink Цвет "чернил".
     * @see #drawSymbol(Graphics, MSymbol, int, int)
     * @see #drawSymbol(Graphics, MSymbol)
     * @see #drawSymbol(Graphics, int, int)
     * @see #drawSymbol(Graphics)
     */
    public static void drawSymbol(Graphics g, MSymbol symbol, boolean onlyInk,
                    int offsetX, int offsetY, int pixselWidth,
                    int pixselHeight, Color paper, Color ink) {
        Color c;
        Rectangle clip;
        int xo, x, y;
        int columnStart, columnEnd;
        int rowStart, rowEnd;
        boolean pixsel;

        if (symbol == null) return;

        clip = g.getClip().getBounds();
        c = g.getColor();

        x = clip.x - offsetX;
        y = clip.y - offsetY;

        columnStart = x / pixselWidth;
        columnEnd = (x + clip.width + pixselWidth - 1) / pixselWidth;
        if (columnStart < 0) columnStart = 0;
        if (columnEnd > symbol.getWidth()) columnEnd = symbol.getWidth();
        if (columnStart == columnEnd) return;

        rowStart = y / pixselHeight;
        rowEnd = (y + clip.height + pixselHeight - 1) / pixselHeight;
        if (rowStart < 0) rowStart = 0;
        if (rowEnd > symbol.getHeight()) rowEnd = symbol.getHeight();
        if (rowStart == rowEnd) return;

        x = columnStart * pixselWidth + offsetX;
        y = rowStart * pixselHeight + offsetY;

        xo = x - pixselWidth;

        for (int l = rowStart; l < rowEnd; l++) {
            x = xo;
            for (int r = columnStart; r < columnEnd; r++) {
                x += pixselWidth;

                try {
                    pixsel = symbol.getPixsel(r, l);
                }
                catch (Exception e) {
                    continue;
                }

                if (pixsel) g.setColor(ink);
                else {
                    if (onlyInk) continue;
                    g.setColor(paper);
                }

                g.fillRect(x, y, pixselWidth, pixselHeight);
            }
            y += pixselHeight;
        }

        g.setColor(c);
    }

    /**
     * 
     * @param g
     * @param symbol
     * @param offsetX
     * @param offsetY
     * @see #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color,
     *      Color)
     */
    public void drawSymbol(Graphics g, MSymbol symbol, int offsetX, int offsetY) {
        drawSymbol(g, symbol, false, offsetX, offsetY, this.pixselSize,
                        this.pixselSize, this.paper, this.ink);
    }

    /**
     * 
     * @param g
     * @param offsetX
     * @param offsetY
     * @see #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color,
     *      Color)
     */
    public void drawSymbol(Graphics g, int offsetX, int offsetY) {
        drawSymbol(g, this.symbol, false, offsetX, offsetY, this.pixselSize,
                        this.pixselSize, this.paper, this.ink);
    }

    /**
     * 
     * @param g
     * @param symbol
     * @see #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color,
     *      Color)
     */
    public void drawSymbol(Graphics g, MSymbol symbol) {
        drawSymbol(g, symbol, false, 0, 0, this.pixselSize, this.pixselSize,
                        this.paper, this.ink);
    }

    /**
     * 
     * @param g
     * @see #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color,
     *      Color)
     */
    public void drawSymbol(Graphics g) {
        drawSymbol(g, this.symbol, false, 0, 0, this.pixselSize,
                        this.pixselSize, this.paper, this.ink);
    }

    /**
     * 
     * @param g
     * @param symbol
     * @param offsetX
     * @param offsetY
     * @param pixselWidth
     * @param pixselHeight
     * @param gridColor
     * @param gridObesity
     */
    public void drawGrid(Graphics g, MSymbol symbol, int offsetX, int offsetY,
                    int pixselWidth, int pixselHeight, Color gridColor,
                    int gridObesity) {
        Color c;
        Rectangle clip;
        int xo, x, y;
        int columnStart, columnEnd;
        int rowStart, rowEnd;

        if (symbol == null) return;

        clip = g.getClip().getBounds();
        c = g.getColor();

        x = clip.x - offsetX;
        y = clip.y - offsetY;

        columnStart = x / pixselWidth;
        columnEnd = (x + clip.width + pixselWidth - 1) / pixselWidth;
        if (columnStart < 0) columnStart = 0;
        if (columnEnd > symbol.getWidth()) columnEnd = symbol.getWidth();
        if (columnStart == columnEnd) return;

        rowStart = y / pixselHeight;
        rowEnd = (y + clip.height + pixselHeight - 1) / pixselHeight;
        if (rowStart < 0) rowStart = 0;
        if (rowEnd > symbol.getHeight()) rowEnd = symbol.getHeight();
        if (rowStart == rowEnd) return;
        g.setColor(gridColor);

        x = columnStart * pixselWidth + offsetX;
        y = rowStart * pixselHeight + offsetY;

        xo = x;

        for (int l = rowStart; l <= rowEnd; l++) {
            x = xo;
            for (int r = columnStart; r <= columnEnd; r++) {
                g.fillRect(x - gridObesity * 2, y - gridObesity / 2,
                                gridObesity * 5, gridObesity);
                g.fillRect(x - gridObesity / 2, y - gridObesity * 2,
                                gridObesity, gridObesity * 5);
                x += pixselWidth;
            }
            y += pixselHeight;
        }

        g.setColor(c);
    }

    public void drawGrid(Graphics g, MSymbol symbol, int offsetX, int offsetY) {
        if (!gridEnable) return;
        drawGrid(g, symbol, offsetX, offsetY, pixselSize, pixselSize,
                        gridColor, gridObesity);
    }

    public void drawGrid(Graphics g, int offsetX, int offsetY) {
        if (!gridEnable) return;
        drawGrid(g, symbol, offsetX, offsetY, pixselSize, pixselSize,
                        gridColor, gridObesity);
    }

    public void drawGrid(Graphics g, MSymbol symbol) {
        if (!gridEnable) return;
        drawGrid(g, symbol, 0, 0, pixselSize, pixselSize, gridColor,
                        gridObesity);
    }

    public void drawGrid(Graphics g) {
        if (!gridEnable) return;
        drawGrid(g, symbol, 0, 0, pixselSize, pixselSize, gridColor,
                        gridObesity);
    }

    public void drawMargins(Graphics g, int offsetX, int offsetY) {
        Composite cmp;
        Color c;
        Graphics2D g2d;
        int x, w, h, left, right, baseline, ascent, capital, descent;

        if (symbol == null) return;

        g2d = (Graphics2D) g;
        c = g.getColor();
        cmp = g2d.getComposite();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        marginOpaque));
        g.setColor(margin);

        left = this.marginLeft * pixselSize;
        right = (symbol.getWidth() - this.marginRight) * pixselSize;

        baseline = pixselSize * this.baseline;
        ascent = baseline - pixselSize * this.ascent;
        capital = baseline - pixselSize * this.ascentCapital;
        descent = baseline + pixselSize * this.descent;

        h = symbol.getHeight() * pixselSize;
        w = symbol.getWidth() * pixselSize;

        g.fillRect(offsetX, offsetY, left, h);
        g.fillRect(offsetX + right, offsetY, w - right, h);

        x = left;
        w = right - left;

        g.fillRect(offsetX + x, offsetY + descent, w, h - descent);
        g.fillRect(offsetX + x, offsetY, w, ascent);

        g.setColor(grayed);

        g.fillRect(offsetX + x, offsetY + ascent, w, capital - ascent);
        g.fillRect(offsetX + x, offsetY + baseline, w, descent - baseline);

        g2d.setComposite(cmp);
        g.setColor(c);
    }

    /**
     * Метод определяет место на символе, к которому принадлежит точка. Позиция
     * точки и смещение символа задаются в координатах виджета.
     * 
     * @param rv Объект для сохранения информации о точке. Если задан
     *            <b>null</b>, то создаётся и возвращается новый объект.
     * @param posX Горизонтальная позиция точки.
     * @param posY Вертикальная позиция точки.
     */
    public MSymbolHit hit(MSymbolHit rv, int posX, int posY) {
        return this.hit(rv, posX, posY, symbol, 0, 0);
    }

    /**
     * Метод определяет место на символе, к которому принадлежит точка. Позиция
     * точки и смещение символа задаются в координатах виджета.
     * 
     * @param rv Объект для сохранения информации о точке. Если задан
     *            <b>null</b>, то создаётся и возвращается новый объект.
     * @param posX Горизонтальная позиция точки.
     * @param posY Вертикальная позиция точки.
     * @param offsetX Горизонтальное смещение символа.
     * @param offsetY Вертикальное смещение символа.
     */
    public MSymbolHit hit(MSymbolHit rv, int posX, int posY, int offsetX,
                    int offsetY) {
        return this.hit(rv, posX, posY, symbol, offsetX, offsetY);
    }

    /**
     * Метод определяет место на символе, к которому принадлежит точка. Позиция
     * точки и смещение символа задаются в координатах виджета.
     * 
     * @param rv Объект для сохранения информации о точке. Если задан
     *            <b>null</b>, то создаётся и возвращается новый объект.
     * @param posX Горизонтальная позиция точки.
     * @param posY Вертикальная позиция точки.
     * @param symbol Отображаемый символ.
     */
    public MSymbolHit hit(MSymbolHit rv, int posX, int posY, MSymbol symbol) {
        return this.hit(rv, posX, posY, symbol, 0, 0);
    }

    /**
     * Метод определяет место на символе, к которому принадлежит точка. Позиция
     * точки и смещение символа задаются в координатах виджета.
     * 
     * @param rv Объект для сохранения информации о точке. Если задан
     *            <b>null</b>, то создаётся и возвращается новый объект.
     * @param posX Горизонтальная позиция точки.
     * @param posY Вертикальная позиция точки.
     * @param symbol Отображаемый символ.
     * @param offsetX Горизонтальное смещение символа.
     * @param offsetY Вертикальное смещение символа.
     */
    public MSymbolHit hit(MSymbolHit rv, int posX, int posY, MSymbol symbol,
                    int offsetX, int offsetY) {
        MSymbolHit ret;
        int x, y, w, h;

        if (rv == null) {
            ret = new MSymbolHit();
        }
        else {
            ret = rv;
        }

        ret.flags = 0;
        ret.column = 0;
        ret.row = 0;

        if (symbol == null) {
            ret.flags = ret.OUTSIDE;
            return ret;
        }

        x = posX - offsetX;
        y = posY - offsetY;
        w = pixselSize * symbol.getWidth();
        h = pixselSize * symbol.getHeight();

        /* Проверка горизонтальной позиции точки. */
        if (x < 0) ret.column = -1;
        else if (x >= w) {
            ret.column = symbol.getWidth();
        }
        else {
            ret.flags |= ret.VALID_COLUMN;
            ret.column = x / pixselSize;
            x %= pixselSize;
        }

        /* Проверка вертикальной позиции точки. */
        if (y < 0) ret.row = -1;
        else if (y >= h) {
            ret.row = symbol.getHeight();
        }
        else {
            ret.flags |= ret.VALID_ROW;

            ret.row = y / pixselSize;
            y %= pixselSize;
        }

        if ((ret.flags & ret.VALID_COLUMN) != 0
                        && (ret.flags & ret.VALID_ROW) != 0) {
            ret.flags |= ret.PIXSEL;
            /* Проверка на "мёртвую зону". */
            int dX, dY;
            dX = pixselSize * deadzone / 100;
            dY = pixselSize * deadzone / 100;
            if ((dX != 0)
                            && (!isAbove(0, dY, dX, 0, x, y)
                                            || !isAbove(pixselSize - dX, 0,
                                                            pixselSize, dY, x,
                                                            y)
                                            || isAbove(pixselSize - dX,
                                                            pixselSize,
                                                            pixselSize,
                                                            pixselSize - dY,
                                                            x, y) || isAbove(0,
                                                                            pixselSize - dY, dX,
                                                                            pixselSize, x, y)))
                ret.flags |= ret.VALID_COLUMN;// DEAD_ZONE;
        }

        if ((ret.flags & ret.VALID_COLUMN) == 0
                        || (ret.flags & ret.VALID_ROW) == 0) {
            ret.flags |= ret.OUTSIDE;
        }
        return ret;
    }

    boolean isAbove(int firstX, int firstY, int secondX, int secondY,
                    int testX, int testY) {
        int y;
        y = firstY + (secondY - firstY) * (testX - firstX) / (secondX - firstX);
        return testY > y;
    }

    /**
     * 
     */
    @Override
    public void mSymbolEvent(MSymbolEvent change) {
        if (change.reason == MSymbolEvent.SIZE) revalidate();
        else repaint(change.x * pixselSize, change.y * pixselSize,
                        change.width * pixselSize, change.height
                                        * pixselSize);
    }

    @Override
    public Dimension calculatePrefSize(Dimension rv) {
        return getSymbolSize(rv);
    }

    @Override
    public void mFontEvent(MFontEvent change) {
        switch (change.getReason()) {
        case MFontEvent.FONT_ASCENT:
        case MFontEvent.FONT_ASCENT_CAPITAL:
        case MFontEvent.FONT_BASELINE:
        case MFontEvent.FONT_DESCENT:
        case MFontEvent.FONT_MARGIN_LEFT:
        case MFontEvent.FONT_MARGIN_RIGHT:
            updateMargins();
            repaint();
            break;
        case MFontEvent.FONT_CHARSET:
        }
    }
    
    void ScallePlus(){
            int w;
            w=getPixselSize();
            w+=(w+2)/3;
            try {
                setPixselSize(w);
            }
            catch (RenderError e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
    }
    
    void ScalleMinus(){
            int w;
            w=getPixselSize();
            w-=(w+1)/3;
            try {
                setPixselSize(w);
            }
            catch (RenderError e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }        
    }
}
