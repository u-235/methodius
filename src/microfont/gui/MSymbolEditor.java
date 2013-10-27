package microfont.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import microfont.MSymbol;

public class MSymbolEditor extends MAbstractComponent implements MouseListener,
                MouseMotionListener, MouseWheelListener
{
    /**  */
    private static final long serialVersionUID = 1L;
    boolean                   changeEnable     = false;
    boolean                   changeSet        = false;

    MSymbolHit                symbolHit        = null;
    int                       prevX, prevY;

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
        // requestFocus();
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
            symbolHit = hit(symbolHit, e.getX(), e.getY());
            if ((symbolHit.flags & symbolHit.PIXSEL) != 0)
                try {
                    symbol.changePixsel(symbolHit.column, symbolHit.row,
                                    changeSet);
                }
                catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                }
        }
    }

    /** {@inheritDoc} */

    @Override
    public void mouseReleased(MouseEvent e) {
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
            if ((symbolHit.column != prevX) || (symbolHit.row != prevY))
                try {
                    symbol.changePixsel(symbolHit.column, symbolHit.row,
                                    changeSet);
                }
                catch (IllegalArgumentException e1) {
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

        pixselSize = count;
        pixselSize = count;
        revalidate();
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
}
