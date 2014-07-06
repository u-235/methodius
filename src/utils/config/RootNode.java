
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
        FileInputStream in;

        if (file == null) {
            throw new NullPointerException();
        }

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.log(Level.INFO, "File {0} not found", file.getAbsolutePath());
            return;
        }

        try {
            loadS(in);
        } catch (IOException e) {
            log.log(Level.INFO, "Error while load file {0}",
                            file.getAbsolutePath());
        }

        try {
            in.close();
        } catch (IOException e) {
            log.log(Level.WARNING, "Error while close file {0}",
                            file.getAbsolutePath());
        }
    }

    protected abstract void loadS(InputStream in) throws IOException;

    public final synchronized void save() {
        if (file != null) save(file);
    }

    public final synchronized void save(String name) {
        save(new File(name));
    }

    public final synchronized void save(File file) {
        FileOutputStream out;

        if (file == null) {
            throw new NullPointerException();
        }

        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            log.log(Level.INFO, "File {0} not found", file.getAbsolutePath());
            return;
        }

        try {
            saveS(out);
        } catch (IOException e1) {
            log.log(Level.INFO, "Error while save file {0}",
                            file.getAbsolutePath());
        }

        try {
            out.close();
        } catch (IOException e) {
            log.log(Level.WARNING, "Error while close file {0}",
                            file.getAbsolutePath());
        }
    }

    protected abstract void saveS(OutputStream out) throws IOException;
}
