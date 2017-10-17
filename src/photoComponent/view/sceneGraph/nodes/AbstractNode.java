package photoComponent.view.sceneGraph.nodes;


import com.sun.istack.internal.NotNull;
import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.IInputContext;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNode implements INode {
    @NotNull
    private IInputContext inputContext;
    private AbstractNode parent;
    @NotNull
    private List<AbstractNode> children;
    private Bounds bounds;
    @NotNull
    private AffineTransform affineTransform;
    private boolean visible;
    private List<ChangeListener> positionListeners;

    protected AbstractNode(Bounds b, IInputContext context) {
        visible = true;
        children = new ArrayList<>();
        positionListeners = new ArrayList<>();
        bounds = b;
        inputContext = context;
        affineTransform = new AffineTransform();
        if (b == null)
            return;
        affineTransform.setToTranslation(b.x, b.y);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        for (AbstractNode child : children) {
            child.setVisible(visible);
        }
    }

    public List<AbstractNode> getChildren() {
        return children;
    }

    protected abstract void paintNode(Graphics g);

    @Override
    public void paint(Graphics g) {
        if (!visible)
            return;

        //Applying transformations and drawing the node itself
        Graphics2D graphics2D = (Graphics2D) g;
        AffineTransform oldTransform = graphics2D.getTransform();
        AffineTransform inverted = null;
        try {
            inverted = affineTransform.createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        oldTransform.concatenate(affineTransform);
        inputContext.applyContext(g);
        paintNode(g);


        //Drawing children
        for (AbstractNode child : children) {
            child.paint(g);
        }
        //Reversing transformations
        inputContext.restoreContext(g);
        if (inverted != null)
            oldTransform.concatenate(inverted);
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(@NotNull Bounds b) {
        bounds = b;
        affineTransform.setToTranslation(b.x, b.y);
        for (ChangeListener l : positionListeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        positionListeners.add(changeListener);
    }

    public boolean removeChangeListener(ChangeListener changeListener) {
        return positionListeners.remove(changeListener);
    }

    public boolean contains(AbstractNode n) {
        if (children.contains(n))
            return true;
        for (AbstractNode child : children) {
            if (child.contains(n))
                return true;
        }
        return false;
    }

    public void addNode(AbstractNode n) {
        children.add(n);
        n.setParent(this);
    }

    public void setParent(AbstractNode parent) {
        if (this.parent != null)
            parent.removeNode(this);
        this.parent = parent;
    }

    public void removeNode(AbstractNode n) {
        if (!children.remove(n))
            for (AbstractNode child : children) {
                child.removeNode(n);
            }

    }
}
