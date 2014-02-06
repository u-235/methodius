
package microfont.gui;

import java.awt.Color;
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
import microfont.render.PixselMapRender;
import microfont.render.PointInfo;

public class MSymbolEditor extends AbstractView {
    /**  */
    private static final long serialVersionUID = 1L;
    MouseHandler              handler;
    Editor                    control;
    private Document          document;

    public MSymbolEditor() {
        super(1);
        handler = new MouseHandler();
        setLayout(new MSymbolEditorLayout(this));

        PixselMapRender render = render();
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

    private class MouseHandler implements MouseListener, MouseMotionListener,
                    MouseWheelListener {
        Rectangle rect;
        PointInfo info;
        boolean paint;

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            rect = elementPosition(ELEMENT_INDEX_RENDER);
            info = render().getPointInfo(info, e.getX() - rect.x,
                            e.getY() - rect.y);
            if (control != null) {
                control.mouseDragged(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = render().getPixselMap();
                if (apm instanceof PixselMap) {
                    PixselMap pm = (PixselMap) apm;
                    pm.setPixsel(info.getX(), info.getY(), paint);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            rect = elementPosition(ELEMENT_INDEX_RENDER);
            info = render().getPointInfo(info, e.getX() - rect.x,
                            e.getY() - rect.y);
            if (control != null) {
                control.mouseMoved(MSymbolEditor.this, e, info);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            rect = elementPosition(ELEMENT_INDEX_RENDER);
            info = render().getPointInfo(info, e.getX() - rect.x,
                            e.getY() - rect.y);
            if (control != null) {
                control.mouseClicked(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = render().getPixselMap();
                if (apm instanceof PixselMap) {
                    PixselMap pm = (PixselMap) apm;
                    if (document != null)
                        document.symbolEdit("paint", (MSymbol) pm);
                    if (e.getButton() == MouseEvent.BUTTON1)
                        pm.setPixsel(info.getX(), info.getY(), true);
                    if (e.getButton() == MouseEvent.BUTTON3)
                        pm.setPixsel(info.getX(), info.getY(), false);
                    if (document != null)
                        document.endEdit();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            rect = elementPosition(ELEMENT_INDEX_RENDER);
            info = render().getPointInfo(info, e.getX() - rect.x,
                            e.getY() - rect.y);
            if (control != null) {
                control.mousePressed(MSymbolEditor.this, e, info);
            } else {
                AbstractPixselMap apm = render().getPixselMap();
                if (apm instanceof PixselMap) {
                    if (e.getButton() == MouseEvent.BUTTON1)
                       paint= true;
                    if (e.getButton() == MouseEvent.BUTTON3)
                        paint= false;

                    PixselMap pm = (PixselMap) apm;
                    if (document != null)
                        document.symbolEdit("paint", (MSymbol) pm);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            rect = elementPosition(ELEMENT_INDEX_RENDER);
            info = render().getPointInfo(info, e.getX() - rect.x,
                            e.getY() - rect.y);
            if (control != null) {
                control.mouseReleased(MSymbolEditor.this, e, info);
            } else if (document != null) document.endEdit();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
