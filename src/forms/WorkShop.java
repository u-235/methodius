
package forms;

import gui.IButton;
import gui.ICheckBoxMenuItem;
import gui.IMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import utils.resource.Resource;
import logic.Application;
import static logic.Application.*;

public class WorkShop extends JFrame {
    private static final long serialVersionUID = 1L;
    JSplitPane                split;
    JRootPane                 root;

    public WorkShop(ActionMap am) {
        this.setLocationRelativeTo(null);

        root = this.getRootPane();
        root.setActionMap(am);

        this.setLayout(new BorderLayout());

        this.setJMenuBar(doMenuBar(am));
        this.add(doToolBar(am), BorderLayout.NORTH);

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        this.add(split);

        this.add(doStatusBar(am), BorderLayout.SOUTH);
    }

    private JMenuBar doMenuBar(ActionMap am) {
        JMenuBar mb;
        JMenu mFile;
        JMenu mEdit;
        JMenu shift;
        JMenu refl;
        JMenu mView;
        JMenu mTools;
        JMenu mode;
        JMenu mHelp;

        mb = new JMenuBar();

        mFile = new IMenu(application().resource().getString("menubar.file",
                        Resource.TEXT_NAME_KEY));
        mFile.add(am.get(Application.ON_OPEN_FONT));
        mFile.add(am.get(Application.ON_NEW_FONT));
        mFile.addSeparator();
        mFile.add(am.get(Application.ON_SAVE_FONT));
        mFile.add(am.get(Application.ON_SAVE_AS));
        mFile.add(application().files().menu());
        mFile.addSeparator();
        mFile.add(am.get(Application.ON_PROPERTIES));
        mFile.addSeparator();
        mFile.add(am.get(Application.ON_EXIT));
        mb.add(mFile);

        mEdit = new IMenu(application().resource().getString("menubar.edit",
                        Resource.TEXT_NAME_KEY));
        mEdit.add(am.get(Application.ON_UNDO));
        mEdit.add(am.get(Application.ON_REDO));
        mEdit.addSeparator();
        shift = new JMenu(application().resource().getString("shift",
                        Resource.TEXT_NAME_KEY));
        shift.add(am.get(Application.ON_SHIFT_LEFT));
        shift.add(am.get(Application.ON_SHIFT_RIGHT));
        shift.add(am.get(Application.ON_SHIFT_UP));
        shift.add(am.get(Application.ON_SHIFT_DOWN));
        mEdit.add(shift);
        mEdit.addSeparator();
        refl = new JMenu(application().resource().getString("reflect",
                        Resource.TEXT_NAME_KEY));
        refl.add(am.get(Application.ON_REFLECT_HOR));
        refl.add(am.get(Application.ON_REFLECT_VERT));
        mEdit.add(refl);
        mb.add(mEdit);

        mView = new IMenu(application().resource().getString("menubar.view",
                        Resource.TEXT_NAME_KEY));
        mb.add(mView);

        mTools = new IMenu(application().resource().getString("menubar.tools",
                        Resource.TEXT_NAME_KEY));
        mode = new IMenu(application().resource().getString("mode",
                        Resource.TEXT_NAME_KEY));
        mode.add(new ICheckBoxMenuItem(am.get(Application.ON_MODE_POINTER)));
        mode.add(new ICheckBoxMenuItem(am.get(Application.ON_MODE_XPENSIL)));
        mode.add(new ICheckBoxMenuItem(am.get(Application.ON_MODE_PENSIL)));
        mode.add(new ICheckBoxMenuItem(am.get(Application.ON_MODE_RUBER)));
        mTools.add(mode);
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

        buttonBar.add(new IButton(am.get(Application.ON_OPEN_FONT)));
        buttonBar.add(new IButton(am.get(Application.ON_SAVE_FONT)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Application.ON_UNDO)));
        buttonBar.add(new IButton(am.get(Application.ON_REDO)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Application.ON_REFLECT_HOR)));
        buttonBar.add(new IButton(am.get(Application.ON_REFLECT_VERT)));

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(new IButton(am.get(Application.ON_SHIFT_DOWN)));
        buttonBar.add(new IButton(am.get(Application.ON_SHIFT_UP)));
        buttonBar.add(new IButton(am.get(Application.ON_SHIFT_LEFT)));
        buttonBar.add(new IButton(am.get(Application.ON_SHIFT_RIGHT)));
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
