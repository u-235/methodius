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

package logic;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import utils.config.ConfigNode;

/**
 * Класс для хранения путей директорий приложения. Часть из этих путей может
 * быть переопределена и сохранена в {@link ConfigNode}.
 * <p>
 * TODO Сделать функцию, преобразующую строку с абсолютным путём в строку вида
 * %user%/path %app%/path и т.п.. Это упростит работу портабельной версии. Так
 * же нужна функция для получения файла из такой строки.
 */
public class Directories {
    public static final String CONFIG_NODE     = "/directories";
    public static final String CONFIG_KEY_USER = "user";
    public static final String CONFIG_KEY_LAST = "last";
    private ConfigNode         config;
    private File               work;
    private File               fonts;
    private File               home;
    private File               user;
    private File               app;
    private File               last;

    /**
     * После создания объекта следует создать {@link ConfigNode} и вызвать
     * {@link #loadConfig(ConfigNode)}.
     */
    Directories() {
        // Свойства перечислены в System.getProperties()
        work = new File(System.getProperty("user.dir"));
        home = new File(System.getProperty("user.home"));
        user = home;

        try {
            app = onlyDir(new File(URLDecoder.decode(getClass()
                            .getProtectionDomain().getCodeSource()
                            .getLocation().getPath(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            app = new File("");
        }

        // Если приложение запущено из IDE, то папку с предопределёнными
        // шрифтами следует искать в ../release/microfonts
        if (app.getName().equals("build")) {
            fonts = new File(app.getParentFile(), "release/microfonts");
        } else {
            fonts = new File(app, "microfonts");
        }
    }

    /**
     * Возвращает часть пути, содержащую только папку.
     * 
     * @param f Путь к файлу или папке.
     * @return Путь к папке.
     */
    public static File onlyDir(File f) {
        if (f.isDirectory()) return f;
        return f.getParentFile();
    }

    /**
     * Загружает параметры, используя узел {@link #CONFIG_NODE}.
     * 
     * @param cfg Настройки.
     */
    void loadConfig(ConfigNode cfg) {
        config = cfg.node(CONFIG_NODE);
        String val;

        if (last != null) {
            config.put(CONFIG_KEY_LAST, last.getAbsolutePath());
        } else {
            val = config.get(CONFIG_KEY_LAST, null);
            if (val != null) last = new File(val);
        }

        val = config.get(CONFIG_KEY_USER, null);
        if (val != null) user = new File(val);
    }

    /**
     * Возвращает путь папки, в которой лежит приложение. Это может быть папка с
     * jar файлом или папка, в которой лежат файлы class.
     * 
     * @see #work()
     */
    public File app() {
        return app;
    }

    /**
     * Возвращает домашнюю папку пользователя.
     * 
     * @see #fonts()
     * @see #user()
     */
    public File home() {
        return home;
    }

    /**
     * Возвращает рабочую папку. Это папка, из которой было запущено приложение.
     * 
     * @see #app()
     */
    public File work() {
        return work;
    }

    /**
     * Возвращает папку последнего открытого шрифта.
     * 
     * @see #last(File)
     */
    public File last() {
        if (last == null) {
            String path = config.get(CONFIG_KEY_LAST, null);
            if (path != null) {
                last = new File(path);
            }
        }

        if (last == null || !last.exists()) {
            last = fonts();
        }

        return last;
    }

    /**
     * Устанавливает путь последнего открытого шрифта. Путь очищается от имени
     * файла и содержит только папку.
     * 
     * @param f Последний открытый шрифт.
     * 
     * @see #last()
     */
    public void last(File f) {
        last = onlyDir(f);
        if (config != null) {
            config.put(CONFIG_KEY_LAST, last.getAbsolutePath());
        }
    }

    /**
     * Возвращает папку с предопределёнными шрифтами.
     * 
     * @see #home()
     * @see #user()
     */
    public File fonts() {
        return fonts;
    }

    /**
     * Возвращает папку с шрифтами пользователя.
     * 
     * @see #fonts()
     * @see #home()
     */
    public File user() {
        return user;
    }

    /**
     * Устанавливает папку с шрифтами пользователя.
     * 
     * @param f
     */
    public void user(File f) {
        user = onlyDir(f);
        if (config != null) {
            config.put(CONFIG_KEY_USER, user.getAbsolutePath());
        }
    }
}
