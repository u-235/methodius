
package microfont.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import microfont.Document;
import microfont.MSymbol;

public class MSymbolEditor extends MAbstractComponent implements MouseListener,
                MouseMotionListener, MouseWheelListener {
    /**  */
    private static final long serialVersionUID = 1L;
    boolean                   changeEnable     = false;
    boolean                   changeSet        = false;
    int                       prevX, prevY;
    private Document          document;

    public MSymbolEditor(MSymbol symbol) {
        super(symbol);

        render.setPixselSize(12);
        render.setSpace(1);
        render.setDrawGrid(true);
        render.setDrawMargins(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    public MSymbolEditor() {
        this(null);
    }

    /** {@inheritDoc} */

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocus();
        //hit(null, e.getX(), e.getY());
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
        if (symbol != null && document != null) document.endEdit();
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
        count = render.getPixselSize() + e.getWheelRotation();
        if (count < 3) count = 3;
        if (count > 25) count = 25;

        render.setPixselSize(count);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void paint(Graphics g, int X, int Y) {
        render.paint(g, X, Y);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document doc) {
        if (document != null) {
            //document.addPropertyChangeListener(this);
        }

        document = doc;

        if (document != null) {
           // document.removePropertyChangeListener(this);
        }
    }

    @Override
    public void setSymbol(MSymbol sym) {
        super.setSymbol(sym);
        if (document != null) document.setEditedSymbol(sym);
    }
}
