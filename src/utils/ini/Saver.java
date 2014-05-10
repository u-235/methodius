
package utils.ini;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Saver {
    private BufferedWriter out;
    IniStyle               style;

    public Saver(OutputStream out, IniStyle style) {
        this.out = new BufferedWriter(new OutputStreamWriter(out));

        if (style == null) this.style = IniStyle.flexible();
        else this.style = style;
    }

    public Saver(OutputStream out) {
        this(out, null);
    }

    public void close() throws IOException {
        if (out == null) return;
        out.close();
        out = null;
    }

    private void putKey(String key) throws IOException {
        String k;

        if (key == null) throw (new IllegalArgumentException("key is null"));
        k = key.trim();
        if (k.isEmpty()) throw (new IllegalArgumentException("key is empty"));

        out.write(k);
        out.write(" =");
    }

    /**
     * TODO Метод должен возвращать исходную строку, в которой лидирующий пробел
     * заменён на \s. Так же должны быть заменены символы возврата каретки и
     * перевода строки на \r и \n
     */
    private static String checkString(String s) {
        if (s.isEmpty()) return null;

        s = s.replace("\\", "\\\\");
        s = s.replace("\n", "\\n");
        s = s.replace("\r", "\\r");

        if (Character.isWhitespace(s.charAt(0))) return "\\s" + s;
        return s;
    }

    private void putValue(String value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(checkString(value));
    }

    private void putValue(boolean value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(value ? "true" : "false");
    }

    private void putValue(byte value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Byte) value).toString());
    }

    private void putValue(char value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Character) value).toString());
    }

    private void putValue(int value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Integer) value).toString());
    }

    private void putValue(long value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Long) value).toString());
    }

    private void putValue(double value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Double) value).toString());
    }

    private void putValue(float value) throws IOException {
        if (out == null) return;
        out.write(' ');
        out.write(((Float) value).toString());
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
        putKey(key);
        putValue(value);
        newLine();
    }

    public void key(String key, boolean value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, byte value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, char value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, int value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, long value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, double value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, float value) throws IOException {
        putKey(key);
        putValue(value);
    }

    public void key(String key, byte[] value) throws IOException {
        String s = null;

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) s = s + " , ";
                s += ((Byte) value[i]).toString();
            }
        }

        key(key, s);
    }

    public void key(String key, char[] value) throws IOException {
        putKey(key);

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) out.write(" , ");
                putValue(((Character) value[i]).toString());
            }
        }
    }

    public void key(String key, int[] value) throws IOException {
        putKey(key);

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) out.write(" , ");
                putValue(((Integer) value[i]).toString());
            }
        }
    }

    public void key(String key, long[] value) throws IOException {
        putKey(key);

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) out.write(" , ");
                putValue(((Long) value[i]).toString());
            }
        }
    }

    public void key(String key, double[] value) throws IOException {
        putKey(key);

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) out.write(" , ");
                putValue(((Double) value[i]).toString());
            }
        }
    }

    public void key(String key, float[] value) throws IOException {
        putKey(key);

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (i != 0) out.write(" , ");
                putValue(((Float) value[i]).toString());
            }
        }
    }
}
