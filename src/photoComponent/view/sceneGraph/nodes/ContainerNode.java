package photoComponent.view.sceneGraph.nodes;

import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.DefaultContext;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ContainerNode extends AbstractNode implements ChangeListener {
    public ContainerNode(Bounds b) {
        super(b, new DefaultContext());
    }

    @Override
    protected void paintNode(Graphics g) {
        //Nothing to do here
    }

    @Override
    public void addNode(AbstractNode n) {
        super.addNode(n);

        n.addChangeListener(this);
        Bounds b = this.getBounds();
        if (!b.contains(n.getBounds()))
            setBounds(b.enlargeBounds(n.getBounds()));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        checkBounds();
    }

    @Override
    public void removeNode(AbstractNode n) {
        super.removeNode(n);
        checkBounds();
    }

    private void checkBounds() {
        Bounds b = new Bounds();
        for (AbstractNode abstractNode : getChildren()) {
            if (!b.contains(abstractNode.getBounds()))
                b = b.enlargeBounds(abstractNode.getBounds());
        }

        if (!b.equals(getBounds()))
            setBounds(b);
    }
}
