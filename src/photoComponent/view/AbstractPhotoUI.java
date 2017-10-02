package photoComponent.view;

import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class AbstractPhotoUI extends ComponentUI implements IPhotoUI, MouseListener, MouseMotionListener {
    public static final String UI_CLASS_ID = "PhotoUI";
    abstract void setErrorMessage(String str);
}
