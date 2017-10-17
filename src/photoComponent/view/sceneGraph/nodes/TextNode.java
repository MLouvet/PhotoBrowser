package photoComponent.view.sceneGraph.nodes;

import photoComponent.view.sceneGraph.Bounds;
import photoComponent.view.sceneGraph.inputContexts.PenContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextNode extends AbstractNode{
    private int maxWidth, maxHeight;
    private String text;
    private PenContext context;

    public TextNode(int x, int y, int maxWidth, int maxHeight, PenContext context) {
        super(new Bounds(x, y, 0, 0), context);
        this.context = context;
        this.maxWidth = maxWidth - x;
        this.maxHeight = maxHeight - y;
        text = "";
    }

    @Override
    protected void paintNode(Graphics g) {
        g = g.create();
        int x = getBounds().x, y = getBounds().y;
        StringBuilder sb = new StringBuilder(text);
        FontMetrics metrics = new Canvas().getFontMetrics(context.getFont());

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

    public boolean addCharacter(Character c) {
        if (!shouldAddCharacter(c))
            return false;
        text += c;
        return true;
    }

    /**
     * Checking if a character can be added and not go out of the gui
     *
     * @param c: character to be added to X
     * @return true if character can be added and displayed
     */
    private boolean shouldAddCharacter(Character c) {
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder(text + c);
        FontMetrics metrics = new Canvas().getFontMetrics(context.getFont());

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
        return height <= maxHeight;
    }

    public String getText() {
        return text;
    }
}
