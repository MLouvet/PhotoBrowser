package photoComponent.view;

import photoComponent.model.PenStatus;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.List;

public interface IPhotoUI {
    void flip();
    Dimension getDimension();

    List<PropertyChangeListener> getListeners();            //Read-only List
    void addPropertyChangeListener(PropertyChangeListener l);
    void removePropertyChangeListener(PropertyChangeListener l);
    void loadImage(Path p);

    void setPenStatus(PenStatus p);

    boolean addCharacter(Character c);
}
