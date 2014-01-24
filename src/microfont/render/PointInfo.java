
package microfont.render;

public class PointInfo {
    int                     x;
    int                     y;
    boolean                 pixsel;
    boolean                 deadZone;
    boolean                 space;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPixsel() {
        return pixsel;
    }

    public boolean isDeadZone() {
        return deadZone;
    }

    boolean isSpace() {
        return space;
    }
    
    public boolean isOutSide(){
        return !pixsel && !space;
    }
}
