
package utils.recent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import utils.event.ListenerChain;

public class RecentFiles {
    ArrayList<File> files     = new ArrayList<File>();
    JMenu           pMenu;
    int             maxFiles  = 8;
    int             maxItems  = 8;
    ListenerChain   listeners = new ListenerChain();

    class Listener implements ActionListener {
        File f;

        Listener(File fl) {
            f = fl;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireSelectFile(f);
        }
    }

    public File getLastFile() {
        return files.get(0);
    }

    public void setLastFile(File last, String name) {
        int i = files.indexOf(last);
        if (i < 0) {
            // Create new item
            files.add(0, last);
            adjustFiles();

            if (name == null) name = last.getName();
            JMenuItem mi = new JMenuItem(name);
            mi.setToolTipText(last.getPath());

            pMenu.add(mi, 0);
            adjustItems();

            mi.addActionListener(new Listener(last));
        } else if (i > 0) {
            // Replace item to top
            if (i < maxFiles) {
                files.remove(i);
                files.add(0, last);
            } else {
                files.add(0, last);
                adjustFiles();
            }

            JMenuItem mi;
            if (i < maxItems) {
                mi = pMenu.getItem(i);
                pMenu.remove(i);
                pMenu.add(mi, 0);
            } else {
                mi = new JMenuItem(name);
                mi.setToolTipText(last.getPath());
                pMenu.add(mi, 0);
                adjustItems();
            }
        } else {
            // Item always in top.
        }
    }

    public File[] getFiles() {
        return null;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int max) {
        maxItems = max;
        adjustFiles();
    }

    private void adjustFiles() {
        // TODO Auto-generated method stub

    }

    protected synchronized JMenu doMenu(String name) {
        if (pMenu == null) {
            pMenu = new JMenu(name);
            JMenuItem mi;
            File f;
            for (int i = 0; i < files.size() && i < maxFiles && i < maxItems; i++) {
                f = files.get(i);
                mi = new JMenuItem(f.getName());
                mi.setToolTipText(f.getPath());
                pMenu.add(mi);
            }
        }
        return pMenu;
    }

    public JMenu menu(String name) {
        return doMenu(name);
    }

    public JMenu menu() {
        return doMenu("recent files");
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int max) {
        maxItems = max;
        adjustItems();
    }

    private void adjustItems() {
        // TODO Auto-generated method stub

    }

    public void addSelectFileListener(SelectFileListener listener) {
        listeners.add(SelectFileListener.class, listener);
    }

    public void removeSelectFileListener(SelectFileListener listener) {
        listeners.remove(SelectFileListener.class, listener);
    }

    protected void fireSelectFile(File f) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == SelectFileListener.class)
                ((SelectFileListener) listenerArray[i + 1]).fileSelected(f);
        }
    }
}
