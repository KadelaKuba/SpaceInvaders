package thegame;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Model {

    private ArrayList<ModelObject> objects = new ArrayList<>();
    private ArrayList<ModelObject> alienList = new ArrayList<>();
    private ModelObject player;
    private boolean newLaserCanShoot;

    public ArrayList<ModelObject> getObjects() {
        return this.objects;
    }

    public ArrayList<ModelObject> getAlienObjects() {
        return this.alienList;
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
        player = new Player(new Point2D(View.WIDTH / 2, 500), new Point2D(100, 0));
//        alien = new Alien(new Point2D(View.WIDTH / 2, 200), new Point2D(100, 0));
        objects.add(player);

        for (int height = 50; height <= 300; height += 100) {
            for (int width = 50; width < View.WIDTH - 30; width += 70) {
                ModelObject alien = new Alien(new Point2D(width, height), new Point2D(100, 0));
                alienList.add(alien);
            }
        }
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
            if (this.getNewLaserCanShoot()) {
                Point2D target = new Point2D(player.getX(), 0);
                Point2D player = this.player.getPosition();
                Point2D speed = target.subtract(player);

                speed = speed.normalize();
                speed = speed.multiply(10);
                Point2D laserStartPosition = player.add(speed.multiply(7));

                Laser laser = new Laser(laserStartPosition, new Point2D(100, 0), speed);
                laser.setDirectionToPoint(laserStartPosition.add(speed.multiply(10)));
                objects.add(laser);
                setNewLaserCanShoot(false);
            }
        }
    }

}
