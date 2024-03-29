/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package microfont.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
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

        getSymbolRender().setPixselSize(4);
        getSymbolRender().setSpacing(1);
        getSymbolRender().setColor(ColorIndex.COLOR_PAPER_MARGINS,
                        Color.LIGHT_GRAY);
        getSymbolRender().setColor(ColorIndex.COLOR_INK, Color.BLACK);
        getSymbolRender().setColor(ColorIndex.COLOR_INK_MARGINS,
                        new Color(60, 0, 0));
        getSymbolRender().setColor(ColorIndex.COLOR_PAPER_ASCENT,
                        new Color(224, 224, 224));
        getSymbolRender().setColor(ColorIndex.COLOR_PAPER, Color.WHITE);
        getSymbolRender().setColor(ColorIndex.COLOR_SPACE,
                        new Color(208, 208, 208));
        getSymbolRender().setColor(ColorIndex.COLOR_GRID,
                        new Color(128, 128, 128));
        getSymbolRender().setGridSize(3);
        getSymbolRender().setGridThickness(1);
        getSymbolRender().setDrawGrid(true);
        getSymbolRender().setDrawMargins(true);

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
            return new Dimension(getSymbolRender().getWidth(),
                            getSymbolRender().getHeight());
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int w = (parent.getWidth() - getSymbolRender().getWidth()) / 2;
            int h = (parent.getHeight() - getSymbolRender().getHeight()) / 2;

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
            info = getSymbolRender().getPointInfo(info, e.getX() - renderPos.x,
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
            info = getSymbolRender().getPointInfo(info, e.getX() - renderPos.x,
                            e.getY() - renderPos.y);
            if (control != null) {
                control.mouseMoved(MSymbolEditor.this, e, info);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            info = getSymbolRender().getPointInfo(info, e.getX() - renderPos.x,
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
            info = getSymbolRender().getPointInfo(info, e.getX() - renderPos.x,
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
            info = getSymbolRender().getPointInfo(info, e.getX() - renderPos.x,
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
