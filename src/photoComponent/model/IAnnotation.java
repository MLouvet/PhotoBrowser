package photoComponent.model;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public interface IAnnotation {

    /**
     * @return Read-only Lists
     */
    List<ChangeListener> getListeners();            //Read-only List
    List<FancyPoint> getPoints();                   //Read-only List
    List<Shape> getShapes();                        //Read-only List
    List<TypedText> getTypedTexts();                //Read-only List

    void addTypedText(TypedText t);
    void addPoint(FancyPoint p);
    void addChangeListener(ChangeListener l);
    void addShape(Shape s);
    void removeTypedText(TypedText t);
    void removePoint(FancyPoint p);
    void removeChangeListener(ChangeListener l);
    void removeShape(Shape s);

    void addTypedTextCharacter(TypedText t, Character c);
    void clean();
}
