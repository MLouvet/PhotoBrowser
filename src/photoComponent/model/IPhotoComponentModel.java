package photoComponent.model;

import java.nio.file.Path;

public interface IPhotoComponentModel {
    //Displaying image
    void loadPhoto(Path path);
    void flip();
    boolean isFlipped();

    //Annotating
    void draw();
    void type();
    void saveAnnotations();

    //Scaling

}
