
package utils.ini;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import utils.config.RootNode;
import utils.config.ConfigNode;

public class IniFile extends RootNode {
    IniStyle style;

    public IniFile() {
        super();
    }

    public IniFile(String file) {
        super(file);
    }

    public IniFile(String file, IniStyle style) {
        super(file);
        this.style = style;
    }

    public IniFile(File file) {
        super(file);
    }

    public IniFile(File file, IniStyle style) {
        super(file);
        this.style = style;
    }

    @Override
    protected void loadS(InputStream in) throws IOException {
        Parser.parse(in, new IniHandler(), style);
    }

    @Override
    protected void saveS(OutputStream out) throws IOException {
        Saver svr = new Saver(out, style);
        try {
            save(this, svr);
        } catch (IOException e) {
            svr.close();
            throw e;
        }
        svr.close();
    }

    protected void save(ConfigNode node, Saver svr) throws IOException {
        if (!node.isEmpty()) {
            String com;
            String val;

            try {
                com = node.getComment();
                val = node.absolutePath().substring(1);

                svr.comment(com);
                svr.section(val);

                for (String k : node.keys()) {
                    com = node.getComment(k);
                    val = node.get(k, null);
                    svr.comment(com);
                    svr.key(k, val);
                }
            } catch (IllegalStateException ignore) {
                log.log(Level.CONFIG, "Node {0} removed", node.absolutePath());
            }

            svr.newLine();
        }

        for (String n : node.childrenNames()) {
            save(node.node(n), svr);
        }
    }

    class IniHandler implements Handler {
        ConfigNode work;
        String     key;
        String     comment;

        IniHandler() {
            work = IniFile.this.root;
            comment = null;
        }

        @Override
        public void error(int state, int ch, int line, int col) {
        }

        @Override
        public void comment(String com) {
            if (comment == null) comment = com;
            else comment = comment + "\n" + com;
        }

        @Override
        public void section(String sec) {
            work = work.root().node(sec);
            if (comment != null) {
                work.putComment(comment);
                comment = null;
            }
        }

        @Override
        public void key(String k) {
            key = k;
        }

        @Override
        public void value(String value) {
            if (key != null) {
                work.put(key, value);
                if (comment != null) {
                    work.putComment(key, comment);
                }
                key = null;
            }
            comment = null;
        }
    }
}
