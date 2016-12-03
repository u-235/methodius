
package microfont.gui;

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
import microfont.render.ComponentRequest;
import microfont.render.PixselMapRender;

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
public class AbstractView extends JComponent {
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

    public PixselMapRender getSymbolRender() {
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
