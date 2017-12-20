package thegame;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Alien extends ModelObject {
    public Alien(Point2D position, Point2D imageOffset) {
        super(position, imageOffset);
    }

    @Override
    public void process() {

    }

    @Override
    public Image getImage(View view) {
        return view.getAlien();
    }
}
