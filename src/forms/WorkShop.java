package forms;

import gui.IButton;
import gui.ICheckBoxMenuItem;
import gui.IMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import utils.resource.Resource;

import logic.Application;

public class WorkShop extends JFrame
{
    private static final long serialVersionUID = 1L;
    JSplitPane     split;
    public boolean exit;

    public WorkShop() {
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Application.checkSaveFont()) dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                exit = true;
            }
        });

        this.setLayout(new BorderLayout());

        this.setJMenuBar(doMenuBar());
        this.add(doToolBar(), BorderLayout.NORTH);

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        this.add(split);

        this.add(doStatusBar(), BorderLayout.SOUTH);
    }

    private JMenuBar doMenuBar() {
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

        mFile = new IMenu(Application.res.getString("menubar.file",
                        Resource.TEXT_NAME_KEY));
        mFile.add(Application.actOpen);
        mFile.add(Application.actNew);
        mFile.addSeparator();
        mFile.add(Application.actSave);
        mFile.add(Application.actSaveAs);
        mFile.addSeparator();
        mFile.add(Application.actProperties);
        mFile.addSeparator();
        mFile.add(Application.actExit);
        mb.add(mFile);

        mEdit = new IMenu(Application.res.getString("menubar.edit",
                        Resource.TEXT_NAME_KEY));
        mEdit.add(Application.actUndo);
        mEdit.add(Application.actRedo);
        mEdit.addSeparator();
        shift = new JMenu(Application.res.getString("shift",
                        Resource.TEXT_NAME_KEY));
        shift.add(Application.actShiftLeft);
        shift.add(Application.actShiftRight);
        shift.add(Application.actShiftUp);
        shift.add(Application.actShiftDown);
        mEdit.add(shift);
        mEdit.addSeparator();
        refl = new JMenu(Application.res.getString("reflect",
                        Resource.TEXT_NAME_KEY));
        refl.add(Application.actReflectHorz);
        refl.add(Application.actReflectVert);
        mEdit.add(refl);
        mb.add(mEdit);

        mView = new IMenu(Application.res.getString("menubar.view",
                        Resource.TEXT_NAME_KEY));
        mb.add(mView);

        mTools = new IMenu(Application.res.getString("menubar.tools",
                        Resource.TEXT_NAME_KEY));
        mode = new IMenu(Application.res.getString("mode",
                        Resource.TEXT_NAME_KEY));
        mode.add(new ICheckBoxMenuItem(Application.actModePointer));
        mode.add(new ICheckBoxMenuItem(Application.actModeXPensil));
        mode.add(new ICheckBoxMenuItem(Application.actModePensil));
        mode.add(new ICheckBoxMenuItem(Application.actModeRuber));
        mTools.add(mode);
        mb.add(mTools);

        mHelp = new IMenu(Application.res.getString("menubar.help",
                        Resource.TEXT_NAME_KEY));
        mb.add(mHelp);

        return mb;
    }

    private JToolBar doToolBar() {
        JToolBar buttonBar;
        JButton btnOpen;
        JButton btnSave;
        JButton btnUndo;
        JButton btnRedo;
        JButton btnReflHorz;
        JButton btnReflVert;
        JButton btnDown;
        JButton btnUp;
        JButton btnLeft;
        JButton btnRight;

        buttonBar = new JToolBar();
        buttonBar.setFloatable(false);

        btnOpen = new IButton(Application.actOpen);
        btnSave = new IButton(Application.actSave);

        btnReflHorz = new IButton(Application.actReflectHorz);
        btnReflVert = new IButton(Application.actReflectVert);

        btnUndo = new IButton(Application.actUndo);
        btnRedo = new IButton(Application.actRedo);

        btnDown = new IButton(Application.actShiftDown);
        btnUp = new IButton(Application.actShiftUp);
        btnLeft = new IButton(Application.actShiftLeft);
        btnRight = new IButton(Application.actShiftRight);

        buttonBar.add(btnOpen);
        buttonBar.add(btnSave);

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(btnUndo);
        buttonBar.add(btnRedo);

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(btnReflHorz);
        buttonBar.add(btnReflVert);

        buttonBar.add(new JToolBar.Separator());

        buttonBar.add(btnDown);
        buttonBar.add(btnUp);
        buttonBar.add(btnLeft);
        buttonBar.add(btnRight);
        return buttonBar;
    }

    private JToolBar doStatusBar() {
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

        JLabel lblUsage = new JLabel();
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
