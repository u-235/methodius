
package microfont.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import microfont.AbstractMFont;
import microfont.AbstractPixselMap;
import microfont.MFont;
import microfont.MSymbol;
import microfont.Metrics;
import microfont.render.ColorIndex;
import microfont.render.ComponentRequest;
import microfont.render.PixselMapRender;
import microfont.render.Render;

/**
 * Отрисовщик {@link MSymbol символа} на экране.<img
 * src="../doc-files/render.png" align=right>
 * <p>
 * Этот класс на самом деле ничего не отображает, но предоставляет набор
 * возможностей для разнообразного отображения символов.<br>
 * На рисунке показан символ <b>Д</b> с шириной 12 и высотой 14 пикселей с
 * сеткой и полями.
 * <p>
 */
public class AbstractView extends JComponent implements Metrics {
    private static final long serialVersionUID = 1L;
    private PixselMapRender   render;
    protected Point           renderPos;
    protected MFont           owner;
    private FontListener      fontListener;

    /**
     * 
     * */
    public AbstractView() {
        renderPos = new Point();

        render = new PixselMapRender(new RenderListener());
        fontListener = new FontListener();
    }

    public Render render() {
        return render;
    }

    public AbstractPixselMap getPixselMap() {
        return render.getPixselMap();
    }

    public void setPixselMap(AbstractPixselMap apm) {
        MFont font;
        render.setPixselMap(apm);

        if (apm instanceof MSymbol) {
            MSymbol sym = (MSymbol) apm;
            AbstractMFont amf = sym.getOwner();
            if (amf instanceof MFont) font = (MFont) amf;
            else font = null;

            if (owner != font) {
                if (owner != null) {
                    owner.removePropertyChangeListener(fontListener);
                    owner = null;
                }

                owner = font;

                if (owner != null) {
                    owner.addPropertyChangeListener(fontListener);
                    updateRenderMetrics(owner);
                }
            }
        } else if (owner != null) {
            owner.removePropertyChangeListener(fontListener);
            owner = null;
        }
    }

    /**
     * Возвращает цвет по индексу.
     * 
     * @param cInd Индекс цвета. Может быть одним из<br>
     *            {@link ColorIndex#COLOR_PAPER}<br>
     *            {@link ColorIndex#COLOR_INK}<br>
     *            {@link ColorIndex#COLOR_PAPER_MARGINS}<br>
     *            {@link ColorIndex#COLOR_INK_MARGINS}<br>
     *            {@link ColorIndex#COLOR_PAPER_ASCENT}<br>
     *            {@link ColorIndex#COLOR_INK_ASCENT}<br>
     *            {@link ColorIndex#COLOR_PAPER_DESCENT}<br>
     *            {@link ColorIndex#COLOR_INK_DESCENT}<br>
     *            {@link ColorIndex#COLOR_SPACE}<br>
     *            {@link ColorIndex#COLOR_GRID}
     */
    public Color getColor(int cInd) {
        return render.getColor(cInd);
    }

    /**
     * Устанавливает цвет по индексу.
     * 
     * @param cInd Индекс цвета. Может быть одним из<br>
     *            {@link ColorIndex#COLOR_PAPER}<br>
     *            {@link ColorIndex#COLOR_INK}<br>
     *            {@link ColorIndex#COLOR_PAPER_MARGINS}<br>
     *            {@link ColorIndex#COLOR_INK_MARGINS}<br>
     *            {@link ColorIndex#COLOR_PAPER_ASCENT}<br>
     *            {@link ColorIndex#COLOR_INK_ASCENT}<br>
     *            {@link ColorIndex#COLOR_PAPER_DESCENT}<br>
     *            {@link ColorIndex#COLOR_INK_DESCENT}<br>
     *            {@link ColorIndex#COLOR_SPACE}<br>
     *            {@link ColorIndex#COLOR_GRID}
     * @param c Устанавливаемый цвет.
     */
    public void setColor(int cInd, Color c) {
        render.setColor(cInd, c);
    }

    /**
     * Возвращает размер пикселя.
     */
    public int getPixselSize() {
        return render.getPixselSize();
    }

    /**
     * Устанавливает размер пикселя.
     * 
     * @param size Новый размер пикселя.
     * @throws IllegalArgumentException Если {@code size} меньше или равно нулю.
     */
    public void setPixselSize(int size) {
        render.setPixselSize(size);
    }

    /**
     * Возвращает соотношение ширины пикселя к его высоте.
     */
    public float getPixselRatio() {
        return render.getPixselRatio();
    }

    /**
     * Устанавливает соотношение ширины пикселя к его высоте.
     * 
     * @param ratio соотношение ширины пикселя к его высоте.
     * @throws IllegalArgumentException Если {@code ratio} меньше или равно
     *             нулю.
     */
    public void setPixselRatio(float ratio) {
        render.setPixselRatio(ratio);
    }

    /**
     * Возврашает величину зазора между пикселями.
     */
    public int getSpacing() {
        return render.getSpacing();
    }

    /**
     * Устанавливает величину зазора между пикселями.
     * 
     * @param sp Зазор между пикселями.
     * @throws IllegalArgumentException Если {@code ratio} меньше нуля.
     */
    public void setSpacing(int sp) {
        render.setSpacing(sp);
    }

    /**
     * Возвращает толщину сетки.
     */
    public int getGridThickness() {
        return render.getGridThickness();
    }

    /**
     * Устанавливает толщину сетки.
     * 
     * @param gt Толщина сетки.
     * @throws IllegalArgumentException Если {@code gt} меньше нуля.
     */
    public void setGridThickness(int gt) {
        render.setGridThickness(gt);
    }

    /**
     * Возвращает размер сетки.
     */
    public int getGridSize() {
        return render.getGridSize();
    }

    /**
     * Устанавливает размер сетки.
     * 
     * @param gs Размер сетки.
     * @throws IllegalArgumentException Если {@code gs} меньше нуля.
     */
    public void setGridSize(int gs) {
        render.setGridSize(gs);
    }

    @Override
    public boolean isMetricActually(int index) {
        return render.isMetricActually(index);
    }

    @Override
    public void setMetricActually(int index, boolean state) {
        render.setMetricActually(index, state);
    }

    @Override
    public int getMetric(int index) {
        return render.getMetric(index);
    }

    @Override
    public void setMetric(int index, int value) {
        render.setMetric(index, value);
    }

    /**
     * Возвращает {@code true} если отрисовываются только закрашенные пиксели.
     */
    public boolean isDrawOnlyInk() {
        return render.isDrawOnlyInk();
    }

    /**
     * Устанавливает режим отрисовки символов.
     * 
     * @param doi {@code true} если нужно отрисовывать только закрашенные
     *            пиксели.
     */
    public void setDrawOnlyInk(boolean doi) {
        render.setDrawOnlyInk(doi);
    }

    /**
     * Возвращает {@code true} если отрисовывается сетка.
     */
    public boolean isDrawGrid() {
        return render.isDrawGrid();
    }

    /**
     * Устанавливает отрисовку сетки.
     * 
     * @param dg {@code true} если нужно отрисовывать сетку.
     */
    public void setDrawGrid(boolean dg) {
        render.setDrawGrid(dg);
    }

    /**
     * Возвращает {@code true} если поля отрисовываются.
     */
    public boolean isDrawMargins() {
        return render.isDrawMargins();
    }

    /**
     * Устанавливает отрисовку полей.
     * 
     * @param dm {@code true} если нужно отрисовывать поля.
     */
    public void setDrawMargins(boolean dm) {
        render.setDrawMargins(dm);
    }

    /**
     * Очищает область отрисовки компонента и делает отрисовку символа.
     */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getForeground());
        ((Graphics2D) g).setBackground(getBackground());

        if (isOpaque()) {
            int x, y, w, h;
            Rectangle clip = g.getClipBounds();
            if (clip == null) {
                x = 0;
                y = 0;
                w = getWidth();
                h = getHeight();
            } else {
                x = clip.x;
                y = clip.y;
                w = clip.width;
                h = clip.height;
            }
            g.clearRect(x, y, w, h);
        }
        render.paint(g, renderPos.x, renderPos.y);
    }

    protected void updateRenderMetrics(MFont font) {
        if (font == null) return;
        for (int i = 0; i <= Metrics.METRIC_MAX; i++) {
            render.setMetricActually(i, font.isMetricActually(i));
            render.setMetric(i, font.getMetric(i));
        }
    }

    protected class FontListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!(evt.getSource() instanceof MFont)) return;
            String prop = evt.getPropertyName();
            if (MFont.PROPERTY_ASCENT.equals(prop)) updateRenderMetrics(owner);
            if (MFont.PROPERTY_BASELINE.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.PROPERTY_DESCENT.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.PROPERTY_LINE.equals(prop)) updateRenderMetrics(owner);
            if (MFont.PROPERTY_MARGIN_LEFT.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.PROPERTY_MARGIN_RIGHT.equals(prop))
                updateRenderMetrics(owner);
        }
    }

    // Класс для получения запросов от PixselMapRender.
    protected class RenderListener implements ComponentRequest {
        @Override
        public void requestRepaint() {
            repaint();
        }

        @Override
        public void requestRepaint(Rectangle rect) {
            rect.x += renderPos.x;
            rect.y += renderPos.y;
            repaint(rect);
        }

        @Override
        public void requestInvalidate() {
            revalidate();
        }
    }
}
