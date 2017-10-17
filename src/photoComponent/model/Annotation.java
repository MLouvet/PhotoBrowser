package photoComponent.model;

import photoComponent.view.sceneGraph.nodes.PathNode;
import photoComponent.view.sceneGraph.nodes.ShapeNode;
import photoComponent.view.sceneGraph.nodes.TextNode;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Annotation extends JComponent implements IAnnotation {
    private List<PathNode> paths = new ArrayList<>();
    private List<ShapeNode> shapes = new ArrayList<>();
    private List<TextNode> texts = new ArrayList<>();
    private List<ChangeListener> listeners = new ArrayList<>();

    @Override
    public void addTextNode(TextNode t) {
        texts.add(t);
        notifyPropertyChanged();
    }

    @Override
    public void addPathNode(PathNode p) {
        paths.add(p);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void addShapeNode(ShapeNode s) {
        shapes.add(s);
        notifyPropertyChanged();
    }


    @Override
    public void removeTextNode(TextNode t) {
        int index = texts.indexOf(t);
        if (index != -1) {
            texts.remove(index);
            notifyPropertyChanged();
        }
    }

    @Override
    public void addTextNodeCharacter(TextNode t, Character c) {
        if (texts.indexOf(t) == -1)
            return;
        t.addCharacter(c);
        notifyPropertyChanged();
    }

    @Override
    public void addPathNodePoint(PathNode node, Point p) {
        if (paths.indexOf(node) == -1)
            return;
        node.addPoint(p);
        notifyPropertyChanged();
    }


    @Override
    public void clean() {
        paths.clear();
        shapes.clear();
        texts.clear();

    }

    private void notifyPropertyChanged() {
        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }
}
