
package microfont.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.nio.charset.Charset;
import javax.swing.Timer;
import microfont.MSymbol;

/**
 */
public class MSymbolView extends MAbstractComponent {
    private static final long serialVersionUID = 1L;
    /**  */
    protected Charset         lang;
    /**  */
    protected String          sample           = "";
    /**  */
    protected String          number           = "";
    /**  */
    protected Color           sampleColor      = new Color(0, 0, 192);
    /**  */
    protected Color           numberColor      = new Color(0, 176, 0);
    /**  */
    protected Font            sampleFont;
    /**  */
    protected Font            numberFont;
    /**  */
    protected int             spacing          = 4;

    /**  */
    protected Dimension       symbolSize       = new Dimension();
    /**  */
    protected Dimension       sampleSize       = new Dimension();
    /**  */
    protected Dimension       numberSize       = new Dimension();
    /**  */
    protected Point           symbolPos        = new Point();
    /**  */
    protected Point           samplePos        = new Point();
    /**  */
    protected Point           numberPos        = new Point();

    private Timer             delay;
    private int               delTop, delBottom, delLeft, delRight;

    public MSymbolView(MSymbol symbol) {
        super(symbol);
        super.render.setPixselSize(1);
        sampleFont = new Font("Courier", Font.BOLD, 16);
        numberFont = new Font("Courier New", Font.PLAIN, 16);
        setBackground(new Color(247, 247, 255));

        delay = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (this) {
                    repaint(delLeft, delTop, delRight - delLeft + 1, delBottom
                                    - delTop + 1);
                    delBottom = -1;
                    delRight = -1;
                    delTop = Integer.MAX_VALUE;
                    delLeft = Integer.MAX_VALUE;
                }
            }
        });
        delay.setRepeats(false);
    }

    public MSymbolView() {
        this(null);
    }

    protected String getSample(int ind, Charset lang) {
        byte[] bts = new byte[4];
        int byteNum;

        byteNum = 1;
        for (int i = 0; i < 4; i++) {
            bts[i] = (byte) ind;
            if (bts[i] != 0) byteNum = i + 1;
            ind >>= 8;
        }

        return new String(bts, 0, byteNum, lang);
    }

    protected String getNumber(int ind) {
        String ret;

        ret = Integer.toString(ind, 16);
        if (ind < 16) ret = "0x0" + ret;
        else ret = "0x" + ret;

        return ret;
    }

    protected void updateStrings() {
        if (symbol == null) {
            number = "";
            sample = "";
            return;
        }

        try {
            lang = Charset.forName(charset);
        } catch (java.lang.IllegalArgumentException ex) {
            lang = Charset.defaultCharset();
        }

        sample = getSample(symbol.getCode(), lang);
        number = getNumber(symbol.getCode());
    }

    protected void checkCellSize() {
        FontMetrics fm;

        symbolSize = getSymbolSize(symbolSize);

        fm = getFontMetrics(sampleFont);
        sampleSize.height = fm.getHeight();
        sampleSize.width = fm.stringWidth(sample);
        samplePos.y = fm.getAscent();

        fm = getFontMetrics(numberFont);
        numberSize.height = fm.getHeight();
        numberSize.width = fm.stringWidth(number);
        numberPos.y = fm.getAscent();
    }

    @Override
    public Dimension calculatePrefSize(Dimension rv) {
        int w, h;

        if (symbol == null) {
            rv.setSize(40, 40);
            return rv;
        }

        checkCellSize();

        w = (numberSize.width > sampleSize.width) ? numberSize.width
                        : sampleSize.width;
        w += spacing * 3;
        w += symbolSize.width;

        h = spacing + numberSize.height + sampleSize.height;
        h = (h > symbolSize.height) ? h : symbolSize.height;
        h += spacing * 2;

        rv.setSize(w, h);
        return rv;
    }

    // @Override
    // public Dimension calculateMaxSize(Dimension rv) {
    // rv.setSize(prefSize);
    // return rv;
    // }

    @Override
    public void doLayout() {
        int w;
        if (symbol == null) return;

        checkCellSize();

        w = (numberSize.width > sampleSize.width) ? numberSize.width
                        : sampleSize.width;

        samplePos.x = spacing;
        numberPos.x = spacing;
        symbolPos.x = spacing + spacing + w;

        samplePos.y += spacing;
        numberPos.y += spacing + spacing + sampleSize.height;
        symbolPos.y = spacing;
    }

    @Override
    public void setSymbol(MSymbol s) {
        super.setSymbol(s);
        updateStrings();
    }

    public void drawCell(Graphics g, int X, int Y) {
        Color oldColor;

        oldColor = g.getColor();

        g.setColor(getForeground());// (sampleColor);
        g.setFont(sampleFont);
        g.drawString(sample, X + samplePos.x, Y + samplePos.y);
        // g.setColor(numberColor);
        g.setFont(getFont());
        g.drawString(number, X + numberPos.x, Y + numberPos.y);
        render.paint(g, X + symbolPos.x, Y + symbolPos.y);

        g.setColor(oldColor);
    }

    @Override
    public void paint(Graphics g, int X, int Y) {
        if (symbol == null) return;
        drawCell(g, X, Y);
    }
}
