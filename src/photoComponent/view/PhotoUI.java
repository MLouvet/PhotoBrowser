package photoComponent.view;

import photoComponent.model.IAnnotation;
import photoComponent.model.PenStatus;
import photoComponent.view.sceneGraph.inputContexts.PenContext;
import photoComponent.view.sceneGraph.inputContexts.StrokeContext;
import photoComponent.view.sceneGraph.nodes.*;

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

public class PhotoUI extends AbstractPhotoUI {
    private RootNode rootNode;
    private ImageNode imageNode;
    private IAnnotation model;
    private List<PropertyChangeListener> listeners;
    private Dimension dimension;
    private boolean isFlipped;
    private boolean canDraw;
    private boolean canType;
    private TextNode currentTextNode;
    private PathNode currentPathNode;
    private String errorMsg;
    private PenStatus penStatus;

    public PhotoUI(IAnnotation model) {
        rootNode = new RootNode();
        imageNode = null;
        this.model = model;
        penStatus = new PenStatus();
        listeners = new ArrayList<>();
        dimension = new Dimension();
        isFlipped = canDraw = canType = false;
        currentTextNode = null;
        errorMsg = "";
    }

    //Events here
    //<editor-fold desc="Events">
    @Override
    public void mouseClicked(MouseEvent e) {
        if (imageNode == null)
            return;
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            flip();
            e.consume();
        } else if (e.getClickCount() == 1 && !e.isConsumed() && isFlipped) {
            canType = true;
            currentTextNode = new TextNode(e.getX(), e.getY(), dimension.width, dimension.height, new PenContext(penStatus.getColor(), penStatus.getFont()));
            rootNode.addNode(currentTextNode);
            model.addTextNode(currentTextNode);
        }
    }

    @Override
    public void flip() {
        isFlipped = !isFlipped;
        for (AbstractNode abstractNode : rootNode.getChildren()) {
            abstractNode.setVisible(isFlipped);
        }
        imageNode.setVisible(!isFlipped);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(this, "dimension", dimension, dimension));
        }
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
            BufferedImage image = ImageIO.read(p.toFile());
            setDimension(image.getWidth(null), image.getHeight(null));
            imageNode = new ImageNode(image, 0, 0);
            rootNode.addNode(imageNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPenStatus(PenStatus p) {
        penStatus = p;
    }

    @Override
    public boolean addCharacter(Character c) {
        return currentTextNode != null && currentTextNode.addCharacter(c);
    }

    private void setDimension(int width, int height) {
        Dimension oldDim = new Dimension(dimension);
        dimension.setSize(width, height);
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
        if (e.getButton() == MouseEvent.BUTTON1) {
            canDraw = false;
            currentPathNode = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    void setErrorMessage(String str) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(this, "errorMsg", errorMsg, str));
        }
        errorMsg = str;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        rootNode.paint(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (canDraw) {
            if (currentPathNode == null) {
                currentPathNode = new PathNode(new StrokeContext(penStatus));
                model.addPathNode(currentPathNode);
                rootNode.addNode(currentPathNode);
            }
            model.addPathNodePoint(currentPathNode, e.getPoint());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (canType) {
            canType = false;
            if (currentTextNode.getText().isEmpty()) {
                rootNode.removeNode(currentTextNode);
                model.removeTextNode(currentTextNode);
            }
            currentTextNode = null;
        }
    }
    //</editor-fold>

}
