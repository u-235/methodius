
package microfont.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import microfont.AbstractPixselMap;
import microfont.MSymbol;

/**
 */
public class MSymbolView extends AbstractView {
    public static int         ELEMENT_INDEX_SAMPLE  = 1;
    public static int         ELEMENT_INDEX_CODE    = 2;
    public static int         ELEMENT_INDEX_UNICODE = 3;
    private static final long serialVersionUID      = 1L;
    SymbolListener            symListener;
    MSymbol                   symbol;
    Font                      fontSample;
    private String            sample;
    private String            code;
    private String            unicode;

    public MSymbolView() {
        super(4);
        symListener = new SymbolListener();
        setPixselSize(1);
        setOpaque(true);
        setBackground(Color.WHITE);
        setLayout(new MSymbolViewLayout(this));
    }

    @Override
    public void setPixselMap(AbstractPixselMap apm) {
        if (symbol != null) symbol.removePropertyChangeListener(symListener);

        if (apm instanceof MSymbol) {
            symbol = (MSymbol) apm;
            symbol.addPropertyChangeListener(symListener);
        } else {
            symbol = null;
        }

        updateStrings();

        super.setPixselMap(apm);
    }

    protected void updateStrings() {
        if (symbol == null || !symbol.isUnicode()) {
            sample = null;
            unicode = null;
        } else {
            sample = new String(Character.toChars(symbol.getUnicode()));
            unicode = "U+" + Integer.toHexString(symbol.getUnicode());
        }

        if (symbol != null) code = Integer.toHexString(symbol.getCode());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (code != null)
            g.drawString(code, elementsPos[ELEMENT_INDEX_CODE].x,
                            elementsPos[ELEMENT_INDEX_CODE].y + 20);
        if (unicode != null)
            g.drawString(unicode, elementsPos[ELEMENT_INDEX_UNICODE].x + 40,
                            elementsPos[ELEMENT_INDEX_UNICODE].y + 20);
        g.setFont(fontSample);
        if (sample != null)
            g.drawString(sample, elementsPos[ELEMENT_INDEX_SAMPLE].x,
                            elementsPos[ELEMENT_INDEX_SAMPLE].y + 10);
    }

    protected class SymbolListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateStrings();
            invalidate();
            repaint();
        }
    }
}
