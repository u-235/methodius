
package microfont.gui;

import gui.ScrollableWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import microfont.AbstractMFont;
import microfont.MFont;
import microfont.MSymbol;
import microfont.events.PixselMapEvent;
import microfont.render.PixselMapRender;

/**
 * Отрисовщик {@link MSymbol символа} на экране.<img
 * src="../doc-files/render.png" align=right>
 * <p>
 * Этот класс на самом деле ничего не отображает, но предоставляет набор
 * возможностей для разнообразного отображения символов.<br>
 * На рисунке показан символ <b>Д</b> с шириной 12 и высотой 14 пикселей с
 * сеткой и полями.
 * <p>
 * <b>Символ по умолчанию.</b><br>
 * Многие методы используют {@linkplain #symbol символ по умолчанию}. При
 * изменении этого символа вызывается метод
 * {@link #pixselChanged(PixselMapEvent)}, что позволяет оперативно обновлять
 * отображение символа.
 * <p>
 * <b>Отрисовка символа.</b><br>
 * <img src="../doc-files/source-elements.png"><br>
 * Метод
 * {@link #drawSymbol(Graphics, MSymbol, boolean, int, int, int, int, Color, Color)}
 * является базовым, другие методы отрисовки символа вызывают его, подставляя
 * переменные класса. При этом могут использоваться переменные {@link #symbol} ,
 * {@link #ink}, {@link #paper}, {@link #pixselSize}.
 * <p>
 * <b>Отрисовка сетки.</b><br>
 * <p>
 * <b>Отрисовка полей.</b><br>
 * <p>
 * <b>Вспомогательные методы.</b><br>
 * Для определения попадания курсора используется метод
 * {@link #hit(MSymbolHit, int, int, microfont.MSymbol, int, int)}
 */
public class MAbstractComponent extends ScrollableWindow {
    /**/
    private static final long serialVersionUID = 1L;
    /**
     * Символ для отрисовки, используемый по умолчанию.
     * 
     * @see #setSymbol(MSymbol)
     * @see #getSymbol()
     */
    protected MSymbol         symbol;
    protected PixselMapRender render;
    /** Имя шрифта. */
    protected String          charset          = "cp1251";

    @SuppressWarnings("serial")
    public class RenderError extends Exception {
        public RenderError(String s) {
            super(s);
        }

    }

    /**
     * Создание объекта с установленными символом по умолчанию.
     * 
     * @param symbol Символ по умолчанию.
     */
    public MAbstractComponent(MSymbol symbol) {
        render = new PixselMapRender();
        this.setSymbol(symbol);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isFocusable()) requestFocus();
            }
        });
    }

    public MAbstractComponent() {
        this(null);
    }

    /**
     * Метод возвращает текущий {@linkplain #symbol символ} по умолчанию.
     * 
     * @return Текущий символ.
     * 
     * @see #setSymbol(MSymbol)
     */
    public MSymbol getSymbol() {
        return this.symbol;
    }

    /**
     * Метод устанавливает {@linkplain #symbol символ} по умолчанию. Если символ
     * принадлежит {@linkplain MFont шрифту}, то обновляются переменные
     * {@link #marginLeft}, {@link #marginRight} и {@link #charset}. Вызывается
     * метод {@link ScrollableWindow#revalidate() revalidate()}.
     * 
     * @param s Новый символ.
     * 
     * @see #getSymbol()
     */
    public void setSymbol(MSymbol s) {
        symbol = s;

        render.setPixselMap(symbol);
    }

    void updateMargins() {
        MFont owner;

        if (symbol == null) return;
        AbstractMFont amf = symbol.getOwner();
        if (!(amf instanceof MFont)) return;

        owner = (MFont) symbol.getOwner();

        render.setMarginLeft(owner.getMarginLeft());
        render.setMarginRight(owner.getMarginRight());
        render.setBase(owner.getBaseline());
        render.setAscent(owner.getAscent());
        render.setLine(owner.getLine());
        render.setDescent(owner.getDescent());
    }

    /**
     * Метод вычисляет размер, необходимый для отбражения символа. Используются
     * {@linkplain #symbol символ} по умолчанию.
     * 
     * @param d Объект, в котором будет возвращён размер символа.
     * @return Возвращает <b>d</b> с вычисленными размерами. Если <b>d</b> равно
     *         <b>null</b>, то создаётся и возвращается новый объект.
     */
    public Dimension getSymbolSize(Dimension d) {
        Dimension ret;

        if (d == null) ret = new Dimension();
        else ret = d;

        ret.width = render.getWidth();
        ret.height = render.getHeight();

        return ret;
    }

    @Override
    public Dimension calculatePrefSize(Dimension rv) {
        return getSymbolSize(rv);
    }
}
