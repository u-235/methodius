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

package utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

public abstract class RootNode extends ConfigNode {
    File file;

    public RootNode() {
        super(null, null);
    }

    public RootNode(String name) {
        this(new File(name));
    }

    public RootNode(File file) {
        this();
        this.file = file;
    }

    public void setFile(File f) {
        file = f;
    }

    public void setFile(String name) {
        if (name == null) file = null;
        else file = new File(name);
    }

    public File getFile() {
        return file;
    }

    public final synchronized void load() {
        if (file != null) load(file);
    }

    public final synchronized void load(String name) {
        load(new File(name));
    }

    public final synchronized void load(File file) {
        ConfigLoader loader;

        if (file == null) {
            throw new NullPointerException();
        }

        try {
            loader = doLoader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.log(Level.INFO, "File {0} not found", file.getAbsolutePath());
            return;
        }

        try {
            loader.load();
        } catch (IOException e) {
            log.log(Level.INFO, "Error while load file {0}",
                            file.getAbsolutePath());
        } catch (InterruptedException e) {
            log.log(Level.INFO, "Interrupt while load file {0}",
                            file.getAbsolutePath());
        }

        try {
            loader.close();
        } catch (IOException e) {
            log.log(Level.WARNING, "Error while close file {0}",
                            file.getAbsolutePath());
        }
    }

    protected abstract ConfigLoader doLoader(InputStream in);

    public final synchronized void save() {
        if (file != null) save(file);
    }

    public final synchronized void save(String name) {
        save(new File(name));
    }

    public final synchronized void save(File file) {
        ConfigSaver saver;

        if (file == null) {
            throw new NullPointerException();
        }

        try {
            saver = doSaver(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            log.log(Level.INFO, "File {0} not found", file.getAbsolutePath());
            return;
        }

        try {
            saver.save();
        } catch (IOException e1) {
            log.log(Level.INFO, "Error while save file {0}",
                            file.getAbsolutePath());
        } catch (InterruptedException e) {
            log.log(Level.INFO, "Interrupt while save file {0}",
                            file.getAbsolutePath());
        }

        try {
            saver.close();
        } catch (IOException e) {
            log.log(Level.WARNING, "Error while close file {0}",
                            file.getAbsolutePath());
        }
    }

    protected abstract ConfigSaver doSaver(OutputStream out);
}
