
package microfont.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import microfont.Document;
import microfont.MSymbol;

public class MSymbolEditor extends MAbstractComponent implements MouseListener,
                MouseMotionListener, MouseWheelListener {
    /**  */
    private static final long serialVersionUID = 1L;
    boolean                   changeEnable     = false;
    boolean                   changeSet        = false;

    MSymbolHit                symbolHit        = null;
    int                       prevX, prevY;
    private Document          document;

    public MSymbolEditor(MSymbol symbol) {
        super(symbol);

        pixselSize = 8;
        gridEnable = true;
        marginEnable = true;
        symbolHit = new MSymbolHit();

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
        hit(null, e.getX(), e.getY());
    }

    /** {@inheritDoc} */

    @Override
    public void mousePressed(MouseEvent e) {
        if (!changeEnable) {
            if (e.getButton() == MouseEvent.BUTTON1) changeSet = true;
            else if (e.getButton() == MouseEvent.BUTTON3) changeSet = false;
            else return;

            changeEnable = true;
            if (symbol != null && document != null)
                document.symbolEdit("paint");

            symbolHit = hit(symbolHit, e.getX(), e.getY());
            if ((symbolHit.flags & symbolHit.PIXSEL) != 0) try {
                symbol.setPixsel(symbolHit.column, symbolHit.row, changeSet);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            }
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
        symbolHit = hit(symbolHit, e.getX(), e.getY());
        if ((symbolHit.flags & symbolHit.PIXSEL) != 0
                        && (symbolHit.flags & symbolHit.DEAD_ZONE) == 0
                        && changeEnable) {
            if ((symbolHit.column != prevX) || (symbolHit.row != prevY)) try {
                symbol.setPixsel(symbolHit.column, symbolHit.row, changeSet);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            }
            prevX = symbolHit.column;
            prevY = symbolHit.row;
        }
    }

    /** {@inheritDoc} */

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int count;
        count = pixselSize + e.getWheelRotation();
        if (count < 3) count = 3;
        if (count > 25) count = 25;

        try {
            setPixselSize(count);
        } catch (RenderError e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void paint(Graphics g, int X, int Y) {
        if (symbol == null) return;
        g.clipRect(X, Y, pixselSize * symbol.getWidth(),
                        pixselSize * symbol.getHeight());
        drawSymbol(g, X, Y);
        drawMargins(g, X, Y);
        drawGrid(g, X, Y);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document doc) {
        if (document != null) {
            document.addPropertyChangeListener(this);
        }

        document = doc;

        if (document != null) {
            document.removePropertyChangeListener(this);
        }
    }

    @Override
    public void setSymbol(MSymbol sym) {
        super.setSymbol(sym);
        if (document != null) document.setEditedSymbol(sym);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (document != null && event.getSource() == document) {
            if (event.getPropertyName().equals(Document.PROPERTY_EDITED_SYMBOL))
                super.setSymbol((MSymbol) event.getNewValue());
        }
    }
}
