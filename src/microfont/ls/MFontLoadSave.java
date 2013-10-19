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

public class MFontLoadSave
{
    protected static final String AUTHOR              = "AUTHOR";
    protected static final String AUTHOR_NAME         = "name";
    protected static final String AUTHOR_CONTACT      = "mail";
    protected static final String INFO                = "INFO";
    protected static final String INFO_CHARSET        = "charset";
    protected static final String INFO_NAME           = "name";
    protected static final String INFO_PROTOTIPE      = "prototype";
    protected static final String INFO_SIZE           = "size";
    protected static final String INFO_FIXSED         = "fixsed";
    protected static final String INFO_WIDTH          = "width";
    protected static final String INFO_HEIGHT         = "height";
    protected static final String INFO_BASELINE       = "baseline";
    protected static final String INFO_ASCENT         = "ascent";
    protected static final String INFO_ASCENT_CAPITAL = "ascentCapital";
    protected static final String INFO_DESCENT        = "descent";
    protected static final String INFO_LEFT_MARGIN    = "leftMargin";
    protected static final String INFO_RIGHT_MARGIN   = "rightMargin";
    protected static final String SYMBOLS             = "SYMBOLS";
    protected static final String SYMBOLS_INDEX       = "index";
    protected static final String SYMBOLS_WIDTH       = "width";
    protected static final String SYMBOLS_BYTES       = "bytes";

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
        String str;

        if (f == null) throw (new IllegalArgumentException("file is null"));

        writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(f), "utf-8"));

        writer.write("; This is MFont file. Version 0.8\r\n");
        writer.write("; If yuor want edit thus use \"Mithodius\". \r\n");

        writer.write("\r\n[" + AUTHOR + "]\r\n");
        writer.write(AUTHOR_NAME + " = ");
        str = mFont.getAuthorName();
        if (str != null) writer.write(str);
        writer.write("\r\n");
        writer.write(AUTHOR_CONTACT + " = ");
        str = mFont.getAuthorMail();
        if (str != null && str != null) writer.write(str);
        writer.write("\r\n");

        writer.write("\r\n[" + INFO + "]\r\n");

        writer.write(INFO_CHARSET + " = ");
        str = mFont.getCharset();
        if (str != null) writer.write(str);
        writer.write("\r\n");

        writer.write(INFO_NAME + " = ");
        str = mFont.getName();
        if (str != null) writer.write(str);
        writer.write("\r\n");

        writer.write(INFO_PROTOTIPE + " = ");
        str = mFont.getPrototype();
        if (str != null) writer.write(str);
        writer.write("\r\n");

        writer.write(INFO_SIZE + " = ");
        writer.write(Integer.toString(mFont.getSize()));
        writer.write("\r\n");

        writer.write(INFO_FIXSED + " = ");
        if (mFont.isFixsed()) {
            writer.write("true\r\n");
        }
        else writer.write("false\r\n");

        writer.write(INFO_WIDTH + " = " + mFont.getWidth() + "\r\n");

        writer.write(INFO_HEIGHT + " = " + mFont.getHeight() + "\r\n");

        writer.write(INFO_BASELINE + " = " + mFont.getBaseline() + "\r\n");

        writer.write(INFO_ASCENT + " = " + mFont.getAscent() + "\r\n");

        writer.write(INFO_ASCENT_CAPITAL + " = " + mFont.getAscentCapital()
                        + "\r\n");

        writer.write(INFO_DESCENT + " = " + mFont.getDescent() + "\r\n");

        writer.write(INFO_LEFT_MARGIN + " = " + mFont.getMarginLeft() + "\r\n");

        writer.write(INFO_RIGHT_MARGIN + " = " + mFont.getMarginRight()
                        + "\r\n");

        writer.write("\n;Byte arrays of synbol in hex radix.\r\n");
        writer.write("[" + SYMBOLS + "]\r\n");
        last = -1;
        for (int i = 0; i < mFont.getSize(); i++) {
            sym = mFont.symbolAtNumber(i);
            last++;
            if (last != sym.getIndex())
                writer.write(SYMBOLS_INDEX + " = " + sym.getIndex() + "\r\n");
            last = i;

            w = sym.getPixsels().getWidth();
            if (!mFont.isFixsed())
                writer.write(SYMBOLS_WIDTH + " = " + w + "\r\n");

            arr = sym.getByteArray();
            writer.write(SYMBOLS_BYTES + " =");
            for (byte bt : arr) {
                writer.write(" " + Integer.toHexString(bt & 0x00ff));
            }

            writer.write("\r\n");
        }

        writer.write("\r\n");
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
        int sectCount = 0;
        String key, value;
        int width = 0, index = 0, size = 0, height = -1;//
        int start, end;
        byte[] bytes;

        inpStream = new FileInputStream(f);

        reader = new BufferedReader(new InputStreamReader(inpStream, "utf-8"));
        ret = new MFont();

        while ((str = reader.readLine()) != null) {
            if (progress != null) {
                int st = (index) * 90 / size;
                if (st <= 0) {
                    st = sectCount * 2;
                }
                else st += 11;

                if (progress.parseString(st)) break;
            }
            str = str.trim();
            if (str.length() == 0) continue;

            start = str.indexOf(';');
            if (start >= 0) {
                str = str.substring(start);
                str = str.trim();
                if (str.length() == 0) continue;
            }

            if (str.charAt(0) == '[') {
                section = str.substring(1, str.indexOf(']'));
                if (section.compareToIgnoreCase(SYMBOLS) == 0
                                && (size < 0 || height < 0)) return null;
                sectCount++;
                continue;
            }

            key = getKey(str);
            value = getKeyValue(str);

            if (section.compareToIgnoreCase(AUTHOR) == 0) {
                if (key.compareToIgnoreCase(AUTHOR_CONTACT) == 0)
                    ret.setAuthorMail(value);
                if (key.compareToIgnoreCase(AUTHOR_NAME) == 0)
                    ret.setAuthorName(value);
                continue;
            }

            if (section.compareToIgnoreCase(INFO) == 0) {
                if (key.compareToIgnoreCase(INFO_CHARSET) == 0) ret
                                .setCharset(value);
                else if (key.compareToIgnoreCase(INFO_NAME) == 0) {
                    ret.setName(value);
                }
                else if (key.compareToIgnoreCase(INFO_PROTOTIPE) == 0) {
                    ret.setPrototype(value);
                }
                else if (key.compareToIgnoreCase(INFO_FIXSED) == 0) {
                    if (value.compareToIgnoreCase("true") == 0) {
                        ret.setFixsed(true);
                    }
                    else ret.setFixsed(false);
                }
                else if (key.compareToIgnoreCase(INFO_WIDTH) == 0) {
                    if (value.length() != 0) width = Integer.parseInt(value);
                    ret.setWidth(width);
                }
                else if (key.compareToIgnoreCase(INFO_HEIGHT) == 0) {
                    if (value.length() != 0) height = Integer.parseInt(value);
                    ret.setHeight(height);
                }
                else if (key.compareToIgnoreCase(INFO_BASELINE) == 0) {
                    if (value.length() != 0)
                        ret.setBaseline(Integer.parseInt(value));
                }
                else if (key.compareToIgnoreCase(INFO_ASCENT) == 0) {
                    if (value.length() != 0)
                        ret.setAscent(Integer.parseInt(value));
                }
                else if (key.compareToIgnoreCase(INFO_ASCENT_CAPITAL) == 0) {
                    if (value.length() != 0)
                        ret.setAscentCapital(Integer.parseInt(value));
                }
                else if (key.compareToIgnoreCase(INFO_DESCENT) == 0) {
                    if (value.length() != 0)
                        ret.setDescent(Integer.parseInt(value));
                }
                else if (key.compareToIgnoreCase(INFO_LEFT_MARGIN) == 0) {
                    if (value.length() != 0)
                        ret.setMarginLeft(Integer.parseInt(value));
                }
                else if (key.compareToIgnoreCase(INFO_RIGHT_MARGIN) == 0) {
                    if (value.length() != 0)
                        ret.setMarginRight(Integer.parseInt(value));
                }
                continue;
            }

            if (section.compareToIgnoreCase(SYMBOLS) == 0) {
                if (key.compareToIgnoreCase(SYMBOLS_INDEX) == 0) {
                    index = Integer.parseInt(value);
                }
                else if (key.compareToIgnoreCase(SYMBOLS_WIDTH) == 0) {
                    width = Integer.parseInt(value);
                }
                else if (key.compareToIgnoreCase(SYMBOLS_BYTES) == 0) {
                    bytes = new byte[(width * height + 7) / 8];

                    start = 0;
                    for (int i = 0; true; i++) {
                        if (i >= bytes.length) break;
                        end = value.indexOf(' ', start);
                        if (end < 0) end = value.length();
                        if (start >= end) break;
                        bytes[i] = (byte) (Integer.parseInt(
                                        value.substring(start, end), 16) & 0xff);
                        start = end + 1;
                        // if (start >= value.length()) break;
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
