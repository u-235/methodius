
package microfont.render;

import java.awt.Color;

public class RenderStyle implements ColorIndex, StylePropertyName {
    /** Набор цветов для отрисовки пикселей. */
    private Color[] colors;
    /** Размер пикселя. */
    private int     pixselSize;
    /** Соотношение высоты пикселя к его ширине. */
    private float   pixselRatio;
    /** Зазор между пикселями. */
    private int     space;
    /** Толщина сетки. */
    private int     gridThickness;
    /** Размер сетки. */
    private int     gridSize;
    /** Размер левого поля. */
    private int     marginLeft;
    /** Размер правого поля. */
    private int     marginRight;
    /** Размер верхнего поля. */
    private int     marginTop;
    /** Размер нижнего поля. */
    private int     marginBottom;
    /** Расстояние до базовой линии строки. */
    private int     base;
    /** Высота строчных букв. */
    private int     line;
    /** Подъём (прописных) букв. */
    private int     ascent;
    /** Понижение букв. */
    private int     descent;

    public Color getColor(int cInd) {
        if (cInd < 0 || cInd > COLOR_MAX) return null;
        return colors[cInd];
    }

    public void setColor(int cInd, Color c) {
        if (cInd < 0 || cInd > COLOR_MAX) return;
        colors[cInd] = c;
    }

    public int getPixselSize() {
        return pixselSize;
    }

    public void setPixselSize(int size) {
        pixselSize = size;
    }

    public float getPixselRatio() {
        return pixselRatio;
    }

    public void setPixselRatio(float ratio) {
        pixselRatio = ratio;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int sp) {
        space = sp;
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
}
