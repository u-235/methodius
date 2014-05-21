
package utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        loadS(in);

        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected abstract void loadS(InputStream in);

    public final void save() {
        if (fName != null) save(fName);
        else if (file != null) save(file);
    }

    public final void save(String name) {
        save(new File(name));
    }

    public final void save(File file) {
        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
            saveS(out);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected abstract void saveS(OutputStream out);
}
