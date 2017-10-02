package photoComponent.view;

import photoComponent.model.IAnnotation;
import photoComponent.model.TypedText;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.CHAR_UNDEFINED;

enum RotationStatus {
    BACK, BACK_TO_FRONT, FRONT, FRONT_TO_BACK
}

public class PhotoUI extends AbstractPhotoUI {
    private IAnnotation model;
    private List<PropertyChangeListener> listeners;
    private Image image;
    private Dimension dimension, loadedDimension;
    private RotationStatus rotationStatus;
    private float viewablePercent;
    private boolean isFlipped;
    private boolean canDraw;
    private boolean canType;
    private TypedText currentText;

    public PhotoUI(IAnnotation model) {
        this.model = model;
        image = null;
        listeners = new ArrayList<>();
        dimension = new Dimension();
        rotationStatus = RotationStatus.FRONT;
        viewablePercent = 1; //0 = sideview
        canDraw = canType = false;
    }

    //Events here
    //<editor-fold desc="Events">
    @Override
    public void mouseClicked(MouseEvent e) {
        if (image == null)
            return;
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            if (rotationStatus == RotationStatus.FRONT || rotationStatus == RotationStatus.BACK_TO_FRONT)
                rotationStatus = RotationStatus.FRONT_TO_BACK;
            else if (rotationStatus == RotationStatus.BACK || rotationStatus == RotationStatus.FRONT_TO_BACK)
                rotationStatus = RotationStatus.BACK_TO_FRONT;

            flip();
            e.consume();
        } else if (e.getClickCount() == 1 && !e.isConsumed() && isFlipped) {
            System.out.println("Click");
            canType = true;
            currentText = new TypedText("", e.getPoint());
            model.addTypedText(currentText);
        }
    }

    @Override
    public void flip() {
        if (image == null)
            return;
        int fps = 60;
        float step = 0.01f;

        //Reduce current frame

        Dimension d = loadedDimension;
        do {
            //Calculate rotation
            if (rotationStatus == RotationStatus.FRONT_TO_BACK) {
                if (!isFlipped) {
                    viewablePercent -= step;
                    if (viewablePercent <= 0) {
                        isFlipped = true;
                    }
                } else {
                    viewablePercent += step;
                    if (viewablePercent >= 1)
                        rotationStatus = RotationStatus.BACK;
                }
            } else if (rotationStatus == RotationStatus.BACK_TO_FRONT) {
                if (isFlipped) {
                    viewablePercent -= step;
                    if (viewablePercent <= 0) {
                        isFlipped = false;
                    }
                } else {
                    viewablePercent += step;
                    if (viewablePercent >= 1)
                        rotationStatus = RotationStatus.FRONT;
                }
            }


            //Letting the image draw itself by putting the thread to sleep
            System.out.println(d.width);
            System.out.println(d.width * viewablePercent);
            setDimension((int) (d.width * viewablePercent), d.height);
            //            try {
            //                Thread.sleep(1000 / fps);
            //            } catch (InterruptedException e) {
            //                e.printStackTrace();
            //            }
            System.out.println(rotationStatus);
        } while (!d.equals(dimension));

    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public void setModel(IAnnotation model) {
        this.model = model;
    }

    @Override
    public List<PropertyChangeListener> getListeners() {
        return listeners;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public void loadImage(Path p) {
        if (!Files.exists(p))
            return;

        try {
            BufferedImage imageTemp = ImageIO.read(p.toFile());
            if (image == null || !image.equals(imageTemp)) {
                image = imageTemp;
                setDimension(image.getWidth(null), image.getHeight(null));
                loadedDimension = new Dimension(dimension);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public boolean addCharacter(Character c) {
        if (!canType || c == CHAR_UNDEFINED)
            return false;

        //Checking if a character can be added
        StringBuilder sb = new StringBuilder(currentText.text + c);
        int breakIndex = sb.lastIndexOf(System.lineSeparator());

        String beforeLast;
        if (breakIndex != -1) {
            beforeLast = sb.substring(0, breakIndex);
            sb = new StringBuilder(sb.substring(breakIndex + 1));
        } else {
            beforeLast = "";
        }

        //TODO : COunt lines

        Graphics g = image.getGraphics(); //Dirty way of getting graphics, but efficient
        Rectangle2D rect = g.getFontMetrics(currentText.font).getStringBounds(sb.toString(), g);

        if (currentText.position.x + (int) rect.getWidth() > dimension.width) {
            System.out.println("TOO LARGE");
            System.out.println("Pos : " + currentText.position.x + " + " + rect.getWidth());
            if (sb.length() == 1) //already at limit
                return false;
            else {  //a linebreak will be added if possible
                int numberOfLinesWithNewBreak = currentText.text.split(System.lineSeparator()).length + 1;
                if (dimension.height < currentText.position.y + rect.getHeight() * numberOfLinesWithNewBreak)
                    return false;
                int lastSpaceIndex = sb.lastIndexOf(" ");
                if (lastSpaceIndex != -1) {
                    sb.replace(lastSpaceIndex, lastSpaceIndex + 1, System.lineSeparator());
                    currentText.text = beforeLast + sb.toString();
                } else
                    currentText.text = currentText.text + System.lineSeparator() + c;
            }
        } else
            currentText.text += c;

        System.out.println("Character ADDED!");

        System.out.println(currentText.text);
        return true;
    }

    private void setDimension(int width, int height) {
        Dimension oldDim = new Dimension(dimension);
        dimension.setSize(width, height);
        System.out.println(dimension);
        if (!dimension.equals(oldDim))
            for (PropertyChangeListener listener : listeners) {
                listener.propertyChange(new PropertyChangeEvent(this, "dimension", oldDim, dimension));
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            canDraw = isFlipped;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            canDraw = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        if (image != null) {
            if (!isFlipped) {//Display image
                g.drawImage(image, 0, 0, dimension.width, dimension.height, c);
            } else {
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, dimension.width, dimension.height);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, dimension.width, dimension.height);
                //TODO : draw annotations here
                g.setColor(Color.BLACK);
                for (Point p : model.getPoints()) {
                    g.fillOval(p.x, p.y, 4, 4);
                }
                for (TypedText t : model.getTypedTexts()) {
                    g.setColor(t.color);
                    g.setFont(t.font);
                    int y = t.position.y;
                    for (String s : t.text.split(System.lineSeparator())) {
                        g.drawString(s, t.position.x, y += g.getFontMetrics().getHeight());
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (canDraw)
            model.addPoint(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (canType) {
            System.out.println("END OF TYPING");
            canType = false;
            if (currentText.text.isEmpty()) {
                model.removeTypedText(currentText);
            }
            currentText = null;
        }
    }
    //</editor-fold>

}
