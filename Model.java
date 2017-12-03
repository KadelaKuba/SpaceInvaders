package thegame;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Model {

    private ArrayList<ModelObject> objects = new ArrayList<>();
    private ModelObject player;
    private boolean newLaserCanShoot;

    public ArrayList<ModelObject> getObjects() {
        return this.objects;
    }

    public Model() {
        this.newLaserCanShoot = true;
    }

    public void setNewLaserCanShoot(boolean newLaserCanShoot) {
        this.newLaserCanShoot = newLaserCanShoot;
    }

    public boolean getNewLaserCanShoot() {
        return this.newLaserCanShoot;
    }

    public synchronized void initGame() {
        objects.clear();
        Point2D center = new Point2D(View.WIDTH / 2, 500);
        player = new Player(center, new Point2D(100, 0));
        objects.add(player);
    }

    public synchronized void movePlayer(int direction) {
        if (player != null) {
            if (player.getX() + direction > 0 && player.getX() + direction < View.WIDTH) {
                player.move(direction, 0);
            }
        }
    }

    public synchronized void shot() {
        if (player != null) {
            System.out.println(this.getNewLaserCanShoot());
            if (this.getNewLaserCanShoot()) {
                Point2D mousePoint = new Point2D(player.getX(), 0);
                Point2D tankPosition = player.getPosition();
                Point2D speed = mousePoint.subtract(tankPosition);
                speed = speed.normalize();
                speed = speed.multiply(5);
                Point2D bombStartPosition = tankPosition.add(speed.multiply(7));
                Laser bomb = new Laser(bombStartPosition, new Point2D(100, 0), speed);
                bomb.setDirectionToPoint(bombStartPosition.add(speed.multiply(10)));
                objects.add(bomb);
                setNewLaserCanShoot(false);
            }
        }
    }

}
