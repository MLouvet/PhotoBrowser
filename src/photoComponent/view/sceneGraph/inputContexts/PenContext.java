package photoComponent.view.sceneGraph.inputContexts;

import com.sun.istack.internal.NotNull;

import java.awt.*;

public class PenContext extends ColorContext {
    @NotNull
    private Font font;
    private Font oldFont;

    public PenContext(Color color, Font font){
        super(color);
        this.font = font;
    }

    @Override
    public void applyContext(Graphics g) {
        super.applyContext(g);
        oldFont = g.getFont();
        g.setFont(font);
    }

    @Override
    public void restoreContext(Graphics g) {
        super.restoreContext(g);
        g.setFont(oldFont);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
