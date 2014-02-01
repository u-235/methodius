
package microfont.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import microfont.Document;
import microfont.render.ColorIndex;
import microfont.render.PixselMapRender;

public class MSymbolEditor extends AbstractView implements MouseListener,
                MouseMotionListener, MouseWheelListener {
    /**  */
    private static final long serialVersionUID = 1L;
    boolean                   changeEnable     = false;
    private Document          document;

    public MSymbolEditor() {
        super(1);
        setLayout(new MSymbolEditorLayout(this));

        PixselMapRender   render=render();
        render.setPixselSize(16);
        render.setSpace(1);
        render.setColor(ColorIndex.COLOR_PAPER_MARGINS, Color.LIGHT_GRAY);
        render.setColor(ColorIndex.COLOR_INK, new Color(0, 0, 0));
        render.setColor(ColorIndex.COLOR_INK_MARGINS, new Color(60, 0, 0));
        render.setColor(ColorIndex.COLOR_PAPER_ASCENT, new Color(224, 224, 224));
        render.setColor(ColorIndex.COLOR_PAPER, Color.WHITE);
        render.setColor(ColorIndex.COLOR_SPACE, new Color(208, 208, 208));
        render.setColor(ColorIndex.COLOR_GRID, new Color(128, 128, 128));
        render.setGridSize(5);
        render.setGridThickness(1);
        render.setDrawGrid(true);
        render.setDrawMargins(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    /** {@inheritDoc} */

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocus();
        // hit(null, e.getX(), e.getY());
    }

    /** {@inheritDoc} */

    @Override
    public void mousePressed(MouseEvent e) {
        if (!changeEnable) {

        }
    }

    /** {@inheritDoc} */

    @Override
    public void mouseReleased(MouseEvent e) {
        // if (symbol != null && document != null) document.endEdit();
        if (changeEnable) changeEnable = false;
    }

    /** {@inheritDoc} */

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /** {@inheritDoc} */

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /** {@inheritDoc} */

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /** {@inheritDoc} */

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int count;
        count = render().getPixselSize() + e.getWheelRotation();
        if (count < 3) count = 3;
        if (count > 25) count = 25;

        render().setPixselSize(count);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document doc) {
        if (document != null) {
            // document.addPropertyChangeListener(this);
        }

        document = doc;

        if (document != null) {
            // document.removePropertyChangeListener(this);
        }
    }
}
