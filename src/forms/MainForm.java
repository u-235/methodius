package forms;

import gui.ActionX;
import gui.IButton;
import gui.ICheckBoxMenuItem;
import gui.IMenu;
import gui.IToggleButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.resource.Resource;

import logic.Application;
import microfont.MFont;
import microfont.MFontLoadSave;
import microfont.MSymbol;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;
import microfont.gui.MListModel;
import microfont.gui.MSymbolCellRenderer;
import microfont.gui.MSymbolEditor;
import microfont.gui.MSymbolView;

public class MainForm
{
    private Resource         res;

    private int              mode;

    /* */
    private OnFontChange     atFontChange;
    private ActionX          actNew;
    private ActionX          actOpen;
    private ActionX          actSave;
    private ActionX          actSaveAs;

    private ActionX          actUndo;
    private ActionX          actRedo;

    private ActionX          actReflectHorz;
    private ActionX          actReflectVert;

    private ActionX          actProperties;
    private ActionX          actExit;

    private ActionX          actShiftLeft;
    private ActionX          actShiftRight;
    private ActionX          actShiftUp;
    private ActionX          actShiftDown;

    private ActionX          actModePointer;
    private ActionX          actModeXPensil;
    private ActionX          actModePensil;
    private ActionX          actModeRuber;

    public JFrame            frame;

    File                     fontFile;
    String                   fontName = "new font";
    boolean                  fontSaved;
    JToggleButton            pensil;
    JToggleButton            ruber;
    MSymbolEditor            edit;
    MSymbolView              view;
    JList                    list;
    private ListModel        listModel;
    private ListCellRenderer listRender;
    public JLabel            lblUsage;
    private IMenu            mStyle;
    FontProperties           fpf;
    JFileChooser             chooserSave;
    JFileChooser             chooserOpen;
    protected int            listIndex;
    public boolean           exit;

    private class OnFontChange implements MFontListener
    {
        @Override
        public void mFontEvent(MFontEvent change){            
            System.out.println(change.toString() + " "
                            + change.getReasonString() + " index="
                            + change.getIndex());
            setSaved(false);
        }
    }

    public class OnNew extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnNew() {
            super("new", res);
            System.out.println("New action");
            System.out.flush();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public class OnOpen extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnOpen() {
            super("open", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File file;
            MFont font;

            if (!checkSaveFont()) return;

            if (chooserOpen == null) {
                chooserOpen = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                "MicroFont", "mfnt");
                chooserOpen.setFileFilter(filter);
                chooserOpen.setDialogTitle("Open font.");
            }

            int returnVal = chooserOpen.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) file = chooserOpen
                            .getSelectedFile();
            else return;

            if (file == null) return;

            try {
                font = MFontLoadSave.load(file, null);
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Can't open file.",
                                "Error", JOptionPane.OK_OPTION);
                return;
            }

            if (font == null)
                JOptionPane.showMessageDialog(null, "Error on load font.",
                                "Error", JOptionPane.OK_OPTION);
            fontFile = file;
            setMFont(font);
        }
    }

    public class OnSave extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnSave() {
            super("save", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            saveFontFile(false);
        }
    }

    public class OnSaveAs extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnSaveAs() {
            super("save.as", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            saveFontFile(true);
        }
    }

    public class OnUndo extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnUndo() {
            super("undo", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public class OnRedo extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnRedo() {
            super("redo", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public class OnReflectHorz extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnReflectHorz() {
            super("reflect.horizontale", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().reflectHorizontale();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnReflectVert extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnReflectVert() {
            super("reflect.verticale", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().reflectVerticale();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnShiftLeft extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftLeft() {
            super("shift.left", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().shiftLeft();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnShiftRight extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftRight() {
            super("shift.right", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().shiftRight();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnShiftUp extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftUp() {
            super("shift.up", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().shiftUp();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnShiftDown extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftDown() {
            super("shift.down", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                edit.getSymbol().shiftDown();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public class OnExit extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnExit() {
            super("exit", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkSaveFont()) frame.dispose();
        }

    }

    public class OnProperties extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnProperties() {
            super("properties", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MFont f, c;
            if (fpf == null) fpf = new FontProperties(frame, res);
            f = ((MListModel) list.getModel()).getFont();
            if (f == null) return;
            c = f.clone();
            if (fpf.start(c) == FontProperties.ACTION_OK) f.copy(c);
            fpf.setMFont(null);
        }

    }

    public class OnModeXPensil extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnModeXPensil() {
            super("mode.xpensil", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = 1;
            updateButtonMode();
        }
    }

    public class OnModePensil extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnModePensil() {
            super("mode.pensil", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = 2;
            updateButtonMode();
        }
    }

    public class OnModeRuber extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnModeRuber() {
            super("mode.ruber", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = 3;
            updateButtonMode();
        }
    }

    public class OnModePointer extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnModePointer() {
            super("mode.pointer", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = 0;
            updateButtonMode();
        }
    }

    void setMFont(MFont font) {
        int i = list.getSelectedIndex();
        MListModel model = (MListModel) list.getModel();

        MFont old = model.getFont();
        if (old != null) old.removeListener(atFontChange);
        if (font != null) font.addListener(atFontChange);

        model.setFont(font);
        list.setModel(model);
        list.setSelectedIndex(i);
        fontName = font.getName();
        updateTitle();
        setSaved(true);
    }

    private boolean checkSaveFont() {
        int r;
        if (fontSaved) return true;

        r = JOptionPane.showConfirmDialog(
                        null,
                        new String[] { "Current file has changed, but not saved.\nSave it?\nAlso your may cancel this operation." },
                        "alert", JOptionPane.YES_NO_CANCEL_OPTION);

        if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION)
            return false;
        if (r == JOptionPane.YES_OPTION) return saveFontFile(false);
        return true;
    }

    private File getSaveFile() {
        File ret = null;
        if (chooserSave == null) {
            chooserSave = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "MicroFont", "mfnt");
            chooserSave.setFileFilter(filter);
            chooserSave.setDialogTitle("Save font as.");
            chooserSave.setAcceptAllFileFilterUsed(false);
            chooserSave.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        while (true) {
            int returnVal = chooserSave.showSaveDialog(frame);
            ret = chooserSave.getSelectedFile();
            if (returnVal != JFileChooser.APPROVE_OPTION) return null;
            ret = chooserSave.getSelectedFile();
            if (!ret.exists()) break;
            returnVal = JOptionPane.showConfirmDialog(null,
                            "Selected file already exsist.\nOwerride it?",
                            "warning", JOptionPane.YES_NO_OPTION);
            if (returnVal == JOptionPane.YES_OPTION) break;
        }

        // Check file extensions
        String name = ret.getName();
        int i = name.lastIndexOf('.');
        if ((i > 0) && (i + 1 < name.length())
                        && (name.substring(i + 1).equalsIgnoreCase("mfnt")))
            name = name.substring(0, i);
        name += ".mfnt";
        ret = new File(ret.getParentFile(), name);

        return ret;
    }

    private boolean saveFontFile(boolean saveAs) {
        File file;

        if (fontFile == null) saveAs = true;
        else if (fontFile.exists() && !fontFile.canWrite()) saveAs = true;

        if (saveAs) {
            file = getSaveFile();
            if (file == null) return false;
            fontFile = file;
        }

        try {
            MFontLoadSave.save(((MListModel) list.getModel()).getFont(),
                            fontFile);
        }
        catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Can't save file.", "Error",
                            JOptionPane.OK_OPTION);
            return false;
        }

        setSaved(true);
        return true;
    }

    void setSaved(boolean saved) {
        boolean old = fontSaved;

        if (old != saved) {
            fontSaved = saved;
            actSave.setEnabled(!fontSaved);
        }
    }

    void updateButtonMode() {
        if (mode == 0) actModePointer.setSelected(true);
        else actModePointer.setSelected(false);

        if (mode == 1) actModeXPensil.setSelected(true);
        else actModeXPensil.setSelected(false);

        if (mode == 2) actModePensil.setSelected(true);
        else actModePensil.setSelected(false);

        if (mode == 3) actModeRuber.setSelected(true);
        else actModeRuber.setSelected(false);
    }

    void updateMenuStyle() {
        class MenuListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem item;

                item = (JMenuItem) e.getSource();

                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                .getInstalledLookAndFeels()) {
                    if (item.getText() == (info.getName())) {
                        try {
                            javax.swing.UIManager.setLookAndFeel(info
                                            .getClassName());
                            SwingUtilities.updateComponentTreeUI(frame);
                        }
                        catch (Exception e1) {
                        }
                        break;
                    }
                }
            }
        }

        ICheckBoxMenuItem item;
        MenuListener ml;

        mStyle.removeAll();
        ml = new MenuListener();

        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                        .getInstalledLookAndFeels()) {
            item = new ICheckBoxMenuItem(info.getName());
            item.addActionListener(ml);
            if (javax.swing.UIManager.getLookAndFeel().getName() == info
                            .getName()) item.setSelected(true);
            mStyle.add(item);
        }
    }

    public MainForm() {
        res = new Resource("locale/MainForm");

        atFontChange = new OnFontChange();
        actOpen = new OnOpen();
        actNew = new OnNew();
        actSave = new OnSave();
        actSaveAs = new OnSaveAs();
        actExit = new OnExit();
        actUndo = new OnUndo();
        actRedo = new OnRedo();
        actReflectHorz = new OnReflectHorz();
        actReflectVert = new OnReflectVert();
        actShiftLeft = new OnShiftLeft();
        actShiftRight = new OnShiftRight();
        actShiftUp = new OnShiftUp();
        actShiftDown = new OnShiftDown();
        actProperties = new OnProperties();
        actModeXPensil = new OnModeXPensil();
        actModePensil = new OnModePensil();
        actModeRuber = new OnModeRuber();
        actModePointer = new OnModePointer();

        setSaved(true);

        doForm();
    }

    void updateTitle() {
        String title = Application.NAME;
        if (fontName != null) title += " : " + fontName;
        if (fontFile != null) title += " (" + fontFile.getName() + ")";
        frame.setTitle(title);
    }

    private void doForm() {
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (checkSaveFont()) frame.dispose();
                // TODO Update and save settings.
            }

            @Override
            public void windowClosed(WindowEvent e) {
                exit = true;
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }
        });

        frame.setLayout(new BorderLayout());

        frame.setJMenuBar(doMenuBar());
        frame.add(doToolBar(), BorderLayout.NORTH);

        frame.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, doLeftPanel(),
                        doRightPanel()), BorderLayout.CENTER);

        frame.add(doStatusBar(), BorderLayout.SOUTH);

        list.setSelectedIndex(0);
        onSelect(list);

        updateTitle();

        frame.pack();
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

        mFile = new IMenu(res.getString("menubar.file", Resource.TEXT_NAME_KEY));
        mFile.add(actOpen);
        mFile.add(actNew);
        mFile.addSeparator();
        mFile.add(actSave);
        mFile.add(actSaveAs);
        mFile.addSeparator();
        mFile.add(actProperties);
        mFile.addSeparator();
        mFile.add(actExit);
        mb.add(mFile);

        mEdit = new IMenu(res.getString("menubar.edit", Resource.TEXT_NAME_KEY));
        mEdit.add(actUndo);
        mEdit.add(actRedo);
        mEdit.addSeparator();
        shift = new JMenu(res.getString("shift", Resource.TEXT_NAME_KEY));
        shift.add(actShiftLeft);
        shift.add(actShiftRight);
        shift.add(actShiftUp);
        shift.add(actShiftDown);
        mEdit.add(shift);
        mEdit.addSeparator();
        refl = new JMenu(res.getString("reflect", Resource.TEXT_NAME_KEY));
        refl.add(actReflectHorz);
        refl.add(actReflectVert);
        mEdit.add(refl);
        mb.add(mEdit);

        mView = new IMenu(res.getString("menubar.view", Resource.TEXT_NAME_KEY));
        mStyle = new IMenu(res.getString("menu.view.style",
                        Resource.TEXT_NAME_KEY));
        updateMenuStyle();
        mView.add(mStyle);
        mb.add(mView);

        mTools = new IMenu(res.getString("menubar.tools",
                        Resource.TEXT_NAME_KEY));
        mode = new IMenu(res.getString("mode", Resource.TEXT_NAME_KEY));
        mode.add(new ICheckBoxMenuItem(actModePointer));
        mode.add(new ICheckBoxMenuItem(actModeXPensil));
        mode.add(new ICheckBoxMenuItem(actModePensil));
        mode.add(new ICheckBoxMenuItem(actModeRuber));
        mTools.add(mode);
        mb.add(mTools);

        mHelp = new IMenu(res.getString("menubar.help", Resource.TEXT_NAME_KEY));
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

        btnOpen = new IButton(actOpen);
        btnSave = new IButton(actSave);

        btnReflHorz = new IButton(actReflectHorz);
        btnReflVert = new IButton(actReflectVert);

        btnUndo = new IButton(actUndo);
        btnRedo = new IButton(actRedo);

        btnDown = new IButton(actShiftDown);
        btnUp = new IButton(actShiftUp);
        btnLeft = new IButton(actShiftLeft);
        btnRight = new IButton(actShiftRight);

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

    public JPanel doRightPanel() {
        JPanel panel;
        JToolBar tools;
        IToggleButton btnXPensil, btnPensil, btnRuber, btnPointer;

        panel = new JPanel();
        panel.setMaximumSize(new Dimension(100000, 1000));
        panel.setLayout(new BorderLayout());

        tools = new JToolBar(JToolBar.VERTICAL);
        tools.setFloatable(false);

        edit = new MSymbolEditor();

        btnPointer = new IToggleButton(actModePointer);
        btnXPensil = new IToggleButton(actModeXPensil);
        btnPensil = new IToggleButton(actModePensil);
        btnRuber = new IToggleButton(actModeRuber);
        updateButtonMode();

        tools.add(btnPointer);
        tools.add(btnXPensil);
        tools.add(btnPensil);
        tools.add(btnRuber);
        tools.add(new JToolBar.Separator());

        panel.add(tools, BorderLayout.WEST);
        panel.add(new JScrollPane(edit), BorderLayout.CENTER);

        return panel;
    }

    public JPanel doLeftPanel() {
        JPanel panel;
        LayoutManager2 pLay;

        panel = new JPanel();
        pLay = new BorderLayout();
        panel.setLayout(pLay);

        view = new MSymbolView();

        list = new JList();
        listModel = new MListModel();
        listRender = new MSymbolCellRenderer();
        list.setModel(listModel);
        list.setCellRenderer(listRender);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                onSelect(((JList) e.getSource()));
            }
        });
        list.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                JList lst = (JList) e.getSource();
                listIndex = lst.locationToIndex(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        JPopupMenu pMenu = new JPopupMenu();
        pMenu.add("Copy to editor");
        pMenu.add("Copy");
        pMenu.addSeparator();
        pMenu.add(actProperties);
        list.setComponentPopupMenu(pMenu);

        panel.add(view, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
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

        lblUsage = new JLabel();
        statusBar.add(lblUsage);

        return statusBar;
    }

    void onSelect(JList l) {
        MSymbol s;

        listIndex = l.getSelectedIndex();
        s = (MSymbol) listModel.getElementAt(listIndex);
        view.setSymbol(s);
        edit.setSymbol(s);
    }
}
