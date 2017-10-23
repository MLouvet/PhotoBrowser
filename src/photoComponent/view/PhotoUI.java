package photoComponent.view;

import photoComponent.model.PenStatus;
import photoComponent.view.sceneGraph.inputContexts.PenContext;
import photoComponent.view.sceneGraph.inputContexts.StrokeContext;
import photoComponent.view.sceneGraph.nodes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
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
    private List<PropertyChangeListener> listeners;
    private Dimension dimension;
    private boolean isFlipped;
    private boolean canDraw;
    private boolean canType;
    private TextNode currentTextNode;
    private PathNode currentPathNode;
    private ShapeNode currentShapeNode;
    private String errorMsg;
    private PenStatus penStatus;
    private Point origin = null;

    public PhotoUI() {
        rootNode = new RootNode();
        imageNode = null;
        penStatus = new PenStatus();
        listeners = new ArrayList<>();
        dimension = new Dimension();
        isFlipped = canDraw = canType = false;
        currentTextNode = null;
        currentPathNode = null;
        currentShapeNode = null;
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
            for (PropertyChangeListener listener : listeners) {
                listener.propertyChange(new PropertyChangeEvent(this, "currentTextNode",
                        null, currentTextNode));
            }
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
            currentShapeNode = null;
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
        boolean wasNull = false;
        if (e.getX() < 0 || dimension.width < e.getX()  //Out of bounds
                || e.getY() < 0 || dimension.height < e.getY())
            return;
        if (canDraw) {
            switch (penStatus.getShapeKind()) {
                case CURVE:
                    if (currentPathNode == null) {
                        currentPathNode = new PathNode(new StrokeContext(penStatus));
                        rootNode.addNode(currentPathNode);
                        for (PropertyChangeListener listener : listeners) {
                            listener.propertyChange(new PropertyChangeEvent(this, "currentPathNode",
                                    null, currentPathNode));
                        }
                    }
                    currentPathNode.addPoint(e.getPoint());
                    for (PropertyChangeListener listener : listeners) {
                        listener.propertyChange(new PropertyChangeEvent(this, "currentPathNode",
                                currentPathNode, currentPathNode));
                    }
                    break;
                case ELLIPSE:
                    if (currentShapeNode == null) {
                        currentShapeNode = new ShapeNode(new Ellipse2D.Float(e.getX(), e.getY(), 0, 0), new StrokeContext(penStatus));
                        wasNull = true;
                    }
                case RECTANGLE:
                    if (currentShapeNode == null) {
                        currentShapeNode = new ShapeNode(new Rectangle2D.Float(e.getX(), e.getY(), 0, 0), new StrokeContext(penStatus));
                        wasNull = true;
                    }
                case ROUND_RECTANGLE:
                    if (currentShapeNode == null) {
                        currentShapeNode = new ShapeNode(new RoundRectangle2D.Float(e.getX(), e.getY(), 0, 0, 20, 20), new StrokeContext(penStatus));
                        wasNull = true;
                    }
                    if (wasNull) {  //Creation of the shape
                        origin = e.getPoint();
                        rootNode.addNode(currentShapeNode);
                        for (PropertyChangeListener listener : listeners) {
                            listener.propertyChange(new PropertyChangeEvent(this, "currentShapeNode",
                                    null, currentShapeNode));
                        }
                    } else { //Updating shape
                        int x = Math.min(origin.x, e.getX());
                        int w = Math.abs(origin.x - e.getX());
                        int y = Math.min(origin.y, e.getY());
                        int h = Math.abs(origin.y - e.getY());

                        RectangularShape r = (RectangularShape) currentShapeNode.getShape();
                        r.setFrame(x, y, w, h);
                        for (PropertyChangeListener listener : listeners) {
                            listener.propertyChange(new PropertyChangeEvent(this, "currentShapeNode",
                                    currentShapeNode, currentShapeNode));
                        }
                    }
                    break;
                case STROKE:
                    if (currentShapeNode == null) {
                        currentShapeNode = new ShapeNode(new Line2D.Float(e.getX(), e.getY(), e.getX() + 10, e.getY() + 10), new StrokeContext(penStatus));
                        rootNode.addNode(currentShapeNode);
                        for (PropertyChangeListener listener : listeners) {
                            listener.propertyChange(new PropertyChangeEvent(this, "currentShapeNode",
                                    null, currentShapeNode));
                        }
                    } else { //Moving target point
                        Line2D.Float l = (Line2D.Float) currentShapeNode.getShape();
                        l.setLine(l.getP1(), e.getPoint());
                        for (PropertyChangeListener listener : listeners) {
                            listener.propertyChange(new PropertyChangeEvent(this, "currentShapeNode",
                                    currentShapeNode, currentShapeNode));
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (canType) {
            canType = false;
            if (currentTextNode.getText().isEmpty()) {
                rootNode.removeNode(currentTextNode);
                for (PropertyChangeListener listener : listeners) {
                    listener.propertyChange(new PropertyChangeEvent(this, "currentTextNode",
                            currentTextNode, null));
                }
            }
            currentTextNode = null;
        }
    }
    //</editor-fold>

}
