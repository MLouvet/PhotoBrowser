package photoComponent.view.sceneGraph.inputContexts;


import photoComponent.model.PenStatus;

import java.awt.*;

public class StrokeContext extends ColorContext {
    private Size size;
    private Stroke oldStroke;

    public StrokeContext(PenStatus penStatus) {
        super(penStatus.getColor());
        size = penStatus.getSize();
    }

    @Override
    public void applyContext(Graphics g) {
        super.applyContext(g);
        Graphics2D graphics2D = (Graphics2D) g;
        oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(getSize()));
    }

    @Override
    public void restoreContext(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(oldStroke);
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

    public enum Size {
        TINY, SMALL, MEDIUM, LARGE
    }
}
