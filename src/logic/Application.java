
package logic;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import microfont.Document;
import microfont.MFont;
import microfont.MSymbol;
import microfont.ls.MFontLoadSave;
import utils.config.ConfigNode;
import utils.config.RootNode;
import utils.ini.IniFile;
import utils.recent.RecentFiles;
import utils.recent.SelectFileListener;
import utils.resource.Resource;
import forms.EditPanel;
import forms.FontPanel;
import forms.FontProperties;
import forms.WorkShop;

public class Application {
    public static final String     NAME         = "Methodius";
    public static final int        VER_MAJOR    = 0;
    public static final int        VER_MINOR    = 8;

    public static final String     ON_HEAP_SIZE = "heap.size";

    static Application             SINGLE       = new Application();
    RootNode                       config;
    Locale                         loc;
    Resource                       res;
    File                           runDir;
    File                           workDir;
    RecentFiles                    files;

    File                           fontFile;
    String                         fontName     = "new font";
    boolean                        fontSaved;
    public boolean                 exit;

    ActionMap                      actions;
    UndoManager                    uManager;
    int                            undoCount;

    WorkShop                       work;
    FontPanel                      fontPanel;
    EditPanel                      editPanel;
    JFileChooser                   chooserSave;
    JFileChooser                   chooserOpen;
    FontProperties                 fpf;

    private Document               doc;
    private PropertyChangeListener atFontChange;

    public static void main(String[] args) {
        Runtime r;
        long us;
        String heaps;

        application().doWorkShop();

        r = Runtime.getRuntime();

        while (!application().exit) {
            us = r.totalMemory() - r.freeMemory();
            heaps = "heap : " + us / 1000 + " kb ("
                            + (us * 100 / r.totalMemory()) + "%)";

            if (application().actions.get(ON_HEAP_SIZE) != null)
                application().actions.get(ON_HEAP_SIZE).actionPerformed(
                                new ActionEvent(application().work,
                                                ActionEvent.ACTION_PERFORMED,
                                                heaps));
            try {
                java.lang.Thread.sleep(750);
            } catch (InterruptedException e) {
            }
        }/* */
    }

    protected Application() {
        try {
            runDir = new File(URLDecoder.decode(getClass().getClassLoader()
                            .getResource("").getPath(), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            runDir = new File("");
        }

        config = new IniFile(new File(runDir, "methodius.ini"));
        config.load();

        String l = config.node("user").get("locale", null);
        if (l == null) loc = Locale.getDefault();
        else loc = new Locale(l);

        workDir = new File(config.node("files").get("last",
                        new File(runDir, "microfonts").getPath()));

        res = new Resource("locale/MainForm", loc);
        res.setIconPath("icons/16/");

        files = new RecentFiles();
        ConfigNode cfg = config.node("/files");
        int max = cfg.getInt("max", 8);
        while (max > 0) {
            String fl = cfg.get(Integer.toString(max--), null);
            if (fl == null) continue;
            files.setLastFile(new File(fl), null);
        }
        files.addSelectFileListener(new SelectFileListener() {
            @Override
            public void fileSelected(File f) {
                loadMFont(f);
            }
        });
    }

    public static Application application() {
        return SINGLE;
    }

    public RootNode config() {
        return config;
    }

    public Locale locale() {
        return loc;
    }

    public void locale(Locale loc) {
        setLocale(loc);
    }

    public RecentFiles recent() {
        return files;
    }

    private void setLocale(Locale loc) {
        this.loc = loc;
    }

    public Resource resource() {
        return res;
    }

    public void doWorkShop() {
        if (work != null) return;

        doc = new Document();

        atFontChange = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent change) {
                updateUndoRedo();
            }
        };

        actions = new Actions(res);
        work = new WorkShop(actions);

        uManager = new UndoManager();
        doc.addUndoableEditListener(uManager);
        doc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                if (undoCount < 0) undoCount = Integer.MIN_VALUE;
                undoCount++;
                updateUndoRedo();
            }
        });
        updateUndoRedo();

        work.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        work.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        editPanel = new EditPanel(actions);
        editPanel.setDocument(doc);
        fontPanel = new FontPanel(actions);
        work.setLeft(fontPanel);
        work.setRight(editPanel);

        //XXX автозагрузка файла при запуске.
        loadMFont(recent().getLastFile());
        // updateTitle();
        setSaved(true);
        work.pack();
        work.setVisible(true);

        new Thread() {
            @Override
            public void run() {
                chooserOpen();
                chooserSave();
            }

        }.start();
    }

    public void exit() {
        if (!checkSaveFont()) return;

        config.node("/files").removeNode();
        ConfigNode fls = config().node("/files");
        fls.putInt("max", recent().getMaxFiles());
        File[] files = recent().getFiles();
        for (int i = files.length; i > 0;) {
            fls.put(Integer.toString(i), files[--i].getAbsolutePath());
        }

        application().config().save();
        work.dispose();
        exit = true;
    }

    public synchronized JFileChooser chooserOpen() {
        if (chooserOpen == null) {
            chooserOpen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "MicroFont", "mfnt");
            chooserOpen.setFileFilter(filter);
            chooserOpen.setDialogTitle("Open font.");
        }
        return chooserOpen;
    }

    public synchronized JFileChooser chooserSave() {
        if (chooserSave == null) {
            chooserSave = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "MicroFont", "mfnt");
            chooserSave.setFileFilter(filter);
            chooserSave.setDialogTitle("Сохранить шрифт как...");
            chooserSave.setAcceptAllFileFilterUsed(false);
            chooserSave.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        return chooserSave;
    }

    public void loadMFont(File file) {
        MFont font;

        if (file == null) return;

        try {
            font = MFontLoadSave.load(file, null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can't open file.", "Error",
                            JOptionPane.OK_OPTION);
            return;
        }

        if (font == null) {
            JOptionPane.showMessageDialog(null, "Error on load font.", "Error",
                            JOptionPane.OK_OPTION);
            return;
        }

        undoCount = 0;
        updateUndoRedo();
        fontFile = file;
        recent().setLastFile(file, font.getName());
        setMFont(font);
    }

    synchronized void setMFont(MFont newFont) {
        MFont font = doc.getFont();
        if (font != null) {
            font.removePropertyChangeListener(atFontChange);
            uManager.discardAllEdits();
        }

        font = newFont;
        doc.setFont(font);

        if (font != null) {
            font.addPropertyChangeListener(atFontChange);
            fontName = font.getName();
        } else {
            fontName = null;
        }
        actions.get(Actions.ON_SAVE_AS).setEnabled(font != null);

        fontPanel.setMFont(font);
        updateTitle();
        updateUndoRedo();
        setSaved(true);
    }

    public boolean checkSaveFont() {
        int r;
        if (fontSaved) return true;
        Object[] options = { "Сохранить", "Забить", "Отменить" };

        r = JOptionPane.showOptionDialog(
                        null,
                        "Файл\n"
                                        + fontFile.getAbsolutePath()
                                        + "\nбыл изменён, но не сохранён. Сохранить его?\nИли отменить.",
                        "Чё делать???", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION)
            return false;
        if (r == JOptionPane.YES_OPTION) return saveFontFile(false);
        return true;
    }

    private File getSaveFile() {
        File ret;
        chooserSave();
        while (true) {

            chooserSave.setCurrentDirectory(SINGLE.workDir);
            int returnVal = chooserSave.showSaveDialog(work);
            ret = chooserSave.getSelectedFile();
            if (ret == null || returnVal != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "Файл не выбран, печаль((",
                                "Твою мать!", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            // if (returnVal != JFileChooser.APPROVE_OPTION) return null;
            if (!ret.exists()) break;
            Object[] options = { "Канешна", "Чо, дурак???" };
            returnVal = JOptionPane.showOptionDialog(null,
                            "Выбранный файл существует.\nПерезаписать его?",
                            "Опа-на", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options,
                            options[1]);
            if (returnVal == JOptionPane.YES_OPTION) break;
        }

        // Check file extensions
        if (ret != null) {
            String name = ret.getName();
            int i = name.lastIndexOf('.');
            if ((i > 0) && (i + 1 < name.length())
                            && (name.substring(i + 1).equalsIgnoreCase("mfnt")))
                name = name.substring(0, i);
            name += ".mfnt";
            ret = new File(ret.getParentFile(), name);

            workDir = ret;
            config().node("files").put("last", ret.getParentFile().getPath());
        }

        return ret;
    }

    public boolean saveFontFile(boolean saveAs) {
        File file;
        MFont font = doc.getFont();

        if (font == null) return false;

        if (fontFile == null) saveAs = true;
        else if (fontFile.exists() && !fontFile.canWrite()) saveAs = true;

        if (saveAs) {
            file = getSaveFile();
            if (file == null) return false;
            fontFile = file;
        }

        try {
            MFontLoadSave.save(font, fontFile);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Can't save file.", "Error",
                            JOptionPane.OK_OPTION);
            return false;
        }

        if (saveAs) uManager.discardAllEdits();
        undoCount = 0;
        updateUndoRedo();
        updateTitle();
        return true;
    }

    void setSaved(boolean saved) {
        boolean old = fontSaved;

        if (old != saved) {
            fontSaved = saved;
            actions.get(Actions.ON_SAVE_FONT).setEnabled(!fontSaved);
        }
    }

    void updateTitle() {
        String title = Application.NAME;
        if (fontName != null) title += " : " + fontName;
        if (fontFile != null) title += " (" + fontFile.getName() + ")";
        work.setTitle(title);
    }

    void updateUndoRedo() {
        Action undo = actions.get(Actions.ON_UNDO);
        Action redo = actions.get(Actions.ON_REDO);

        undo.setEnabled(uManager.canUndo());
        undo.putValue(Action.SHORT_DESCRIPTION,
                        uManager.getUndoPresentationName());

        redo.setEnabled(uManager.canRedo());
        redo.putValue(Action.SHORT_DESCRIPTION,
                        uManager.getRedoPresentationName());

        setSaved(undoCount == 0);
    }

    public void undo() {
        if (uManager.canUndo()) {
            undoCount--;
            uManager.undo();
        }
        updateUndoRedo();
    }

    public void redo() {
        if (uManager.canRedo()) {
            undoCount++;
            uManager.redo();
        }
        updateUndoRedo();
    }

    public void reflectHorz() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("reflect horizontale");
            symbol.reflectHorizontale();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void reflectVert() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("reflect verticale");
            symbol.reflectVerticale();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void shiftLeft() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("shift left");
            symbol.shiftLeft();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void shiftRight() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("shift right");
            symbol.shiftRight();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void shiftUp() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("shift up");
            symbol.shiftUp();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void shiftDown() {
        try {
            MSymbol symbol = editPanel.getMSymbol();
            doc.symbolEdit("shift down");
            symbol.shiftDown();
            doc.endEdit();
        } catch (NullPointerException ex) {
        }
    }

    public void showProperties() {
        MFont font = doc.getFont();
        MFont c;
        if (fpf == null) fpf = new FontProperties(work, resource());

        if (font == null) return;
        c = font.clone();
        if (fpf.start(c) == FontProperties.ACTION_OK) {
            doc.fontEdit("change property");
            font.copy(c);
            doc.endEdit();
        }
        fpf.setMFont(null);
    }
}
