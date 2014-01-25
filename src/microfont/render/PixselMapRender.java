
package microfont.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import microfont.AbstractPixselMap;

public class PixselMapRender implements ColorIndex, StylePropertyName {
    /** Карта пикселей для отрисовки. */
    private AbstractPixselMap pixmap;
    private RenderStyle       style;
    /** Высота карты пикселей как картинки. */
    private int               width;
    /** Ширина карты пикселей как картинки. */
    private int               height;
    /** Набор картинок для быстрой отрисовки пикселя. */
    private Image[]           images;
    /** Отрисовывать только закрашенные пиксели. */
    private boolean           drawOnlyInk;
    /** Отрисовывать сетку. */
    private boolean           drawGrid;
    /** Отрисовывать поля своими цветами. */
    private boolean           drawMargins;

    /**
     * Создание отрисовщика для заданной карты пикселей.
     */
    public PixselMapRender(AbstractPixselMap apm) {
        pixmap = apm;
        images = new Image[COLOR_MAX + 1];
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
        Rectangle oldCl = g2d.getClipBounds(null);
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

    public RenderStyle getStyle() {
        return style;
    }

    public void setStyle(RenderStyle rs) {
        style = rs;
        updateSize();
        updatePixselImages();
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

    protected int pixselWidth() {
        if (style == null) return 1;
        return style.getPixselSize();
    }

    protected int pixselHeight() {
        if (style == null) return 1;
        return (int) (style.getPixselSize() * style.getPixselRatio());
    }

    protected Color pixselColor(int index) {
        if (style == null) return null;
        return style.getColor(index);
    }

    protected Image doPixselImage(Color c) {
        if (c == null) return null;

        int w = pixselWidth();
        int h = pixselHeight();

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
            Color c = pixselColor(i);
            if (c == null) images[i] = null;
            else images[i] = doPixselImage(c);
        }
    }

    protected void drawPixsel(int x, int y, int cInd, Color bg, Color fg) {

    }
}
