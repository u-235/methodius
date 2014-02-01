
package microfont.gui;

import java.awt.Font;
import java.awt.Graphics;

/**
 */
public class MSymbolView extends AbstractView {
    public static int         ELEMENT_INDEX_SAMPLE  = 1;
    public static int         ELEMENT_INDEX_CODE    = 2;
    public static int         ELEMENT_INDEX_UNICODE = 3;
    private static final long serialVersionUID      = 1L;
    Font                      fontSample;
    String                    sample                = "s";
    String                    code                  = "0x55";
    String                    unicode               = "U+774";

    public MSymbolView() {
        super(4);
        render().setPixselSize(1);
        setLayout(new MSymbolViewLayout(this));
    }

    @Override
    public void paint(Graphics g) {
        if (code != null)
            g.drawString(code, elementsPos[ELEMENT_INDEX_CODE].x,
                            elementsPos[ELEMENT_INDEX_CODE].y);
        if (unicode != null)
            g.drawString(unicode, elementsPos[ELEMENT_INDEX_UNICODE].x,
                            elementsPos[ELEMENT_INDEX_UNICODE].y);
        g.setFont(fontSample);
        if (sample != null)
            g.drawString(sample, elementsPos[ELEMENT_INDEX_SAMPLE].x,
                            elementsPos[ELEMENT_INDEX_SAMPLE].y);

        super.paint(g);
    }
}
