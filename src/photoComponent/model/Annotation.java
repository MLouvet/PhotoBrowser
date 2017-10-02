package photoComponent.model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Annotation extends JComponent implements IAnnotation {
    private List<FancyPoint> points = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<TypedText> typedTexts = new ArrayList<>();
    private List<ChangeListener> listeners = new ArrayList<>();

    @Override
    public List<ChangeListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    @Override
    public synchronized List<FancyPoint> getPoints() {
        return Collections.unmodifiableList(points);
    }

    @Override
    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    @Override
    public List<TypedText> getTypedTexts() {
        return Collections.unmodifiableList(typedTexts);
    }

    @Override
    public void addTypedText(TypedText t) {
        typedTexts.add(t);
        notifyPropertyChanged();
    }

    @Override
    public void addPoint(FancyPoint p) {
        points.add(p);
        notifyPropertyChanged();
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void addShape(Shape s) {
        shapes.add(s);
        notifyPropertyChanged();
    }


    @Override
    public void removeTypedText(TypedText t) {
        int index = typedTexts.indexOf(t);
        if (index != -1) {
            typedTexts.remove(index);
            notifyPropertyChanged();
        }
    }

    @Override
    public void removePoint(FancyPoint p) {
        int index = points.indexOf(p);
        if (index != -1) {
            points.remove(index);
            notifyPropertyChanged();
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        int index = listeners.indexOf(l);
        if (index != -1)
            listeners.remove(index);
    }

    @Override
    public void removeShape(Shape s) {
        int index = shapes.indexOf(s);
        if (index != -1) {
            shapes.remove(index);
            notifyPropertyChanged();
        }
    }

    @Override
    public void addTypedTextCharacter(TypedText t, Character c) {
        if (typedTexts.indexOf(t) == -1)
            return;
        t.addCharacter(c);
        notifyPropertyChanged();
    }

    @Override
    public void clean() {
        points.clear();
        shapes.clear();
        typedTexts.clear();

    }

    private void notifyPropertyChanged() {
        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }
}
