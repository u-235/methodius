
package utils.ini;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Saver implements Closeable {
    private BufferedWriter out;
    IniStyle               style;

    public Saver(OutputStream out, IniStyle style) {
        if (style == null) this.style = IniStyle.flexible();
        else this.style = style;

        this.out = new BufferedWriter(new OutputStreamWriter(out,
                        this.style.charset));
    }

    public Saver(OutputStream out) {
        this(out, null);
    }

    @Override
    public void close() throws IOException {
        if (out == null) return;
        out.close();
        out = null;
    }

    /**
     * TODO Метод должен возвращать исходную строку, в которой лидирующий пробел
     * заменён на \s. Так же должны быть заменены символы возврата каретки и
     * перевода строки на \r и \n
     */
    private static String checkString(String s) {
        if (s==null || s.isEmpty()) return "";

        s = s.replace("\\", "\\\\");
        s = s.replace("\n", "\\n");
        s = s.replace("\r", "\\r");

        if (Character.isWhitespace(s.charAt(0))) return "\\s" + s;
        return s;
    }

    public void newLine() throws IOException {
        out.write(style.lineEnd);
    }

    public void comment(String com) throws IOException {
        if (out == null) return;
        if (com == null || com.isEmpty()) return;

        StringTokenizer st = new StringTokenizer(com, "\n", true);
        while (st.hasMoreTokens()) {
            String sc = st.nextToken();
            if (sc.equals("\n")) {
                out.write(style.lineEnd);
            } else {
                out.write(style.commentStart.charAt(0));
                out.write(sc);
            }
        }
        out.write(style.lineEnd);
    }

    public void section(String sec) throws IOException {
        if (out == null) return;
        if (sec == null)
            throw (new IllegalArgumentException("section is null"));
        out.write('[');
        out.write(sec);
        out.write(']');
        out.write(style.lineEnd);
    }

    public void key(String key, String value) throws IOException {
        if (key == null) throw (new IllegalArgumentException("key is null"));
        if (key.isEmpty())
            throw (new IllegalArgumentException("key is empty"));

        out.write(key);
        out.write(" = ");
        out.write(checkString(value));
        newLine();
    }
}
