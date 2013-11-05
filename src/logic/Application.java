package logic;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
    public static final String   NAME             = "Mifodius";
    public static final int      VER_MAJOR        = 0;
    public static final int      VER_MINOR        = 8;

    public static final String   ON_NEW_FONT      = "font.new";
    public static final String   ON_OPEN_FONT     = "font.open";
    public static final String   ON_SAVE_FONT     = "font.save";
    public static final String   ON_SAVE_AS       = "font.save.as";
    public static final String   ON_UNDO          = "undo";
    public static final String   ON_REDO          = "redo";
    public static final String   ON_REFLECT_HOR   = "refl.hor";
    public static final String   ON_REFLECT_VERT  = "refl.vert";
    public static final String   ON_PROPERTIES    = "font.prop";
    public static final String   ON_EXIT          = "exit";
    public static final String   ON_SHIFT_LEFT    = "shift.left";
    public static final String   ON_SHIFT_RIGHT   = "shift.right";
    public static final String   ON_SHIFT_UP      = "shift.up";
    public static final String   ON_SHIFT_DOWN    = "shift.down";
    public static final String   ON_MODE_POINTER  = "mode.point";
    public static final String   ON_MODE_XPENSIL  = "mode.x.pensil";
    public static final String   ON_MODE_PENSIL   = "mode.pensil";
    public static final String   ON_MODE_RUBER    = "mode.ruber";
    public static final String   ON_SYMBOL_CHANGE = "symbol.change";
    public static final String   ON_HEAP_SIZE     = "heap.size";

    public static Resource       res;
    static File                  fontFile;
    static String                fontName         = "new font";
    static boolean               fontSaved;
    public static boolean        exit;

    static ActionMap             actions;

    static WorkShop              work;
    static FontPanel             fontPanel;
    static EditPanel             editPanel;
    static JFileChooser          chooserSave;
    static JFileChooser          chooserOpen;
    static FontProperties        fpf;

    private static MFont         font;
    private static MFontListener atFontChange;
    private static int           mode;

    public static void main(String[] args) {
        Runtime r;
        long us;
        String heaps;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        doWorkShop();

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        r = Runtime.getRuntime();

        while (!exit) {
            us = r.totalMemory() - r.freeMemory();
            heaps = "heap : " + us / 1000 + " kb ("
                            + (us * 100 / r.totalMemory()) + "%)";

            if (actions.get(ON_HEAP_SIZE) != null)
                actions.get(ON_HEAP_SIZE).actionPerformed(
                                new ActionEvent(work,
                                                ActionEvent.ACTION_PERFORMED,
                                                heaps));
            try {
                java.lang.Thread.sleep(750);
            }
            catch (InterruptedException e) {
            }
        }/* */
    }

    public static void doWorkShop() {
        if (work != null) return;
        res = new Resource("locale/MainForm");
        atFontChange = new OnFontChange();

        actions = doActions();
        work = new WorkShop(actions);

        work.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        work.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Application.checkSaveFont()) work.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                exit = true;
            }
        });
        editPanel = new EditPanel(actions);
        fontPanel = new FontPanel(actions);
        work.setLeft(fontPanel);
        work.setRight(editPanel);

        updateTitle();
        setSaved(true);

        work.pack();
        work.setVisible(true);

        new Thread() {
            @Override
            public void run() {
                doChooserOpen();
                doChooserSave();
            }

        }.start();
    }

    static ActionMap doActions() {
        ActionMap am = new ActionMap();

        am.put(ON_OPEN_FONT, new OnOpen());
        am.put(ON_NEW_FONT, new OnNew());
        am.put(ON_SAVE_FONT, new OnSave());
        am.put(ON_SAVE_AS, new OnSaveAs());
        am.put(ON_EXIT, new OnExit());
        am.put(ON_UNDO, new OnUndo());
        am.put(ON_REDO, new OnRedo());
        am.put(ON_REFLECT_HOR, new OnReflectHorz());
        am.put(ON_REFLECT_VERT, new OnReflectVert());
        am.put(ON_SHIFT_LEFT, new OnShiftLeft());
        am.put(ON_SHIFT_RIGHT, new OnShiftRight());
        am.put(ON_SHIFT_UP, new OnShiftUp());
        am.put(ON_SHIFT_DOWN, new OnShiftDown());
        am.put(ON_PROPERTIES, new OnProperties());
        am.put(ON_MODE_XPENSIL, new OnModeXPensil());
        am.put(ON_MODE_PENSIL, new OnModePensil());
        am.put(ON_MODE_RUBER, new OnModeRuber());
        am.put(ON_MODE_POINTER, new OnModePointer());
        am.put(ON_SYMBOL_CHANGE, new OnSymbolChange());

        return am;
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
            actions.get(ON_SAVE_FONT).setEnabled(!fontSaved);
        }
    }

    public static void updateButtonMode() {
        ActionX actModePointer = (ActionX) actions.get(ON_MODE_POINTER);
        ActionX actModeXPensil = (ActionX) actions.get(ON_MODE_XPENSIL);
        ActionX actModePensil = (ActionX) actions.get(ON_MODE_PENSIL);
        ActionX actModeRuber = (ActionX) actions.get(ON_MODE_RUBER);

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

    public static class OnSymbolChange extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            editPanel.setMSymbol(fontPanel.getSelectedSymbol());
        }
    };

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
                editPanel.getMSymbol().reflectHorizontale();
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
                editPanel.getMSymbol().reflectVerticale();
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
                editPanel.getMSymbol().shiftLeft();
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
                editPanel.getMSymbol().shiftRight();
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
                editPanel.getMSymbol().shiftUp();
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
                editPanel.getMSymbol().shiftDown();
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
