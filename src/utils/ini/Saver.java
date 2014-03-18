
package utils.ini;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class Saver {
    private BufferedWriter out;

    public Saver(Writer out) {
        this.out = new BufferedWriter(out);
    }

    @Override
    protected void finalize() {
        try {
            close();
        } catch (IOException e) {
        }
    }

    private void close() throws IOException {
        if (out == null) return;
        out.close();
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
    private String checkString(String s) {
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
        if (out == null) return;
        out.newLine();
    }

    public void comment(String com) throws IOException {
        String[] s;
        if (out == null) return;
        if (com == null || com.isEmpty()) return;
        s = com.split("\n");
        for (int i = 0; i < s.length; i++) {
            out.write(';');
            if (s[i] != null && !s[i].isEmpty()) out.write(s[i]);
            out.newLine();
        }
    }

    public void section(String sec) throws IOException {
        String s;
        if (out == null) return;
        if (sec == null)
            throw (new IllegalArgumentException("section is null"));
        s = sec.trim();
        if (s.isEmpty())
            throw (new IllegalArgumentException("section is empty"));
        out.write('[');
        out.write(sec.trim());
        out.write(']');
        out.newLine();
    }

    public void key(String key, String value) throws IOException {
        putKey(key);
        putValue(value);
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
