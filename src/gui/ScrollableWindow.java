
package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 * 
 */
public class ScrollableWindow extends JComponent {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * Показывает, являются ли вычисленные размеры действительными. Метод
     * {@linkplain #invalidate()} делает переменную <b>false</b>.
     */
    protected boolean         sizeValid;
    /**
     * Переменная показывает, нужно ли вычислять максимальный размер компонента.
     * 
     * @see #maxSize
     * @see #getMaximumSize()
     */
    protected boolean         maxSizeEnable;
    /**
     * Вычисленный максимальный размер компонента.
     * 
     * @see #maxSizeEnable
     * @see #calculateMaxSize(Dimension)
     * @see #getMaximumSize()
     */
    protected Dimension       maxSize;
    /**
     * Вычисленный минимальный размер компонента.
     * 
     * @see #calculateMinSize(Dimension)
     * @see #getMinimumSize()
     */
    protected Dimension       minSize;
    /**
     * Вычисленный желаемый размер компонента.
     * 
     * @see #calculatePrefSize(Dimension)
     * @see #getPreferredSize()
     */
    protected Dimension       prefSize;

    /**
     * Метод вычисляет максимальный размер компонента. Этот метод должен быть
     * переопределён в потомке для получения актуального размера.
     * 
     * @param rv Объект для хранения размера. Метод {@linkplain #checkSize()}
     *            гарантирует, что <b>rv</b> не <b>null</b>.
     * @return Возвращает <b>rv</b> с вычисленным размером.
     * @see #maxSize
     * @see #getMaximumSize()
     */
    public Dimension calculateMaxSize(Dimension rv) {
        rv.width = Integer.MAX_VALUE;
        rv.height = Integer.MAX_VALUE;
        return rv;
    }

    /**
     * Метод вычисляет минимальный размер компонента. Этот метод должен быть
     * переопределён в потомке для получения актуального размера.
     * 
     * @param rv Объект для хранения размера. Метод {@linkplain #checkSize()}
     *            гарантирует, что <b>rv</b> не <b>null</b>.
     * @return Возвращает <b>rv</b> с вычисленным размером.
     * @see #minSize
     * @see #getMinimumSize()
     */
    public Dimension calculateMinSize(Dimension rv) {
        rv.setSize(0, 0);
        return rv;
    }

    /**
     * Метод вычисляет желаемый размер компонента. Этот метод должен быть
     * переопределён в потомке для получения актуального размера.
     * 
     * @param rv Объект для хранения размера. Метод {@linkplain #checkSize()}
     *            гарантирует, что <b>rv</b> не <b>null</b>.
     * @return Возвращает <b>rv</b> с вычисленным размером.
     * @see #prefSize
     * @see #getPreferredSize()
     */
    public Dimension calculatePrefSize(Dimension rv) {
        return rv;
    }

    /**
     * Метод проверяет переменную {@link #sizeValid} и при необходимости
     * вызывает методы {@linkplain #calculateMaxSize(Dimension)},
     * {@linkplain #calculateMinSize(Dimension)} и
     * {@linkplain #calculatePrefSize(Dimension)} для вычисления правильных
     * размеров.
     * 
     * @see #getMaximumSize()
     * @see #getMinimumSize()
     * @see #getPreferredSize()
     */
    public void checkSize() {
        // synchronized (getTreeLock()) {
        if (sizeValid) return;

        if (maxSize == null) maxSize = new Dimension();
        if (minSize == null) minSize = new Dimension();
        if (prefSize == null) prefSize = new Dimension();

        prefSize = calculatePrefSize(prefSize);
        maxSize = calculateMaxSize(maxSize);
        minSize = calculateMinSize(minSize);
        sizeValid = true;
        // }
    }

    /**
     * Возвращает максимальный размер компонента. Если для компонента
     * максимальный размер был установлен при помощи <b>setMaximumSize()</b> или
     * переменная {@linkplain #maxSizeEnable} равна <b>false</b>, то
     * возвращается предустановленный размер.<br>
     * Иначе вызывается {@linkplain #checkSize()} и возвращается копия
     * вычисленного размер {@linkplain #maxSize}.
     */
    @Override
    public Dimension getMaximumSize() {
        if (super.isMaximumSizeSet() || !maxSizeEnable)
            return super.getMaximumSize();
        checkSize();
        return new Dimension(maxSize);
    }

    /**
     * Возвращает максимальный размер компонента. Если для компонента
     * максимальный размер был установлен при помощи <b>setMinimumSize()</b>, то
     * возвращается предустановленный размер.<br>
     * Иначе вызывается {@linkplain #checkSize()} и возвращается копия
     * вычисленного размер {@linkplain #minSize}.
     */
    @Override
    public Dimension getMinimumSize() {
        if (super.isMinimumSizeSet()) return super.getMinimumSize();
        checkSize();
        return new Dimension(minSize);
    }

    /**
     * Возвращает максимальный размер компонента. Если для компонента
     * максимальный размер был установлен при помощи <b>setPreferredSize()</b>,
     * то возвращается предустановленный размер.<br>
     * Иначе вызывается {@linkplain #checkSize()} и возвращается копия
     * вычисленного размер {@linkplain #prefSize}.
     */
    @Override
    public Dimension getPreferredSize() {
        if (super.isPreferredSizeSet()) return super.getPreferredSize();
        checkSize();
        return new Dimension(prefSize);
    }

    /**
     * 
     */
    @Override
    public void doLayout() {
        checkSize();
    }

    /**
     * Метод делает {@linkplain #sizeValid} равным <b>false</b>.
     * 
     * @see #validate()
     * @see #revalidate()
     */
    @Override
    public void invalidate() {
        sizeValid = false;
        super.invalidate();
    }

    /**
     * Метод вызывает {@linkplain #doLayout()} и {@linkplain #repaint()}.
     * 
     * @see #invalidate()
     * @see #revalidate()
     */
    @Override
    public void validate() {
        doLayout();
        super.validate();
        repaint();
    }

    /**
     * Потомки класса не должны использовать этот метод. Вместо этого
     * используйте метод {@linkplain #paint(Graphics, int, int)}.
     * 
     * @inheritDoc
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle clip = g.getClipBounds();
        g2d.setBackground(getBackground());
        g2d.setColor(getForeground());
        if (!isOpaque()) g.clearRect(clip.x, clip.y, clip.width, clip.height);
        paint(g, 0, 0);
    }

    /**
     * Это пустой метод. Он должен быть переопределён в потомке.
     * 
     * @param g Графический контекст для отображения.
     * @param x Смещение по горизонтали.
     * @param y Смещение по вертикали.
     */
    public void paint(Graphics g, int x, int y) {
    }
}
