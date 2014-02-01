
package microfont.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import microfont.AbstractMFont;
import microfont.AbstractPixselMap;
import microfont.MFont;
import microfont.MSymbol;
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
    public static int         ELEMENT_INDEX_RENDER = 0;
    private static final long serialVersionUID     = 1L;
    private PixselMapRender   render;
    protected Rectangle[]     elementsPos;
    protected MFont           owner;
    private FontListener      fontListener;

    /**
     * Создание объекта с установленными символом по умолчанию.
     * 
     * @param num
     * @throws IllegalArgumentException
     */
    public AbstractView(int num) {
        if (num < 1) throw new IllegalArgumentException("size =" + num);
        elementsPos = new Rectangle[num];
        for (int i = 0; i < num; i++) {
            elementsPos[i] = new Rectangle();
        }

        render = new PixselMapRender(new RenderListener());
        fontListener = new FontListener();
    }

    public Rectangle elementPosition(int i) {
        if (i < 0 || i >= elementsPos.length) return null;
        return elementsPos[i];
    }

    public PixselMapRender render() {
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
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        render.paint(g, elementsPos[ELEMENT_INDEX_RENDER].x,
                        elementsPos[ELEMENT_INDEX_RENDER].y);
    }

    protected void updateRenderMetrics(MFont font) {
        if (font == null) return;
        render.setMarginLeft(font.getMarginLeft());
        render.setMarginRight(font.getMarginRight());
        render.setBase(font.getBaseline());
        render.setLine(font.getLine());
        render.setAscent(font.getAscent());
        render.setDescent(font.getDescent());
    }

    protected class FontListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!(evt.getSource() instanceof MFont)) return;
            String prop = evt.getPropertyName();
            if (MFont.STYLE_PROPERTY_ASCENT.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.STYLE_PROPERTY_BASELINE.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.STYLE_PROPERTY_DESCENT.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.STYLE_PROPERTY_LINE.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.STYLE_PROPERTY_MARGIN_LEFT.equals(prop))
                updateRenderMetrics(owner);
            if (MFont.STYLE_PROPERTY_MARGIN_RIGHT.equals(prop))
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
            rect.x += elementsPos[0].x;
            rect.y += elementsPos[0].y;
            repaint(rect);
        }

        @Override
        public void requestInvalidate() {
            revalidate();
        }
    }
}
