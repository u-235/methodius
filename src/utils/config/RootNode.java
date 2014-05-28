
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
    File   file;
    String fName;

    public RootNode() {
        super(null, null);
    }

    public RootNode(String name) {
        this();
        fName = name;
    }

    public RootNode(File file) {
        this();
        this.file = file;
    }

    public final void load() {
        if (fName != null) load(fName);
        else if (file != null) load(file);
    }

    public final void load(String name) {
        load(new File(name));
    }

    public final void load(File file) {
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

    public final void save() {
        if (fName != null) save(fName);
        else if (file != null) save(file);
    }

    public final void save(String name) {
        save(new File(name));
    }

    public final void save(File file) {
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
