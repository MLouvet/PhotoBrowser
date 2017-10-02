package photoComponent;

import javax.swing.event.ChangeListener;

public interface IPenSelector {
    void addChangeListener(ChangeListener l);
    boolean removeChangeListener(ChangeListener l);
}
