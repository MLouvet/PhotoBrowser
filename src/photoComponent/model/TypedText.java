package photoComponent.model;

import java.awt.*;

public class TypedText {
    private String text;
    private Font font;
    private Point position; //From top-left corner of the photo
    private float size;
    private Color color;
    private int style;

    public TypedText(String text) {
        this.text = text;
        font = new Font("Times New Roman", Font.PLAIN, 8);
    }
}
