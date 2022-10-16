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

package utils.recent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import utils.event.ListenerChain;

/**
 * Класс для хранения списка недавних файлов (recent files) и формирования меню.
 */
public class RecentFiles {
    ArrayList<File> files     = new ArrayList<File>();
    JMenu           pMenu;
    int             maxFiles  = 8;
    int             maxItems  = 8;
    ListenerChain   listeners = new ListenerChain();

    /**
     * Вспомогательный класс для получения сообщений о выборе пункта меню. Это
     * сообщение приводит к выпуску {@linkplain SelectFileListener сообщения о
     * выборе файла}.
     * 
     */
    class Listener implements ActionListener {
        File f;

        /**
         * Конструктор получателя сообщений о выборе пункта меню.
         * 
         * @param fl Файл, который соответствует пункту меню.
         */
        Listener(File fl) {
            f = fl;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Просто выдаём сообщение.
            fireSelectFile(f);
        }
    }

    /**
     * Возвращает последний файл или {@code null}, если метод
     * {@link #setLastFile(File, String)} ни разу не вызывался.
     */
    public File getLastFile() {
        if (files.isEmpty()) return null;
        return files.get(0);
    }

    /**
     * Устанавливает последний открытый файл.
     * 
     * @param last Последний файл. Полный путь используется как всплывающая
     *            подсказка пункта меню.
     * @param name Имя файла, отображаемое в пункте меню. Если равно
     *            {@code null}, то имя берётся из {@code last}.
     */
    public void setLastFile(File last, String name) {
        int i = files.indexOf(last);
        if (i < 0) {
            // Create new item
            files.add(0, last);
            adjustFiles();

            if (name == null) name = last.getName();
            if (pMenu != null) {
                JMenuItem mi = new JMenuItem(name);
                mi.setToolTipText(last.getPath());

                pMenu.add(mi, 0);
                adjustItems();

                mi.addActionListener(new Listener(last));
            }
        } else if (i > 0) {
            // Replace item to top
            if (i < maxFiles) {
                files.remove(i);
                files.add(0, last);
            } else {
                files.add(0, last);
                adjustFiles();
            }

            if (pMenu != null) {
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
            }
        } else {
            // Item always in top.
        }
    }

    /**
     * Возвращает количество сохранённых файлов.
     * 
     * @see #getMaxFiles()
     * @see #getMaxItems()
     */
    public int getFilesCount() {
        return files.size();
    }

    public File[] getFiles() {
        return files.toArray(new File[files.size()]);
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int max) {
        if (max < 0)
            throw new IllegalArgumentException("invalid maximum files: " + max);
        maxFiles = max;
        adjustFiles();
        adjustItems();
    }

    private void adjustFiles() {
        int i = files.size();
        while (i > maxFiles) {
            files.remove(--i);
        }
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
                mi.addActionListener(new Listener(f));
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
        if (max < 0)
            throw new IllegalArgumentException("invalid maximum item: " + max);
        maxItems = max;
        adjustItems();
    }

    private void adjustItems() {
        if (pMenu == null) return;

        int i = pMenu.getItemCount();
        while (i > maxItems || i > maxFiles) {
            pMenu.remove(--i);
        }
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
