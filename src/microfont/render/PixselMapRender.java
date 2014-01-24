
package microfont.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import microfont.AbstractPixselMap;

public class PixselMapRender implements ColorIndex {
    /** Карта пикселей для отрисовки. */
    private AbstractPixselMap pixmap;
    /** Высота карты пикселей как картинки. */
    private int               width;
    /** Ширина карты пикселей как картинки. */
    private int               height;
    /** Набор цветов для отрисовки пикселей. */
    private Color[]           colors;
    /** Набор картинок для быстрой отрисовки пикселя. */
    private Image[]           images;
    /** Размер пикселя. */
    private int               pixselSize;
    /** Соотношение высоты пикселя к его ширине. */
    private float             pixselRatio;
    /** Зазор между пикселями. */
    private int               space;
    /** Отрисовывать только закрашенные пиксели. */
    private boolean           drawOnlyInk;
    /** Толщина сетки. */
    private int               gridThickness;
    /** Размер сетки. */
    private int               gridSize;
    /** Отрисовывать сетку. */
    private boolean           drawGrid;
    /** Размер левого поля. */
    private int               marginLeft;
    /** Размер правого поля. */
    private int               marginRight;
    /** Размер верхнего поля. */
    private int               marginTop;
    /** Размер нижнего поля. */
    private int               marginBottom;
    /** Расстояние до базовой линии строки. */
    private int               base;
    /** Высота строчных букв. */
    private int               line;
    /** Подъём (прописных) букв. */
    private int               ascent;
    /** Понижение букв. */
    private int               descent;
    /** Отрисовывать поля своими цветами. */
    private boolean           drawMargins;

    /**
     * Создание отрисовщика для заданной карты пикселей.
     */
    public PixselMapRender(AbstractPixselMap apm) {
        pixmap = apm;
        colors = new Color[COLOR_MAX + 1];
        images = new Image[COLOR_MAX + 1];
        pixselSize = 1;
        pixselRatio = 1.0f;
        space = 0;
        updateSize();
    }

    /**
     * Создание отрисовщика.
     */
    public PixselMapRender() {
        this(null);
    }

    /**
     * Отрисовка карты пикселей.
     * 
     * @param g Графический контекст для отрисовки.
     * @param x Начальная координата по горизонтали.
     * @param y Начальная координата по вертикали.
     */
    public void paint(Graphics g, int x, int y) {
        if (pixmap == null || pixmap.isEmpty()) return;
        Graphics2D g2d = (Graphics2D) g;
        // TODO Auto-generated method stub
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public AbstractPixselMap getPixselMap() {
        return pixmap;
    }

    public void setPixselMap(AbstractPixselMap pm) {
        pixmap = pm;
        updateSize();
    }

    public Color getColor(int cInd) {
        if (cInd < 0 || cInd > COLOR_MAX) return null;
        return colors[cInd];
    }

    public void setColor(int cInd, Color c) {
        if (cInd < 0 || cInd > COLOR_MAX) return;
        colors[cInd] = c;
        images[cInd] = doPixselImage(c);
    }

    public int getPixselSize() {
        return pixselSize;
    }

    public void setPixselSize(int size) {
        pixselSize = size;
        updateSize();
    }

    public float getPixselRatio() {
        return pixselRatio;
    }

    public void setPixselRatio(float ratio) {
        pixselRatio = ratio;
        updateSize();
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int sp) {
        space = sp;
        updateSize();
    }

    public int getGridThickness() {
        return gridThickness;
    }

    public void getGridThickness(int gt) {
        gridThickness = gt;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gs) {
        gridSize = gs;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int m) {
        marginLeft = m;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int m) {
        marginRight = m;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int m) {
        marginTop = m;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int m) {
        marginBottom = m;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int b) {
        base = b;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int l) {
        line = l;
    }

    public int getAscent() {
        return ascent;
    }

    public void setAscent(int a) {
        ascent = a;
    }

    public int getDescent() {
        return descent;
    }

    public void setDescent(int d) {
        descent = d;
    }

    public boolean isDrawOnlyInk() {
        return drawOnlyInk;
    }

    public void setDrawOnlyInk(boolean doi) {
        drawOnlyInk = doi;
    }

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean dg) {
        drawGrid = dg;
    }

    public boolean isDrawMargins() {
        return drawMargins;
    }

    public void setDrawMargins(boolean dm) {
        drawMargins = dm;
    }

    public PointInfo getPointInfo(PointInfo pi, int x, int y) {
        PointInfo ret;
        if (pi == null) ret = new PointInfo();
        else ret = pi;

        return ret;
    }

    public Rectangle toPixselRect(Rectangle points, Rectangle pixsels) {
        Rectangle ret;
        if (pixsels == null) ret = new Rectangle();
        else ret = pixsels;

        return ret;
    }

    public Rectangle toPointRect(Rectangle pixsels, Rectangle points) {
        Rectangle ret;
        if (points == null) ret = new Rectangle();
        else ret = points;

        return ret;
    }

    protected void updateSize() {

    }

    protected Image doPixselImage(Color c) {
        if (c == null) return null;

        int w = pixselSize;
        int h = (int) (w * pixselRatio);

        Image img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();
        g.setColor(c);
        g.fillRect(0, 0, w, h);
        return img;
    }

    protected void updatePixselImages() {
        for (int i = 0; i <= COLOR_MAX; i++) {
            if (i == COLOR_SPACE) continue;
            if (i == COLOR_GRID) continue;
            Color c = colors[i];
            if (c == null) images[i] = null;
            else images[i] = doPixselImage(c);
        }
    }

    protected void drawPixsel(int x, int y, int cInd, Color bg, Color fg) {

    }
}
