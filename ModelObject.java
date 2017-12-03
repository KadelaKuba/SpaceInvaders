package thegame;

import javafx.geometry.Point2D;

public abstract class ModelObject {

    private Point2D position;
    double direction;
    private final Point2D imageOffset;

    public ModelObject(Point2D position, Point2D imageOffset) {
        this.position = new Point2D(position.getX(), position.getY());
        this.imageOffset = imageOffset;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public Point2D getPosition() {
        return this.position;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirectionToPoint(Point2D direction) {
        Point2D imageRotation = position.add(imageOffset);
        double angle = position.angle(direction, imageRotation);
        if (direction.getY() < position.getY()) {
            angle = -angle;
        }
        this.direction = angle;
    }

    public boolean isOutOfSpace() {
        if (this.position.getX() < 0 || this.position.getX() > View.WIDTH) {
            return true;
        }
        if (this.position.getY() < 0 || this.position.getY() > View.HEIGHT) {
            return true;
        }
        return false;
    }

    protected void move(double offsetX, double offsetY) {
        position = new Point2D(getX() + offsetX, getY() + offsetY);
    }

    public abstract void process();
}
