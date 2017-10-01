package photoComponent.model;

import java.awt.*;

public class TypedText {
    public String text;
    public Font font;
    public Point position; //From top-left corner of the photo
    public Color color;

    public TypedText(String text, Point p) {
        this.text = text;
        position = p;
        font = new Font("Times New Roman", Font.PLAIN, 24);
    }
}
