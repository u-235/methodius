
package microfont.ls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import utils.ini.Handler;
import utils.ini.Parser;
import utils.ini.Saver;
import microfont.MFont;
import microfont.MSymbol;
import microfont.Metrics;

public class MFontLoadSave {
    protected static final String INFO              = "INFO";
    protected static final String INFO_CHARSET      = "charset";
    protected static final String INFO_NAME         = "name";
    protected static final String INFO_PROTOTIPE    = "prototype";
    protected static final String INFO_DESCRIPTION  = "description";
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

    /**
     * Сохраняет шрифт в заданном файле.
     * 
     * @param svr Файл для записи шрифта.
     * @throws NullPointerException Если файл <b>svr</b> равен <b>null</b>.
     * @throws IOException
     */
    public static void save(MFont mFont, Saver svr, MFontSaveProgress progress)
                    throws NullPointerException, IOException {
        MSymbol sym;
        int w, last;
        byte[] arr;

        if (svr == null) throw (new IllegalArgumentException("file is null"));

        svr.comment(" This is MFont file. Version 0.8"
                        + "\n If yuor need edit this file use \"Methodius\".");

        svr.section(INFO);
        svr.key(INFO_CHARSET, mFont.getCodePage());
        svr.key(INFO_NAME, mFont.getName());
        svr.key(INFO_PROTOTIPE, mFont.getPrototype());
        svr.key(INFO_DESCRIPTION, mFont.getDescriptin());
        svr.key(INFO_SIZE, Integer.toString(mFont.length()));

        if (mFont.isFixsed()) {
            svr.key(INFO_FIXSED, "true");
        } else {
            svr.key(INFO_FIXSED, "false");
        }

        svr.key(INFO_WIDTH, Integer.toString(mFont.getWidth()));
        svr.key(INFO_HEIGHT, Integer.toString(mFont.getHeight()));
        svr.key(INFO_BASELINE, Integer.toString(mFont
                        .getMetric(Metrics.METRIC_BASELINE)));
        svr.key(INFO_ASCENT, Integer.toString(mFont
                        .getMetric(Metrics.METRIC_ASCENT)));
        svr.key(INFO_LINE,
                        Integer.toString(mFont.getMetric(Metrics.METRIC_LINE)));
        svr.key(INFO_DESCENT, Integer.toString(mFont
                        .getMetric(Metrics.METRIC_DESCENT)));
        svr.key(INFO_LEFT_MARGIN,
                        Integer.toString(mFont.getMetric(Metrics.METRIC_LEFT)));
        svr.key(INFO_RIGHT_MARGIN,
                        Integer.toString(mFont.getMetric(Metrics.METRIC_RIGHT)));

        svr.comment(" Byte arrays of synbol in hex radix.");
        svr.section(SYMBOLS);
        last = -1;

        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < mFont.length(); i++) {
            sym = mFont.symbolByIndex(i);
            last++;
            if (last != sym.getCode()) {
                svr.key(SYMBOLS_CODE, Integer.toString(sym.getCode()));
            }
            last = sym.getCode();

            w = sym.getWidth();
            if (!mFont.isFixsed()) {
                svr.key(SYMBOLS_WIDTH, Integer.toString(w));
            }

            arr = sym.getByteArray();
            buff.delete(0, buff.length());
            for (byte bt : arr) {
                if (buff.length() > 0) buff.append(" ");
                buff.append(Integer.toHexString(bt & 0x00ff));
            }
            svr.key(SYMBOLS_BYTES, buff.toString());
        }
    }

    public static void save(MFont mFont, Saver svr)
                    throws NullPointerException, IOException {
        save(mFont, svr, null);
    }

    public static MFont load(File f, MFontLoadProgress progress)
                    throws IOException {
        FileInputStream inp = new FileInputStream(f);

        FontHandler fhandler = new FontHandler();
        try {
            Parser.parse(inp, fhandler, null);
        } catch (IOException e) {
            inp.close();
            throw (e);
        }

        inp.close();
        return fhandler.font;
    }

    public static MFont load(File f) throws IOException {
        return load(f, null);
    }

    static class FontHandler implements Handler {
        MFont  font    = new MFont();
        int    section = 0;
        String key     = null;
        int    code    = 0;
        int    width   = 0;
        int    height  = 0;

        @Override
        public void value(String value) {
            if (key == null) return;

            if (section == 1) {
                if (key.equalsIgnoreCase(INFO_ASCENT)) {
                    font.setMetric(Metrics.METRIC_ASCENT,
                                    Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_ASCENT, true);
                } else if (key.equalsIgnoreCase(INFO_BASELINE)) {
                    font.setMetric(Metrics.METRIC_BASELINE,
                                    Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_BASELINE, true);
                } else if (key.equalsIgnoreCase(INFO_DESCENT)) {
                    font.setMetric(Metrics.METRIC_DESCENT,
                                    Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_DESCENT, true);
                } else if (key.equalsIgnoreCase(INFO_LEFT_MARGIN)) {
                    font.setMetric(Metrics.METRIC_LEFT, Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_LEFT, true);
                } else if (key.equalsIgnoreCase(INFO_LINE)) {
                    font.setMetric(Metrics.METRIC_LINE, Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_LINE, true);
                } else if (key.equalsIgnoreCase(INFO_RIGHT_MARGIN)) {
                    font.setMetric(Metrics.METRIC_RIGHT,
                                    Integer.parseInt(value));
                    font.setMetricActually(Metrics.METRIC_RIGHT, true);
                } else if (key.equalsIgnoreCase(INFO_DESCRIPTION)) {
                    font.setDescriptin(value);
                } else if (key.equalsIgnoreCase(INFO_NAME)) {
                    font.setName(value);
                } else if (key.equalsIgnoreCase(INFO_PROTOTIPE)) {
                    font.setPrototype(value);
                } else if (key.equalsIgnoreCase(INFO_CHARSET)) {
                    font.setCodePage(value);
                } else if (key.equalsIgnoreCase(INFO_FIXSED)) {
                    font.setFixsed(value.equalsIgnoreCase("true"));
                } else if (key.equalsIgnoreCase(INFO_HEIGHT)) {
                    height = Integer.parseInt(value);
                    font.setHeight(height);
                } else if (key.equalsIgnoreCase(INFO_WIDTH)) {
                    width = Integer.parseInt(value);
                    font.setWidth(width);
                }
            } else if (section == 2) {
                if (key.equalsIgnoreCase(SYMBOLS_CODE)) {
                    code = Integer.parseInt(value);
                } else if (key.equalsIgnoreCase(SYMBOLS_WIDTH)) {
                    width = Integer.parseInt(value);
                } else if (key.equalsIgnoreCase(SYMBOLS_BYTES)) {
                    StringTokenizer st = new StringTokenizer(value, " ", false);
                    byte[] bytes = new byte[st.countTokens()];
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        bytes[i] = (byte) Short.parseShort(st.nextToken(), 16);
                    }

                    font.add(new MSymbol(code, width, height, bytes));
                    code++;
                }
            } else {
                // TODO handle error
            }
        }

        @Override
        public void section(String sec) {
            key = null;
            switch (section) {
            case 0:
                if (INFO.equals(sec)) {
                    section = 1;
                } else {
                    // TODO handle error
                }
                break;
            case 1:
                if (SYMBOLS.equals(sec)) {
                    section = 2;
                } else {
                    // TODO handle error
                }
                break;
            default:
                // TODO handle error
            }
        }

        @Override
        public void key(String k) {
            if (section == 1 || section == 2) key = k;
        }

        @Override
        public void error(int state, int ch, int line, int col) {
            // TODO handle error
        }

        @Override
        public void comment(String com) {
            // nop
        }
    }
}
