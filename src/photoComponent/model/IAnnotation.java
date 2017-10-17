package photoComponent.model;

import photoComponent.view.sceneGraph.nodes.PathNode;
import photoComponent.view.sceneGraph.nodes.ShapeNode;
import photoComponent.view.sceneGraph.nodes.TextNode;

import javax.swing.event.ChangeListener;
import java.awt.*;

public interface IAnnotation {

    /**
     * @return Read-only Lists
     */

    void addTextNode(TextNode t);
    void addPathNode(PathNode p);
    void addChangeListener(ChangeListener l);
    void addShapeNode(ShapeNode s);
    void removeTextNode(TextNode t);

    void addTextNodeCharacter(TextNode t, Character c);
    void addPathNodePoint(PathNode node, Point p);
    void clean();
}
