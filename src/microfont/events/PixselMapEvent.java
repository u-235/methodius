
package microfont.events;

import java.awt.Rectangle;
import java.util.EventObject;
import microfont.PixselMap;

public class PixselMapEvent extends EventObject {
    private static final long serialVersionUID = 4283930318715669061L;
    private int               x, y, width, height;

    public PixselMapEvent(PixselMap source, int x, int y, int width, int height) {
        super(source);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PixselMapEvent(PixselMap source, Rectangle rect) {
        this(source, rect.x, rect.x, rect.width, rect.height);
    }

    public Rectangle rect() {
        return new Rectangle(x, y, width, height);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
