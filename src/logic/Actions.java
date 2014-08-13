
package logic;

import gui.ActionX;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import utils.resource.Resource;

public class Actions extends ActionMap {
    /**
     * 
     */
    private static final long serialVersionUID = -448757768836113043L;
    public static final String ON_NEW_FONT               = "font.new";
    public static final String ON_OPEN_FONT              = "font.open";
    public static final String ON_SAVE_FONT              = "font.save";
    public static final String ON_SAVE_AS                = "font.save.as";
    public static final String ON_UNDO                   = "undo";
    public static final String ON_REDO                   = "redo";
    public static final String ON_REFLECT_HOR            = "refl.hor";
    public static final String ON_REFLECT_VERT           = "refl.vert";
    public static final String ON_PROPERTIES             = "font.prop";
    public static final String ON_EXIT                   = "exit";
    public static final String ON_SHIFT_LEFT             = "shift.left";
    public static final String ON_SHIFT_RIGHT            = "shift.right";
    public static final String ON_SHIFT_UP               = "shift.up";
    public static final String ON_SHIFT_DOWN             = "shift.down";
    public static final String ON_SELECTED_SYMBOL_CHANGE = "symbol.change";
    public static final String ON_HEAP_SIZE              = "heap.size";

    Actions(Resource res) {
        put(ON_OPEN_FONT, new ActionX("open", res) {
            /**
             * 
             */
            private static final long serialVersionUID = -8239139805873686115L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application app = Application.application();

                if (!app.checkSaveFont()) return;

                JFileChooser open = app.chooserOpen();

                open.setCurrentDirectory(app.workDir);

                int returnVal = open.showOpenDialog(app.work);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    app.loadMFont(open.getSelectedFile());
                }
            }
        });

        put(ON_NEW_FONT, new ActionX("new", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 2222109098575086243L;

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        put(ON_SAVE_FONT, new ActionX("save", res) {
            /**
             * 
             */
            private static final long serialVersionUID = -1262585621274635667L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().saveFontFile(false);
            }
        });

        put(ON_SAVE_AS, new ActionX("save.as", res) {
            /**
             * 
             */
            private static final long serialVersionUID = -2648970719518831244L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().saveFontFile(true);
            }
        });

        put(ON_EXIT, new ActionX("exit", res) {
            /**
             * 
             */
            private static final long serialVersionUID = -5673304528277289026L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().exit();
            }
        });

        put(ON_UNDO, new ActionX("undo", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 6369536195819891213L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().undo();
            }
        });

        put(ON_REDO, new ActionX("redo", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 8990893922616694875L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().redo();
            }
        });

        put(ON_REFLECT_HOR, new ActionX("reflect.horizontale", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 6042304586480846773L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().reflectHorz();
            }
        });

        put(ON_REFLECT_VERT, new ActionX("reflect.verticale", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 6315966229834932485L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().reflectVert();
            }
        });

        put(ON_SHIFT_LEFT, new ActionX("shift.left", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 5598978355416881202L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().shiftLeft();
            }
        });

        put(ON_SHIFT_RIGHT, new ActionX("shift.right", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 4099663800169439244L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().shiftRight();
            }
        });

        put(ON_SHIFT_UP, new ActionX("shift.up", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 8141910278175809154L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().shiftUp();
            }
        });

        put(ON_SHIFT_DOWN, new ActionX("shift.down", res) {
            /**
             * 
             */
            private static final long serialVersionUID = 7199246162733501462L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().shiftDown();
            }
        });

        put(ON_PROPERTIES, new ActionX("properties", res) {
            /**
             * 
             */
            private static final long serialVersionUID = -2238831501425198088L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().showProperties();
            }
        });

        put(ON_SELECTED_SYMBOL_CHANGE, new AbstractAction() {
            /**
             * 
             */
            private static final long serialVersionUID = 7805430753742616L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Application.application().editPanel.setMSymbol(Application
                                .application().fontPanel.getSelectedSymbol());
            }
        });
    }
}
