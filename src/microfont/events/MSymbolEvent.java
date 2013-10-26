package microfont.events;

import utils.event.DataEvent;
import microfont.MSymbol;
import microfont.PixselMap;

public class MSymbolEvent extends DataEvent
{
    /**
     * 
     */
    private static final long serialVersionUID = 1300518002930615341L;
    /** Массив точек был скопирован из другого символа. */
    public static final int   COPY             = 1;
    /** Размер символа изменился. */
    public static final int   SIZE             = 2;
    /** Был изменён пиксель. */
    public static final int   PIXSEL           = 3;
    /** */
    public static final int   INDEX            = 4;
    /**  */
    public static final int   SHIFT            = 5;
    public static final int   UNKNOWN          = 0;

    public PixselMap          source;
    public int                reason;
    public int                y;
    public int                x;
    public int                height;
    public int                width;

    public MSymbolEvent(PixselMap s, int reason, int x, int y, int width,
                    int height) {
        super(s, reason, null, null);
        source = s;
        this.reason = reason;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MSymbolEvent(PixselMap s, int reason) {
        super(s, reason, null, null);
        source = s;
        this.reason = reason;
    }

    public MSymbolEvent(MSymbol s) {
        this(s, UNKNOWN);
    }
}
