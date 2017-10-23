package photoComponent;

import photoComponent.model.PenStatus;
import photoComponent.view.sceneGraph.inputContexts.StrokeContext;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Class created to handle input variation (font, color, size, etc...)
 */
public class PenSelector extends JPanel implements IPenSelector, ChangeListener {
    private final JSpinner size;
    private final JComboBox<String> fontList;
    private PenStatus penStatus;
    private List<ChangeListener> listeners;

    private ButtonGroup sizeSelector, shapeSelector;
    private JRadioButton tinySize, smallSize, mediumSize, largeSize;
    private JToggleButton jToggleButtonCurve, jToggleButtonEllipse, jToggleButtonRectangle,
            jToggleButtonRoundRectangle, jToggleButtonStroke;


    public PenSelector() {
        super(new FlowLayout(FlowLayout.LEFT));
        penStatus = new PenStatus();
        listeners = new ArrayList<>();


        //region Part 1 of control
        //Part 1 of control
        JPanel textPanel = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder("New text");
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitlePosition(TitledBorder.BOTTOM);
        textPanel.setBorder(border);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        //Handling various fonts
        fontList = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        textPanel.add(fontList);

        //Spinner to select fontSize
        size = new JSpinner(new SpinnerNumberModel(24, 1, 100, 1));
        JLabel l = new JLabel("Font size:");
        l.setLabelFor(size);

        JPanel fontContainer = new JPanel();
        fontContainer.add(l);
        fontContainer.add(size);

        textPanel.add(fontContainer);

        JPanel styleHolder = new JPanel();
        ButtonGroup styleSelector = new ButtonGroup();
        JRadioButton plain = new JRadioButton("Plain");
        JRadioButton italic = new JRadioButton("Italic");
        JRadioButton bold = new JRadioButton("Bold");
        styleSelector.add(plain);
        styleSelector.add(italic);
        styleSelector.add(bold);
        styleHolder.add(plain);
        styleHolder.add(italic);
        styleHolder.add(bold);
        plain.setSelected(true);
        textPanel.add(styleHolder);


        JButton jButtonColorPopup = new JButton("Change text/pen color");
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(textPanel);
        p.add(jButtonColorPopup);


        add(p);
        //endregion

        //region Part 2 of control
        //Part 2 of the control
        sizeSelector = new ButtonGroup();
        tinySize = new JRadioButton("Tiny");
        smallSize = new JRadioButton("Small");
        mediumSize = new JRadioButton("Medium");
        largeSize = new JRadioButton("Large");

        sizeSelector.add(tinySize);
        sizeSelector.add(smallSize);
        sizeSelector.add(mediumSize);
        sizeSelector.add(largeSize);

        JPanel sizeContainer = new JPanel();
        border = BorderFactory.createTitledBorder("Pen size");
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitlePosition(TitledBorder.BOTTOM);
        sizeContainer.setBorder(border);
        sizeContainer.setLayout(new BoxLayout(sizeContainer, BoxLayout.Y_AXIS));
        sizeContainer.add(tinySize);
        sizeContainer.add(smallSize);
        sizeContainer.add(mediumSize);
        sizeContainer.add(largeSize);
        mediumSize.setSelected(true);
        add(sizeContainer);
        //endregion

        //region Part 3 of control
        shapeSelector = new ButtonGroup();
        
        ImageIcon curve = new ImageIcon(this.getClass().getResource("ressources/img/curves.png"));
        ImageIcon ellipse = new ImageIcon(this.getClass().getResource("ressources/img/ellipse.png"));
        ImageIcon rectangle = new ImageIcon(this.getClass().getResource("ressources/img/rectangle.png"));
        ImageIcon roundRectangle = new ImageIcon(this.getClass().getResource("ressources/img/roundrectangle.png"));
        ImageIcon stroke = new ImageIcon(this.getClass().getResource("ressources/img/stroke.png"));

        curve = new ImageIcon(curve.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        ellipse = new ImageIcon(ellipse.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        rectangle = new ImageIcon(rectangle.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        roundRectangle = new ImageIcon(roundRectangle.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        stroke = new ImageIcon(stroke.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        
        jToggleButtonCurve = new JToggleButton(curve);
        jToggleButtonEllipse = new JToggleButton(ellipse);
        jToggleButtonRectangle = new JToggleButton(rectangle);
        jToggleButtonRoundRectangle = new JToggleButton(roundRectangle);
        jToggleButtonStroke = new JToggleButton(stroke);

        jToggleButtonCurve.setMaximumSize(new Dimension(24,24));
        jToggleButtonEllipse.setMaximumSize(new Dimension(24,24));
        jToggleButtonRectangle.setMaximumSize(new Dimension(24,24));
        jToggleButtonRoundRectangle.setMaximumSize(new Dimension(24,24));
        jToggleButtonStroke.setMaximumSize(new Dimension(24,24));

        shapeSelector.add(jToggleButtonCurve);
        shapeSelector.add(jToggleButtonEllipse);
        shapeSelector.add(jToggleButtonRectangle);
        shapeSelector.add(jToggleButtonRoundRectangle);
        shapeSelector.add(jToggleButtonStroke);

        JPanel shapeContainer = new JPanel();
        border = BorderFactory.createTitledBorder("Shape");
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitlePosition(TitledBorder.BOTTOM);
        shapeContainer.setBorder(border);
        shapeContainer.setLayout(new BoxLayout(shapeContainer, BoxLayout.Y_AXIS));
        shapeContainer.add(jToggleButtonCurve);
        shapeContainer.add(jToggleButtonEllipse);
        shapeContainer.add(jToggleButtonRectangle);
        shapeContainer.add(jToggleButtonRoundRectangle);
        shapeContainer.add(jToggleButtonStroke);
        jToggleButtonCurve.setSelected(true);
        add(shapeContainer);
        //endregion

        //region Event area
        PenSelector parent = this;  //For events


        //region Font list selection
        size.addChangeListener(e -> parent.stateChanged(new ChangeEvent(parent)));
        fontList.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED)
                return;
            Font f = penStatus.getFont();
            penStatus.setFont(new Font((String) fontList.getSelectedItem(), f.getStyle(), f.getSize()));
            stateChanged(new ChangeEvent(parent));

        });
        //endregion

        //region Font size
        size.addChangeListener(e -> {
            Font f = penStatus.getFont();
            penStatus.setFont(new Font(f.getName(), f.getStyle(), (int) size.getValue()));
            stateChanged(new ChangeEvent(parent));
        });
        //endregion

        //region Font style
        plain.addActionListener(e -> {
            Font f = penStatus.getFont();
            penStatus.setFont(new Font(f.getName(), Font.PLAIN, f.getSize()));
            stateChanged(new ChangeEvent(parent));
        });

        italic.addActionListener(e -> {
            Font f = penStatus.getFont();
            penStatus.setFont(new Font(f.getName(), Font.ITALIC, f.getSize()));
            stateChanged(new ChangeEvent(parent));
        });

        bold.addActionListener(e -> {
            Font f = penStatus.getFont();
            penStatus.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
            stateChanged(new ChangeEvent(parent));
        });
        //endregion

        //region color selection
        jButtonColorPopup.addActionListener(e -> {
            Color c = JColorChooser.showDialog(parent, "New text and pen color", penStatus.getColor());
            if (c != null) {
                penStatus.setColor(c);
                stateChanged(new ChangeEvent(this));
            }
        });
        //endregion

        //region Pen size
        tinySize.addActionListener(e -> {
            penStatus.setSize(StrokeContext.Size.TINY);
            stateChanged(new ChangeEvent(this));
        });

        smallSize.addActionListener(e -> {
            penStatus.setSize(StrokeContext.Size.SMALL);
            stateChanged(new ChangeEvent(this));
        });

        mediumSize.addActionListener(e -> {
            penStatus.setSize(StrokeContext.Size.MEDIUM);
            stateChanged(new ChangeEvent(this));
        });

        largeSize.addActionListener(e -> {
            penStatus.setSize(StrokeContext.Size.LARGE);
            stateChanged(new ChangeEvent(this));
        });
        //endregion

        //region Description
        jToggleButtonCurve.addActionListener(e -> {
            penStatus.setShapeKind(PenStatus.ShapeKind.CURVE);
            stateChanged(new ChangeEvent(this));
        });

        jToggleButtonEllipse.addActionListener(e -> {
            penStatus.setShapeKind(PenStatus.ShapeKind.ELLIPSE);
            stateChanged(new ChangeEvent(this));
        });

        jToggleButtonRectangle.addActionListener(e -> {
            penStatus.setShapeKind(PenStatus.ShapeKind.RECTANGLE);
            stateChanged(new ChangeEvent(this));
        });

        jToggleButtonRoundRectangle.addActionListener(e -> {
            penStatus.setShapeKind(PenStatus.ShapeKind.ROUND_RECTANGLE);
            stateChanged(new ChangeEvent(this));
        });

        jToggleButtonStroke.addActionListener(e -> {
            penStatus.setShapeKind(PenStatus.ShapeKind.STROKE);
            stateChanged(new ChangeEvent(this));
        });
        //endregion
        //endregion

        //region Correcting focus to allow typing
        size.setFocusable(false);
        ((JSpinner.DefaultEditor) size.getEditor()).getTextField().setFocusable(false);
        fontList.setFocusable(false);
        plain.setFocusable(false);
        bold.setFocusable(false);
        italic.setFocusable(false);
        tinySize.setFocusable(false);
        smallSize.setFocusable(false);
        mediumSize.setFocusable(false);
        largeSize.setFocusable(false);
        jButtonColorPopup.setFocusable(false);
        jToggleButtonCurve.setFocusable(false);
        jToggleButtonEllipse.setFocusable(false);
        jToggleButtonRectangle.setFocusable(false);
        jToggleButtonRoundRectangle.setFocusable(false);
        jToggleButtonStroke.setFocusable(false);
        //endregion

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    @Override
    public boolean removeChangeListener(ChangeListener l) {
        return listeners.remove(l);
    }

    public PenStatus getPenStatus() {
        return penStatus;
    }
}
