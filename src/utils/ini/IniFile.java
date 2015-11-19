
package utils.ini;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import utils.config.ConfigLoader;
import utils.config.ConfigSaver;
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
    protected ConfigLoader doLoader(InputStream in) {
        return new Reader(in);
    }

    class Reader implements ConfigLoader {
        Parser parser;

        public Reader(InputStream in, IniStyle st) {
            parser = new Parser(in, st);
        }

        public Reader(InputStream in) {
            parser = new Parser(in, style);
        }

        @Override
        public void close() throws IOException {
            Parser p = parser;
            parser = null;
            p.close();
        }

        @Override
        public void load() throws IOException, InterruptedException {
            if (parser == null) {
                throw new IOException("Parser already close");
            }

            parser.parse(new IniHandler());
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

    @Override
    protected ConfigSaver doSaver(OutputStream out) {
        return new Saver(out, style);
    }

    class Saver implements ConfigSaver {
        Formater formater;

        public Saver(OutputStream out, IniStyle st) {
            formater = new Formater(out, st);
        }

        public Saver(OutputStream out) {
            this(out, style);
        }

        @Override
        public void close() throws IOException {
            Formater f = formater;
            formater = null;
            f.close();
        }

        @Override
        public void save() throws IOException, InterruptedException {
            save(IniFile.this);
        }

        void testInterrupt() throws InterruptedException {
            if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
                throw new InterruptedException();
            }
        }

        protected void save(ConfigNode node) throws IOException,
                        InterruptedException {
            if (!node.isEmpty()) {
                String com;
                String val;

                try {
                    com = node.getComment();
                    val = node.absolutePath().substring(1);

                    testInterrupt();
                    formater.comment(com);
                    formater.section(val);

                    for (String k : node.keys()) {
                        com = node.getComment(k);
                        val = node.get(k, null);

                        testInterrupt();
                        formater.comment(com);
                        formater.key(k, val);
                    }
                } catch (IllegalStateException ignore) {
                    log.log(Level.CONFIG, "Node {0} removed",
                                    node.absolutePath());
                }

                formater.newLine();
            }

            for (String n : node.childrenNames()) {
                save(node.node(n));
            }
        }
    }
}
