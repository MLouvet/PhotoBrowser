package photoComponent.model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;zzz
import java.nio.file.Path;
import java.util.List;

public class PhotoComponentModel extends JComponent implements ChangeListener, IPhotoComponentModel {
    private List<Point> points;
    private List<TypedText> typedTexts;
    private List<Shape> shapes;
    private Image image;
    private boolean isFlipped;

    public PhotoComponentModel(BufferedImage image) {
        this.image = image;
        if (image == null)
            return;
        this.setPreferredSize(new Dimension(image.getHeight(), image.getWidth()));
    }

    protected void paintComponent() {
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    public void loadPhoto(Path path) {

    }

    @Override
    public void flip() {

    }

    @Override
    public boolean isFlipped() {
        return false;
    }

    @Override
    public void draw() {

    }

    @Override
    public void type() {

    }

    @Override
    public void saveAnnotations() {

    }
}
