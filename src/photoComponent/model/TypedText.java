package photoComponent.model;

import java.awt.*;

public class TypedText {
    private String text;
    private Font font;
    private Point position; //From top-left corner of the photo
    private Color color;

    public TypedText(String text, Point p, PenStatus status) {
        this.text = text;
        position = p;
        font = status.getFont();
        color = status.getColor();
    }



    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public void addCharacter(Character c){
        text += c;
    }
}
