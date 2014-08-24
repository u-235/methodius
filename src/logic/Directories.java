
package logic;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import utils.config.ConfigNode;

public class Directories {
    ConfigNode   config;
    private File work = new File(System.getProperty("user.dir"));
    private File home = new File(System.getProperty("user.home"));
    private File user;
    private File run;
    private File last;
    private File build;

    Directories() {
        try {
            run = new File(URLDecoder.decode(getClass().getClassLoader()
                            .getResource("").getPath(), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            run = new File("");
        }

        build = run;
        if (build.getName().equals("build")) {
            build = new File(build.getParentFile(), "release");
        }
    }

    public static File onlyDir(File f) {
        if (f.isDirectory()) return f;
        return f.getParentFile();
    }

    void loadConfig(ConfigNode cfg) {
        config = cfg.node("/directories");
        String val;

        val = config.get("last", null);
        if (val != null) last = new File(val);

        val = config.get("user", null);
        if (val != null) user = new File(val);

        if (last != null) config.put("last", last.getAbsolutePath());
    }

    /**
     * Возвращает путь папки, в которой лежит приложение. Это может быть папка с
     * jar файлом или папка, в которой лежат файлы class.
     */
    public File run() {
        return run;
    }

    public File home() {
        return home;
    }

    public File work() {
        return work;
    }

    public File last() {
        if (last == null) {
            String path = config.get("last", null);
            if (path != null) {
                last = new File(path);
            }

            if (last == null || !last.exists()) {
                last = new File(build, "microfonts");
            }
        }
        return last;
    }

    public void last(File f) {
        last = onlyDir(f);
        if (config != null) {
            config.put("last", last.getAbsolutePath());
        }
    }
}
