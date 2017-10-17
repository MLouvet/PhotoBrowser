package photoComponent.view.sceneGraph;

import java.awt.*;

public class Bounds {
    public final int x, y, width, height;   //Public final access to gain easy access but prevent direct modification
    //and to allow setters to raise event chains

    public Bounds() {
        x = y = width = height = 0;
    }

    public Bounds(Bounds b) {
        x = b.x;
        y = b.y;
        width = b.width;
        height = b.height;
    }

    public Bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Bounds(Rectangle r) {
        x = r.x;
        y = r.y;
        width = r.width;
        height = r.height;
    }

    public boolean contains(Bounds b) {
        if (b == null || this.isEmpty()) //Infinite boundaries if null
            return false;
        return contains(b.x, b.y) && contains(b.x + b.width, b.y + b.height);
    }

    public boolean isEmpty() {
        return x <= 0 || y <= 0 || width <= 0 || height <= 0;
    }

    private boolean contains(int x, int y) {
        return this.x <= x && x <= this.x + width
                && this.y <= y && y <= this.y + height;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj || this.hashCode() == obj.hashCode())
            return true;
        if (!(obj instanceof Bounds))
            return false;

        Bounds b = (Bounds) obj;

        return width == b.width && height == b.height
                && x == b.x && y == b.y;
    }

    public Bounds enlargeBounds(Bounds newBound) {
        int minX = Math.min(x, newBound.x);
        int minY = Math.min(y, newBound.y);
        int maxX = Math.max(x + width, newBound.x + newBound.width);
        int maxY = Math.max(y + height, newBound.y + newBound.height);

        return new Bounds(minX, minY, maxX, maxY);
    }
}
