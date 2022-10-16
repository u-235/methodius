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

package forms;

import static logic.Application.application;
import gui.IButton;
import gui.IMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import logic.Actions;
import logic.Application;
import utils.config.ConfigNode;
import utils.resource.Resource;

public class WorkShop extends JFrame {
    private static final long serialVersionUID = 1L;
    JSplitPane                split;
    JRootPane                 root;
    ConfigNode                config;

    public WorkShop(ActionMap am) {
        config = Application.application().config().node("/frame");
        config.putComment(null);
        config.putComment("bounds", " Position and size main window.\n"
                        + " x y width height.");
        config.putComment(
                        "state",
                        " State main window; most be one of following:\n"
                                        + " normal iconic maximized horiz vert.");
        config.putComment("horiz", " Position of horizontal split.");
        config.putComment("vert", " Position of vertical split.");

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (config == null) return;

                String state;
                switch (e.getNewState()) {
                case Frame.ICONIFIED:
                    state = "iconic";
                    break;
                case Frame.MAXIMIZED_BOTH:
                    state = "maximized";
                    break;
                case Frame.MAXIMIZED_HORIZ:
                    state = "horiz";
                    break;
                case Frame.MAXIMIZED_VERT:
                    state = "vert";
                    break;
                default:
                    state = "normal";
                }
                config.put("state", state);
            }
        });

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                fixBounds();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                fixBounds();
            }

            void fixBounds() {
                if (config == null) return;
                if (WorkShop.this.getExtendedState() == NORMAL)
                    config.putRectangle("bounds", getBounds());
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // nop
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // nop
            }
        });

        // this.setLocationRelativeTo(null);

        root = getRootPane();
        root.setActionMap(am);

        this.setLayout(new BorderLayout());

        this.setJMenuBar(doMenuBar(am));
        this.add(doToolBar(am), BorderLayout.NORTH);

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        // split.setOneTouchExpandable(true);
        split.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (config == null) return;

                if (evt.getPropertyName().equals(
                                JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    config.putInt("horiz", split.getDividerLocation());
                }
            }
        });
        this.add(split);

        this.add(doStatusBar(am), BorderLayout.SOUTH);
    }

    @Override
    public void pack() {
        Rectangle pos = config.getRectangle("bounds", null);

        if (pos == null) {
            super.pack();
        } else {
            setBounds(pos);
            String state = config.get("state", "normal");
            if (state.equals("iconic")) setExtendedState(ICONIFIED);
            else if (state.equals("maximized")) setExtendedState(MAXIMIZED_BOTH);
            else if (state.equals("horiz")) setExtendedState(MAXIMIZED_HORIZ);
            else if (state.equals("vert")) setExtendedState(MAXIMIZED_VERT);
            else setState(NORMAL);

            split.setDividerLocation(config.getInt("horiz", -1));
        }

    }

    private JMenuBar doMenuBar(ActionMap am) {
        JMenuBar mb;
        JMenu mFile;
        JMenu mEdit;
        JMenu shift;
        JMenu refl;
        JMenu mView;
        JMenu mTools;
        JMenu mHelp;

        mb = new JMenuBar();

        mFile = new IMenu(application().resource().getString("menubar.file",
                        Resource.TEXT_NAME_KEY));
        mFile.add(am.get(Actions.ON_OPEN_FONT));
        mFile.add(am.get(Actions.ON_NEW_FONT));
        mFile.addSeparator();
        mFile.add(am.get(Actions.ON_SAVE_FONT));
        mFile.add(am.get(Actions.ON_SAVE_AS));
        mFile.add(application().recent().menu());
        mFile.addSeparator();
        mFile.add(am.get(Actions.ON_PROPERTIES));
        mFile.addSeparator();
        mFile.add(am.get(Actions.ON_EXIT));
        mb.add(mFile);

        mEdit = new IMenu(application().resource().getString("menubar.edit",
                        Resource.TEXT_NAME_KEY));
        mEdit.add(am.get(Actions.ON_UNDO));
        mEdit.add(am.get(Actions.ON_REDO));
        mEdit.addSeparator();
        shift = new JMenu(application().resource().getString("shift",
                        Resource.TEXT_NAME_KEY));
        shift.add(am.get(Actions.ON_SHIFT_LEFT));
        shift.add(am.get(Actions.ON_SHIFT_RIGHT));
        shift.add(am.get(Actions.ON_SHIFT_UP));
        shift.add(am.get(Actions.ON_SHIFT_DOWN));
        mEdit.add(shift);
        mEdit.addSeparator();
        refl = new JMenu(application().resource().getString("reflect",
                        Resource.TEXT_NAME_KEY));
        refl.add(am.get(Actions.ON_REFLECT_HOR));
        refl.add(am.get(Actions.ON_REFLECT_VERT));
        mEdit.add(refl);
        mb.add(mEdit);

        mView = new IMenu(application().resource().getString("menubar.view",
                        Resource.TEXT_NAME_KEY));
        mb.add(mView);

        mTools = new IMenu(application().resource().getString("menubar.tools",
                        Resource.TEXT_NAME_KEY));
        mb.add(mTools);

        mHelp = new IMenu(application().resource().getString("menubar.help",
                        Resource.TEXT_NAME_KEY));
        mb.add(mHelp);

        return mb;
    }

    private JToolBar doToolBar(ActionMap am) {
        JToolBar buttonBar;

        buttonBar = new JToolBar();
        buttonBar.setFloatable(false);

        buttonBar.add(new IButton(am.get(Actions.ON_OPEN_FONT)));
        buttonBar.add(new IButton(am.get(Actions.ON_SAVE_FONT)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Actions.ON_UNDO)));
        buttonBar.add(new IButton(am.get(Actions.ON_REDO)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Actions.ON_REFLECT_HOR)));
        buttonBar.add(new IButton(am.get(Actions.ON_REFLECT_VERT)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Actions.ON_SHIFT_DOWN)));
        buttonBar.add(new IButton(am.get(Actions.ON_SHIFT_UP)));
        buttonBar.add(new IButton(am.get(Actions.ON_SHIFT_LEFT)));
        buttonBar.add(new IButton(am.get(Actions.ON_SHIFT_RIGHT)));
        return buttonBar;
    }

    private JToolBar doStatusBar(ActionMap am) {
        JToolBar statusBar;
        IButton btnGC;

        statusBar = new JToolBar();
        statusBar.setFloatable(false);

        btnGC = new IButton("GC");
        btnGC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runtime.getRuntime().gc();
            }
        });

        statusBar.add(btnGC);

        statusBar.add(new JToolBar.Separator());

        final JLabel lblUsage = new JLabel();
        am.put(Application.ON_HEAP_SIZE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblUsage.setText(e.getActionCommand());
            }
        });
        statusBar.add(lblUsage);

        return statusBar;
    }

    public void setLeft(Component comp) {
        split.setLeftComponent(comp);
    }

    public void setRight(Component comp) {
        split.setRightComponent(comp);
    }
}
