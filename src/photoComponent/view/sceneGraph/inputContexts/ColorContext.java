package photoComponent.view.sceneGraph.inputContexts;

import java.awt.*;

public class ColorContext implements IInputContext {
    Color color;
    private Color oldColor;

    public ColorContext(Color color){
        this.color = color;
    }

    @Override
    public void applyContext(Graphics g) {
        oldColor = g.getColor();
        g.setColor(color);
    }

    @Override
    public void restoreContext(Graphics g) {
        g.setColor(oldColor);
    }
}
