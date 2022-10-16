
package microfont.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import microfont.AbstractPixselMap;
import microfont.MSymbol;
import microfont.Metrics;

/**
 */
public class MSymbolView extends AbstractView {
    private static final long serialVersionUID = 1L;
    SymbolListener            symListener;
    MSymbol                   symbol;
    Font                      fontSample;
    private String            sample;
    private String            code;
    private String            unicode;
    protected Point           samplePos;
    protected Point           codePos;
    protected Point           unicodePos;

    public MSymbolView() {
        super();
        symListener = new SymbolListener();
        getSymbolRender().setPixselSize(1);
        setOpaque(true);
        setBackground(Color.WHITE);
        samplePos = new Point();
        codePos = new Point();
        unicodePos = new Point();
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

    public void setSampleFont(Font f) {
        fontSample = f;
        revalidate();
    }

    public Font getSampleFont() {
        if (fontSample == null) return getFont();
        return fontSample;
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

        if (code != null) {
            g.drawString(code, codePos.x, codePos.y);
        }

        if (unicode != null) {
            g.drawString(unicode, unicodePos.x, unicodePos.y);
        }

        if (sample != null) {
            g.setFont(fontSample);
            g.drawString(sample, samplePos.x, samplePos.y);
        }
    }

    protected class MSymbolViewLayout implements LayoutManager {
        Dimension pref;
        Dimension renderSize;
        int       renderOffset;
        Dimension sampleSize;
        int       sampleOffset;
        Dimension codeSize;
        int       codeOffset;
        Dimension unicodeSize;
        int       unicodeOffset;

        public MSymbolViewLayout() {
            pref = new Dimension();
            renderSize = new Dimension();
            codeSize = new Dimension();
            unicodeSize = new Dimension();
            sampleSize = new Dimension();
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // не поддерживается
        }

        @Override
        public void removeLayoutComponent(Component comp) {
            // не поддерживается
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            if (!isValid()) {
                calculate();
                layout();
            }
            return pref;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return pref;
        }

        @Override
        public void layoutContainer(Container parent) {
            calculate();
            layout();
        }

        void calculate() {
            Graphics g = getGraphics();
            if (g == null) return;
            FontMetrics fm = g.getFontMetrics(getFont());

            if (code == null) {
                codeSize.width = 0;
                codeSize.height = 0;
                codeOffset = 0;
            } else {
                codeSize.width = fm.stringWidth(code);
                codeSize.height = fm.getHeight();
                codeOffset = fm.getAscent();
            }

            if (unicode == null) {
                unicodeSize.width = 0;
                unicodeSize.height = 0;
                unicodeOffset = 0;
            } else {
                unicodeSize.width = fm.stringWidth(unicode);
                unicodeSize.height = fm.getHeight();
                unicodeOffset = fm.getAscent();
            }

            renderSize.width = getSymbolRender().getWidth();
            renderSize.height = getSymbolRender().getHeight();
            if (owner != null
                            && owner.isMetricActually(Metrics.METRIC_BASELINE)) {
                renderOffset = getSymbolRender().pixselToPointY(
                                owner.getMetric(Metrics.METRIC_BASELINE));
            } else {
                renderOffset = 0;
            }

            if (sample == null) {
                sampleSize.width = 0;
                sampleSize.height = 0;
                sampleOffset = 0;
            } else {
                fm = g.getFontMetrics(getSampleFont());
                sampleSize.width = fm.stringWidth(sample);
                sampleSize.height = fm.getHeight();
                sampleOffset = fm.getAscent();
            }
        }

        void layout() {
            // Выравнивание образца и символа по базовой линии.
            int diff = renderOffset - sampleOffset;
            if (diff > 0) {
                renderPos.y = 0;
                samplePos.y = renderOffset;
            } else {
                renderPos.y = -diff;
                samplePos.y = sampleOffset;
            }

            // Смещаем строки кода и юникода на самый высокий элемент.
            codePos.y = samplePos.y;
            unicodePos.y = samplePos.y;
            diff = sampleSize.height - sampleOffset;
            int delta = renderSize.height - renderOffset;
            if (delta > diff) {
                codePos.y += delta;
                unicodePos.y += delta;
            } else {
                codePos.y += diff;
                unicodePos.y += diff;
            }
            // Добавляем пространство.
            codePos.y += 4; // TODO magic!!
            unicodePos.y += 4;
            // Выравнивание строки кода и юникода по высоте.
            if (codeOffset - unicodeOffset > 0) {
                codePos.y += codeOffset;
                unicodePos.y += codeOffset;
            } else {
                codePos.y += unicodeOffset;
                unicodePos.y += unicodeOffset;
            }

            diff = codeSize.height - codeOffset;
            delta = unicodeSize.height - unicodeOffset;
            pref.height = codePos.y;
            if (diff > delta) {
                pref.height += diff;
            } else {
                pref.height += delta;
            }

            samplePos.x = 0;
            renderPos.x = sampleSize.width + 4;// TODO magic!
            pref.width = renderPos.x + renderSize.width;

            codePos.x = 0;
            unicodePos.x = codeSize.width + 4;// TODO magic!
            pref.width = pref.width > unicodePos.x + unicodeSize.width
                            ? pref.width : unicodePos.x + unicodeSize.width;
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
