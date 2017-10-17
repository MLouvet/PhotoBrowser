package photoComponent.view.sceneGraph.nodes;

import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.StrokeContext;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class PathNode extends AbstractNode {
    private GeneralPath path;
    private StrokeContext strokeContext;

    public PathNode(StrokeContext strokeContext) {
        super(new Bounds(), strokeContext);
        path = new GeneralPath();
        this.strokeContext = strokeContext;
    }

    public void addPoint(Point p) {
        if (path.getCurrentPoint() == null) {
            path.moveTo(p.x, p.y);
            this.setBounds(new Bounds());
        } else {
            path.lineTo(p.x, p.y);
            this.setBounds(new Bounds(getBounds().x, getBounds().y, path.getBounds().width, path.getBounds().height));
        }
    }

    @Override
    protected void paintNode(Graphics g) {
        g = g.create();
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.draw(path);
    }
}
