package photoComponent;

import photoComponent.model.Annotation;
import photoComponent.model.IAnnotation;
import photoComponent.model.PenStatus;
import photoComponent.view.AbstractPhotoUI;
import photoComponent.view.PhotoUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.nio.file.Paths;

/**
 * Created by Mathieu on 20/09/2017.
 */
public class PhotoComponent extends JComponent {
    /*
     * Documentation :
     * https://community.oracle.com/docs/DOC-983603
     * https://openclassrooms.com/courses/apprenez-a-programmer-en-java/mieux-structurer-son-code-le-pattern-mvc
     * https://docs.oracle.com/javase/7/docs/api/java/awt/Point.html
     * https://stackoverflow.com/questions/9711938/java-draw-line-as-the-mouse-is-moved
     * http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Mousedraganddraw.htm
     * https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics2D.html
     * http://www.java2s.com/Code/JavaAPI/java.awt/FontderiveFontintstylefloatsize.htm
     */

    private IAnnotation model;
    private AbstractPhotoUI ui;

    public PhotoComponent() {
        model = new Annotation();
        ui = new PhotoUI(model);

        //        PhotoComponent photoComponent = this;
        model.addChangeListener(e -> {
            revalidate();
            repaint();
        });

        ui.addPropertyChangeListener(evt -> {
            if ("dimension".equals(evt.getPropertyName()))
                setPreferredSize(ui.getDimension());
            repaint();
            revalidate();
        });

        ui.addPropertyChangeListener(e -> {
            for (PropertyChangeListener l : getPropertyChangeListeners()) {
                l.propertyChange(e);
            }
        });

        this.addMouseListener(ui);
        this.addMouseMotionListener(ui);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.paint(g, this);
    }

    public void addCharacter(Character c) {
        if (ui.addCharacter(c)) {
            repaint();
            revalidate();
        }
    }

    public void loadImage(String path) {
        ui.loadImage(Paths.get(path));
        model.clean();
    }

    public void setPenStatus(PenStatus penStatus) {
        ui.setPenStatus(penStatus);
    }
}
