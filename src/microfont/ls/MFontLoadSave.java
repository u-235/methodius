
package microfont.ls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import microfont.MFont;
import microfont.MSymbol;
import microfont.Metrics;

public class MFontLoadSave {
    protected static final String AUTHOR            = "AUTHOR";
    protected static final String AUTHOR_NAME       = "name";
    protected static final String AUTHOR_CONTACT    = "mail";
    protected static final String INFO              = "INFO";
    protected static final String INFO_CHARSET      = "charset";
    protected static final String INFO_NAME         = "name";
    protected static final String INFO_PROTOTIPE    = "prototype";
    protected static final String INFO_SIZE         = "size";
    protected static final String INFO_FIXSED       = "fixsed";
    protected static final String INFO_WIDTH        = "width";
    protected static final String INFO_HEIGHT       = "height";
    protected static final String INFO_BASELINE     = "baseline";
    protected static final String INFO_ASCENT       = "ascent";
    protected static final String INFO_LINE         = "line";
    protected static final String INFO_DESCENT      = "descent";
    protected static final String INFO_LEFT_MARGIN  = "leftMargin";
    protected static final String INFO_RIGHT_MARGIN = "rightMargin";
    protected static final String SYMBOLS           = "SYMBOLS";
    protected static final String SYMBOLS_CODE      = "code";
    protected static final String SYMBOLS_WIDTH     = "width";
    protected static final String SYMBOLS_BYTES     = "bytes";

    private static void writeNewLine(BufferedWriter writer) throws IOException {
        writer.write("\r\n");
    }

    private static void writeComment(BufferedWriter writer, String comment)
                    throws IOException {
        writer.write(";");
        writer.write(comment);
        writer.write("\r\n");
    }

    private static void writeSection(BufferedWriter writer, String section)
                    throws IOException {
        writer.write("[");
        writer.write(section);
        writer.write("]");
        writer.write("\r\n");
    }

    private static void writeKey(BufferedWriter writer, String key)
                    throws IOException {
        writer.write(key);
        writer.write(" = ");
    }

    private static void writeKey(BufferedWriter writer, String key, String value)
                    throws IOException {
        if (value == null) return;
        writeKey(writer, key);
        writer.write(value);
        writer.write("\r\n");
    }

    private static void writeKey(BufferedWriter writer, String key, int value)
                    throws IOException {
        writeKey(writer, key, Integer.toString(value));
    }

    /**
     * Сохраняет шрифт в заданном файле.
     * 
     * @param f Файл для записи шрифта.
     * @throws IllegalArgumentException Если файл <b>f</b> равен <b>null</b>.
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SecurityException
     * @see #save(MFont, String)
     */
    public static void save(MFont mFont, File f)
                    throws IllegalArgumentException, IOException,
                    FileNotFoundException, SecurityException {
        BufferedWriter writer;
        MSymbol sym;
        int w, last;
        byte[] arr;

        if (f == null) throw (new IllegalArgumentException("file is null"));

        writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(f), "utf-8"));

        writeComment(writer, "   This is MFont file. Version 0.8");
        writeComment(writer,
                        "   If yuor need edit this file use \"Methodius\".");
        writeNewLine(writer);

        writeSection(writer, AUTHOR);
        writeKey(writer, AUTHOR_NAME, mFont.getAuthor());
        writeNewLine(writer);

        writeSection(writer, INFO);
        writeKey(writer, INFO_CHARSET, mFont.getCodePage());
        writeKey(writer, INFO_NAME, mFont.getName());
        writeKey(writer, INFO_PROTOTIPE, mFont.getPrototype());
        writeKey(writer, INFO_SIZE, mFont.length());

        writeKey(writer, INFO_FIXSED);
        if (mFont.isFixsed()) {
            writer.write("true");
        } else writer.write("false");
        writeNewLine(writer);

        writeKey(writer, INFO_WIDTH, mFont.getWidth());
        writeKey(writer, INFO_HEIGHT, mFont.getHeight());
        writeKey(writer, INFO_BASELINE,
                        mFont.getMetric(Metrics.METRIC_BASELINE));
        writeKey(writer, INFO_ASCENT, mFont.getMetric(Metrics.METRIC_ASCENT));
        writeKey(writer, INFO_LINE, mFont.getMetric(Metrics.METRIC_LINE));
        writeKey(writer, INFO_DESCENT, mFont.getMetric(Metrics.METRIC_DESCENT));
        writeKey(writer, INFO_LEFT_MARGIN, mFont.getMetric(Metrics.METRIC_LEFT));
        writeKey(writer, INFO_RIGHT_MARGIN,
                        mFont.getMetric(Metrics.METRIC_RIGHT));
        writeNewLine(writer);

        writeComment(writer, "Byte arrays of synbol in hex radix.");
        writeSection(writer, SYMBOLS);
        last = -1;
        for (int i = 0; i < mFont.length(); i++) {
            sym = mFont.symbolByIndex(i);
            last++;
            if (last != sym.getCode()) {
                writeKey(writer, SYMBOLS_CODE, sym.getCode());
            }
            last = sym.getCode();

            w = sym.getWidth();
            if (!mFont.isFixsed()) {
                writeKey(writer, SYMBOLS_WIDTH, w);
            }

            arr = sym.getByteArray();
            writeKey(writer, SYMBOLS_BYTES);
            for (byte bt : arr) {
                writer.write(" " + Integer.toHexString(bt & 0x00ff));
            }

            writeNewLine(writer);
        }

        writeNewLine(writer);
        writer.close();
    }

    /**
     * 
     * @param fname
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SecurityException
     */
    public void save(MFont mFont, String fname)
                    throws IllegalArgumentException, IOException,
                    FileNotFoundException, SecurityException {
        if (fname == null)
            throw (new IllegalArgumentException("file name is null"));
        save(mFont, new File(fname));
    }

    static private String getKey(String str) {
        String ret = "";
        int i;

        i = str.indexOf('=');
        if (i >= 0) ret = str.substring(0, i).trim();

        return ret;
    }

    static private String getKeyValue(String str) {
        String ret = "";
        int i;

        i = str.indexOf('=') + 1;
        if (i > 0 && i < str.length()) ret = str.substring(i).trim();

        return ret;
    }

    static public MFont load(File f, MFontLoadProgress progress)
                    throws IOException {
        MFont ret;
        FileInputStream inpStream;
        BufferedReader reader;
        String str;
        String section = "";
        String key, value;
        int width = 0, index = 0, size = 0, height = -1;
        byte[] bytes;

        inpStream = new FileInputStream(f);

        reader = new BufferedReader(new InputStreamReader(inpStream, "utf-8"));
        ret = new MFont();

        int line = 0;
        while ((str = reader.readLine()) != null) {
            line++;
            if (progress != null && progress.parseString(line)) break;

            str = str.trim();
            if (str.length() == 0) continue;
            if (str.charAt(0) == ';') continue;

            if (str.charAt(0) == '[') {
                section = str.substring(1, str.indexOf(']'));
                if (section.compareToIgnoreCase(SYMBOLS) == 0
                                && (size < 0 || height < 0)) {
                    reader.close();
                    return null;
                }
                continue;
            }

            key = getKey(str);
            value = getKeyValue(str);
            if (key.isEmpty() || value.isEmpty()) continue;

            if (section.compareToIgnoreCase(AUTHOR) == 0) {
                if (key.compareToIgnoreCase(AUTHOR_CONTACT) == 0)
                    ret.setAuthor(ret.getAuthor() + value);
                if (key.compareToIgnoreCase(AUTHOR_NAME) == 0)
                    ret.setAuthor(value + ret.getAuthor());
                continue;
            }

            if (section.compareToIgnoreCase(INFO) == 0) {
                if (key.compareToIgnoreCase(INFO_CHARSET) == 0) ret
                                .setCodePage(value);
                else if (key.compareToIgnoreCase(INFO_NAME) == 0) {
                    ret.setName(value);
                } else if (key.compareToIgnoreCase(INFO_PROTOTIPE) == 0) {
                    ret.setPrototype(value);
                } else if (key.compareToIgnoreCase(INFO_FIXSED) == 0) {
                    if (value.compareToIgnoreCase("true") == 0) {
                        ret.setFixsed(true);
                    } else ret.setFixsed(false);
                } else if (key.compareToIgnoreCase(INFO_WIDTH) == 0) {
                    if (value.length() != 0) width = Integer.parseInt(value);
                    ret.setWidth(width);
                } else if (key.compareToIgnoreCase(INFO_HEIGHT) == 0) {
                    if (value.length() != 0) height = Integer.parseInt(value);
                    ret.setHeight(height);
                } else if (key.compareToIgnoreCase(INFO_BASELINE) == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_BASELINE,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_BASELINE, true);
                    }
                } else if (key.compareToIgnoreCase(INFO_ASCENT) == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_ASCENT,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_ASCENT, true);
                    }
                } else if (key.compareToIgnoreCase(INFO_LINE) == 0
                                || key.compareToIgnoreCase("ascentCapital") == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_LINE,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_LINE, true);
                    }
                } else if (key.compareToIgnoreCase(INFO_DESCENT) == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_DESCENT,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_DESCENT, true);
                    }
                } else if (key.compareToIgnoreCase(INFO_LEFT_MARGIN) == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_LEFT,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_LEFT, true);
                    }
                } else if (key.compareToIgnoreCase(INFO_RIGHT_MARGIN) == 0) {
                    if (value.length() != 0) {
                        ret.setMetric(Metrics.METRIC_RIGHT,
                                        Integer.parseInt(value));
                        ret.setMetricActually(Metrics.METRIC_RIGHT, true);
                    }
                }
                continue;
            }

            if (section.compareToIgnoreCase(SYMBOLS) == 0) {
                if (key.compareToIgnoreCase(SYMBOLS_CODE) == 0
                                || key.compareToIgnoreCase("index") == 0) {
                    index = Integer.parseInt(value);
                } else if (key.compareToIgnoreCase(SYMBOLS_WIDTH) == 0) {
                    width = Integer.parseInt(value);
                } else if (key.compareToIgnoreCase(SYMBOLS_BYTES) == 0) {
                    bytes = new byte[(width * height + 7) / 8];

                    int n = 0;
                    boolean space = true;
                    for (int i = 0; i < value.length(); i++) {
                        if (n >= bytes.length) break;
                        char c = value.charAt(i);
                        if (c >= '0' && c <= '9') {
                            bytes[n] = (byte) (bytes[n] * 16 + c - '0');
                            space = false;
                        } else if (c >= 'a' && c <= 'f') {
                            bytes[n] = (byte) (bytes[n] * 16 + c - 'a' + 10);
                            space = false;
                        } else if (c >= 'A' && c <= 'F') {
                            bytes[n] = (byte) (bytes[n] * 16 + c - 'A' + 10);
                            space = false;
                        } else if (!space) {
                            space = true;
                            n++;
                        }
                    }

                    ret.add(new MSymbol(index, width, height, bytes));
                    index++;
                }
            }
        }
        reader.close();

        return ret;
    }

    public static MFont load(String fname, MFontLoadProgress progress)
                    throws IOException {
        return load(new File(fname), progress);
    }
}
