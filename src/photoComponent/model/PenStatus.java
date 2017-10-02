package photoComponent.model;

import java.awt.*;

public class PenStatus {
    private Font font;
    private Color color;
    private FancyPoint.Size size;

    public PenStatus() {
        font = new Font("Times New Roman", Font.PLAIN, 24);
        size = FancyPoint.Size.MEDIUM;
        color = Color.BLACK;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public FancyPoint.Size getSize() {
        return size;
    }

    public void setSize(FancyPoint.Size size) {
        this.size = size;
    }
}
