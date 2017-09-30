package photoComponent.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PhotoComponentUI extends IPhotoComponentUI implements MouseListener {


    @Override
    public void flip() {

    }

    //Events here


    //<editor-fold desc="Events">
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2 /*&& !e.isConsumed()*/){

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //</editor-fold>
}
