package photoComponent.view.sceneGraph.nodes;

import photoComponent.view.sceneGraph.inputContexts.DefaultContext;

import java.awt.*;

public class RootNode extends AbstractNode {

    public RootNode() {
        super(null, new DefaultContext());
    }

    @Override
    public void paintNode(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        //Nothing to do as it displays nothing
    }
}
