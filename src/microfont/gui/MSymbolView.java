
package microfont.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
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
    protected Rectangle       samplePos;
    protected Rectangle       codePos;
    protected Rectangle       unicodePos;

    public MSymbolView() {
        super();
        symListener = new SymbolListener();
        setPixselSize(1);
        setOpaque(true);
        setBackground(Color.WHITE);
        samplePos = new Rectangle();
        codePos = new Rectangle();
        unicodePos = new Rectangle();
        setLayout(new MSymbolViewLayout());
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

        if (code != null) g.drawString(code, codePos.x, codePos.y + 20);
        if (unicode != null)
            g.drawString(unicode, unicodePos.x + 40, unicodePos.y + 20);
        g.setFont(fontSample);
        if (sample != null)
            g.drawString(sample, samplePos.x, samplePos.y + 10);
    }

    protected class MSymbolViewLayout implements LayoutManager {
        Dimension pref;

        public MSymbolViewLayout() {
            pref = new Dimension();
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeLayoutComponent(Component comp) {
            // TODO Auto-generated method stub

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            calculate();
            return pref;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            calculate();
            layout();
        }

        void calculate() {
            pref.width = render().getWidth() + 90;
            pref.height = render().getHeight() + 10;
        }

        void layout() {
            Rectangle pos = renderPos;
            pos.width = render().getWidth();
            pos.height = render().getHeight();

            pos.x = 90;
            pos.y = 10;
        }
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
