package photoComponent.view.sceneGraph.inputContexts;

import java.awt.*;

public interface IInputContext {
    void applyContext(Graphics g);
    void restoreContext(Graphics g);
}
