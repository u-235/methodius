
package microfont.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import microfont.AbstractPixselMap;
import microfont.Document;
import microfont.MSymbol;
import microfont.PixselMap;
import microfont.render.ColorIndex;
import microfont.render.PointInfo;

public class MSymbolEditor extends AbstractView {
    /**  */
    private static final long serialVersionUID = 1L;
    protected MouseHandler    handler;
    protected Editor          control;
    private Document          document;

    public MSymbolEditor() {
        super();
        handler = new MouseHandler();
        setLayout(new MSymbolEditorLayout());

        setPixselSize(4);
        setSpacing(1);
        setColor(ColorIndex.COLOR_PAPER_MARGINS, Color.LIGHT_GRAY);
        setColor(ColorIndex.COLOR_INK, Color.BLACK);
        setColor(ColorIndex.COLOR_INK_MARGINS, new Color(60, 0, 0));
        setColor(ColorIndex.COLOR_PAPER_ASCENT, new Color(224, 224, 224));
        setColor(ColorIndex.COLOR_PAPER, Color.WHITE);
        setColor(ColorIndex.COLOR_SPACE, new Color(208, 208, 208));
        setColor(ColorIndex.COLOR_GRID, new Color(128, 128, 128));
        setGridSize(3);
        setGridThickness(1);
        setDrawGrid(true);
        setDrawMargins(true);

        addMouseListener(handler);
        addMouseMotionListener(handler);
        addMouseWheelListener(handler);
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

    protected class MSymbolEditorLayout implements LayoutManager {
        public MSymbolEditorLayout() {
            //
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeLayoutComponent(Component comp) {
            // TODO Auto-generated method stub

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(render().getWidth(), render().getHeight());
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int w = (parent.getWidth() - render().getWidth()) / 2;
            int h = (parent.getHeight() - render().getHeight()) / 2;

            renderPos.x = w < 0 ? 0 : w;
            renderPos.y = h < 0 ? 0 : h;
        }
    }

    protected class MouseHandler implements MouseListener, MouseMotionListener,
                    MouseWheelListener {
        PointInfo info;
        boolean   paint;

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            info = render().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mouseDragged(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = getPixselMap();
                if (apm instanceof PixselMap) {
                    PixselMap pm = (PixselMap) apm;
                    pm.setPixsel(info.getX(), info.getY(), paint);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            info = render().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mouseMoved(MSymbolEditor.this, e, info);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            info = render().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mouseClicked(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = getPixselMap();
                if (apm instanceof PixselMap) {
                    PixselMap pm = (PixselMap) apm;
                    if (document != null)
                        document.symbolEdit("paint", (MSymbol) pm);
                    if (e.getButton() == MouseEvent.BUTTON1)
                        pm.setPixsel(info.getX(), info.getY(), true);
                    if (e.getButton() == MouseEvent.BUTTON3)
                        pm.setPixsel(info.getX(), info.getY(), false);
                    if (document != null) document.endEdit();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            info = render().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mousePressed(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = getPixselMap();
                if (apm instanceof PixselMap) {
                    if (e.getButton() == MouseEvent.BUTTON1) paint = true;
                    if (e.getButton() == MouseEvent.BUTTON3) paint = false;

                    PixselMap pm = (PixselMap) apm;
                    if (document != null)
                        document.symbolEdit("paint", (MSymbol) pm);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            info = render().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mouseReleased(MSymbolEditor.this, e, info);
            } else if (document != null) document.endEdit();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //
        }
    }
}
