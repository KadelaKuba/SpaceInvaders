package thegame;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class PlayerLaser extends ModelObject {

    Point2D speedVector;

    public PlayerLaser(Point2D position, Point2D imageOffset, Point2D speedVector) {
        super(position, imageOffset);
        this.speedVector = speedVector;
    }

    @Override
    public void process() {
        move(speedVector.getX(), speedVector.getY());
    }

    @Override
    public Image getImage(View view) {
        return view.getLaser();
    }

}
