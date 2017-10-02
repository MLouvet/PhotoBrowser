package photoComponent.model;

import java.awt.*;

public class FancyPoint extends Point {
    private Color c;
    private Size size;

    public FancyPoint(Point p, PenStatus status) {
        super(p.x, p.y);
        c = status.getColor();
        size = status.getSize();
    }

    public FancyPoint(int x, int y, PenStatus status) {
        super(x, y);
        this.c = status.getColor();
        size = status.getSize();
    }

    public int getSize() {
        switch (size) {
            case TINY:
                return 3;
            case SMALL:
                return 4;
            case MEDIUM:
                return 8;
            case LARGE:
                return 12;
            default:
                throw new Error("Size not Implemented");
        }
    }

    public Size getEnumSize(){
        return size;
    }

    public void setSize(Size s){
        size = s;
    }

    public Color getColor() {
        return c;
    }

    public enum Size {
        TINY, SMALL, MEDIUM, LARGE
    }

}
