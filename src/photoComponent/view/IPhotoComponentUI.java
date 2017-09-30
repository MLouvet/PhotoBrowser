package photoComponent.view;

import javax.swing.plaf.ComponentUI;

public abstract class IPhotoComponentUI extends ComponentUI {
    public static final String UI_CLASS_ID = "PhotoUI";

    public abstract void flip();
}
