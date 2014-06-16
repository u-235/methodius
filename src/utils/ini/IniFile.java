
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
        load();
    }

    public IniFile(String file, IniStyle style) {
        super(file);
        this.style = style;
        load();
    }

    public IniFile(File file) {
        super(file);
        load();
    }

    public IniFile(File file, IniStyle style) {
        super(file);
        this.style = style;
        load();
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
            // XXX print
            log.log(Level.CONFIG, "error at " + line + " line in " + col
                            + " column, character code point = " + ch);
        }

        @Override
        public void comment(String com) {
            // XXX print
            log.log(Level.CONFIG, "comments : " + com);
            if (comment == null) comment = com;
            else comment = comment + "\n" + com;
        }

        @Override
        public void section(String sec) {
            // XXX print
            log.log(Level.CONFIG, "section  : " + sec);
            work = work.root().node(sec);
            if (comment != null) {
                work.putComment(comment);
                comment = null;
            }
        }

        @Override
        public void key(String k) {
            // XXX print
            log.log(Level.CONFIG, "key      : " + k);
            key = k;
        }

        @Override
        public void value(String value) {
            // XXX print
            log.log(Level.CONFIG, "value    : {0}", value);

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
