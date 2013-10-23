package logic;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.resource.Resource;

import microfont.MFont;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;
import microfont.ls.MFontLoadSave;
import forms.EditPanel;
import forms.FontPanel;
import forms.FontProperties;
import forms.WorkShop;
import gui.ActionX;

public class Application
{
    public static final String   NAME      = "Mifodius";
    public static final int      VER_MAJOR = 0;
    public static final int      VER_MINOR = 8;

    public static Resource       res;
    static File                  fontFile;
    static String                       fontName  = "new font";
    static boolean               fontSaved;

    static WorkShop              work;
    static FontPanel             fontPanel;
    static EditPanel             editPanel;
    static JFileChooser          chooserSave;
    static JFileChooser          chooserOpen;
    static FontProperties        fpf;

    private static MFont         font;
    private static MFontListener atFontChange;
    public static ActionX        actNew;
    public static ActionX        actOpen;
    public static ActionX        actSave;
    public static ActionX        actSaveAs;
    public static ActionX        actUndo;
    public static ActionX        actRedo;
    public static ActionX        actReflectHorz;
    public static ActionX        actReflectVert;
    public static ActionX        actProperties;
    public static ActionX        actExit;
    public static ActionX        actShiftLeft;
    public static ActionX        actShiftRight;
    public static ActionX        actShiftUp;
    public static ActionX        actShiftDown;
    public static ActionX        actModePointer;
    public static ActionX        actModeXPensil;
    public static ActionX        actModePensil;
    public static ActionX        actModeRuber;
    private static int           mode;

    public static void run() {
        if (work != null) return;
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

        work = new WorkShop();
        editPanel = new EditPanel();
        fontPanel = new FontPanel();
        work.setLeft(fontPanel);
        work.setRight(editPanel);

        updateTitle();
        setSaved(true);

        work.pack();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                work.setVisible(true);
                System.out.println("gui thread pririy is "
                                + Thread.currentThread().getPriority());
            }
        });

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        System.out.println("back thread pririy is "
                        + Thread.currentThread().getPriority());
        System.out.println("do open dialog");
        doChooserOpen();
        System.out.println("do save dialog");
        doChooserSave();
    }

    static synchronized void doChooserOpen() {
        if (chooserOpen != null) return;
        chooserOpen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "MicroFont", "mfnt");
        chooserOpen.setFileFilter(filter);
        chooserOpen.setDialogTitle("Open font.");
    }

    static synchronized void doChooserSave() {
        if (chooserSave == null) {
            chooserSave = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "MicroFont", "mfnt");
            chooserSave.setFileFilter(filter);
            chooserSave.setDialogTitle("Save font as.");
            chooserSave.setAcceptAllFileFilterUsed(false);
            chooserSave.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }

    static void setMFont(MFont newFont) {
        if (font != null) font.removeListener(atFontChange);
        if (newFont != null) newFont.addListener(atFontChange);
        font = newFont;

        fontPanel.setMFont(font);
        updateTitle();
        setSaved(true);
    }

    public static boolean checkSaveFont() {
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

    private static File getSaveFile() {
        File ret = null;
        doChooserSave();
        while (true) {
            int returnVal = chooserSave.showSaveDialog(work);
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

    private static boolean saveFontFile(boolean saveAs) {
        File file;

        if (fontFile == null) saveAs = true;
        else if (fontFile.exists() && !fontFile.canWrite()) saveAs = true;

        if (saveAs) {
            file = getSaveFile();
            if (file == null) return false;
            fontFile = file;
        }

        try {
            MFontLoadSave.save(font, fontFile);
        }
        catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Can't save file.", "Error",
                            JOptionPane.OK_OPTION);
            return false;
        }

        setSaved(true);
        return true;
    }

    static void setSaved(boolean saved) {
        boolean old = fontSaved;

        if (old != saved) {
            fontSaved = saved;
            actSave.setEnabled(!fontSaved);
        }
    }

    public static void updateButtonMode() {
        if (mode == 0) actModePointer.setSelected(true);
        else actModePointer.setSelected(false);

        if (mode == 1) actModeXPensil.setSelected(true);
        else actModeXPensil.setSelected(false);

        if (mode == 2) actModePensil.setSelected(true);
        else actModePensil.setSelected(false);

        if (mode == 3) actModeRuber.setSelected(true);
        else actModeRuber.setSelected(false);
    }

    static void updateTitle() {
        String title = Application.NAME;
        if (fontName != null) title += " : " + fontName;
        if (fontFile != null) title += " (" + fontFile.getName() + ")";
        work.setTitle(title);
    }

    private static class OnFontChange implements MFontListener
    {
        @Override
        public void mFontEvent(MFontEvent change) {
            System.out.println(change.toString() + " "
                            + change.getReasonString() + " index="
                            + change.getIndex());
            setSaved(false);
        }
    }

    public static class OnNew extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnNew() {
            super("new", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    public static class OnOpen extends ActionX
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

            doChooserOpen();

            int returnVal = chooserOpen.showOpenDialog(work);
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

    public static class OnSave extends ActionX
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

    public static class OnSaveAs extends ActionX
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

    public static class OnUndo extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnUndo() {
            super("undo", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public static class OnRedo extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnRedo() {
            super("redo", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    public static class OnReflectHorz extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnReflectHorz() {
            super("reflect.horizontale", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().reflectHorizontale();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnReflectVert extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnReflectVert() {
            super("reflect.verticale", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().reflectVerticale();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnShiftLeft extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftLeft() {
            super("shift.left", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().shiftLeft();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnShiftRight extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftRight() {
            super("shift.right", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().shiftRight();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnShiftUp extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftUp() {
            super("shift.up", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().shiftUp();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnShiftDown extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnShiftDown() {
            super("shift.down", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                editPanel.edit.getSymbol().shiftDown();
            }
            catch (NullPointerException ex) {
            }
        }
    }

    public static class OnExit extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnExit() {
            super("exit", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkSaveFont()) work.dispose();
        }

    }

    public static class OnProperties extends ActionX
    {
        private static final long serialVersionUID = 1L;

        public OnProperties() {
            super("properties", res);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MFont c;
            if (fpf == null) fpf = new FontProperties(work, res);

            if (font == null) return;
            c = font.clone();
            if (fpf.start(c) == FontProperties.ACTION_OK) font.copy(c);
            fpf.setMFont(null);
        }

    }

    public static class OnModeXPensil extends ActionX
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

    public static class OnModePensil extends ActionX
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

    public static class OnModeRuber extends ActionX
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

    public static class OnModePointer extends ActionX
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
}
