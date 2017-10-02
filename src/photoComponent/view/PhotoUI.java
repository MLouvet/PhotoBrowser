package photoComponent.view;

import photoComponent.model.IAnnotation;
import photoComponent.model.TypedText;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
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
        if (!canType || c == CHAR_UNDEFINED || currentText == null)
            return false;

        if (shouldAddCharacter(currentText.text, c)) {
            model.addTypedTextCharacter(currentText, c);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checking if a character can be added and not go out of the gui
     *
     * @param text: text which will be displayed for currentText
     * @param c: character to be added to X
     * @return true if character can be added and displayed
     */
    private boolean shouldAddCharacter(String text, Character c) {
        int maxWidth = dimension.width - currentText.position.x;
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder(text + c);
        FontMetrics metrics = image.getGraphics().getFontMetrics(currentText.font);

        for (int i = 0; i < sb.length(); i++) {
            int size = metrics.stringWidth(sb.substring(0, i + 1));
            if (size > maxWidth) {
                lines.add(sb.substring(0, i));
                sb.delete(0, i);
                i = 0;
            }
        }
        if (sb.length() != 0)
            lines.add(sb.toString());
        if (lines.isEmpty()) //Case where a character can't be added at all: border of the frame
            return false;

        int height = metrics.getHeight() * (lines.size() - 1); //Drawn from bottom to top
        return currentText.position.y + height <= dimension.height;
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
                    drawStringWithBreaks(t.text, t.font, g, t.position.x, t.position.y);
                }
            }
        }
    }

    private void drawStringWithBreaks(String s, Font f, Graphics g, int x, int y) {
        int maxWidth = dimension.width - x;
        StringBuilder sb = new StringBuilder(s);
        FontMetrics metrics = image.getGraphics().getFontMetrics(f);

        for (int i = 0; i < sb.length(); i++) {
            int size = metrics.stringWidth(sb.substring(0, i + 1));
            if (size > maxWidth) {
                g.drawString(sb.substring(0, i), x, y);
                y += metrics.getHeight();
                sb.delete(0, i);
                i = 0;
            }
        }
        if (sb.length() != 0)
            g.drawString(sb.toString(), x, y);
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
