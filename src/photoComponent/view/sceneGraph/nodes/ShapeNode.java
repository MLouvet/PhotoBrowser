package photoComponent.view.sceneGraph.nodes;

import com.sun.istack.internal.NotNull;
import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.StrokeContext;

import java.awt.*;

public class ShapeNode extends AbstractNode {
    private Shape shape;

    public ShapeNode(@NotNull Shape s, StrokeContext context) {
        super(new Bounds(), context);
        shape = s;
    }

    @Override
    protected void paintNode(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.draw(shape);
    }

    public Shape getShape() {
        return shape;
    }
}
