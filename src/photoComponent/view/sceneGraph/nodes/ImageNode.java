package photoComponent.view.sceneGraph.nodes;

import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.DefaultContext;

import java.awt.*;

public class ImageNode extends AbstractNode {
    int x, y;
    private Image image;

    public ImageNode(Image i, int x, int y) {
        super(new Bounds(x, y, i.getWidth(null), i.getHeight(null)), new DefaultContext());
        image = i;
    }

    @Override
    protected void paintNode(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
