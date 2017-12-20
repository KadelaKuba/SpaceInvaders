package thegame;

import javafx.geometry.Point2D;

public class Boss extends ModelObject {

    Point2D speedVector;

    public Boss(Point2D position, Point2D imageOffset, Point2D speedVector) {
        super(position, imageOffset);
        this.speedVector = speedVector;
    }

    @Override
    public void process() {
        move(speedVector.getX(), speedVector.getY());
    }
}
