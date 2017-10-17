package photoComponent.view.sceneGraph.nodes;

import com.sun.istack.internal.NotNull;
import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.ColorContext;

import java.awt.*;

public class ShapeNode extends AbstractNode {
    private Shape shape;

    protected ShapeNode(@NotNull Shape s, ColorContext context) {
        super(new Bounds(s.getBounds()), context);
        shape = s;
    }

    @Override
    protected void paintNode(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.draw(shape);
    }
}
