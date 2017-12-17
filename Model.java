package thegame;

import java.util.ArrayList;

import javafx.geometry.Point2D;

import javax.swing.text.Position;

public class Model {

    private ArrayList<ModelObject> objects = new ArrayList<>();
    private ArrayList<ModelObject> alienList = new ArrayList<>();
    private ModelObject player;
    private boolean newLaserCanShoot;
    boolean right = true;

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
        objects.add(player);

        for (int height = 50; height <= 300; height += 100) {
            for (int width = 200; width <= View.WIDTH - 200; width += 100) {
                ModelObject alien = new Alien(new Point2D(width, height), new Point2D(100, 0));
                alienList.add(alien);
            }
        }
    }

    public synchronized void movePlayer(int direction) {
        if (player != null) {
            if (player.getX() + direction > 10 && player.getX() + direction < View.WIDTH - 10) {
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

    public void moveAliens() {
        if (this.right) {
            for (ModelObject alien : alienList) {
                alien.move(30, 0);
            }
            if (alienList.get(alienList.size() - 1).getX() >= 750) {
                this.right = false;

            }
        } else {
            for (ModelObject alien : alienList) {
                alien.move(-30, 0);
            }

            if (alienList.get(0).getX() <= 50) {
                this.right = true;

            }
        }
    }

    public boolean solveCollison(ModelObject laser, ModelObject alien, View view) {
        double xbombimg = view.getLaser().getWidth() / 2;
        double ybombimg = view.getLaser().getHeight() / 2;
        double xmonsterimg = view.getAlien().getWidth() / 2;
        double ymonsterimg = view.getAlien().getHeight() / 2;


        double Xa1, Ya1, Xa2, Ya2, Xb1, Yb1, Xb2, Yb2;

        Xa1 = laser.getX() - xbombimg;
        Ya1 = laser.getY() - ybombimg;
        Xa2 = laser.getX() + xbombimg;
        Ya2 = laser.getY() + ybombimg;

        Xb1 = alien.getX() - xmonsterimg;
        Yb1 = alien.getY() - ymonsterimg;
        Xb2 = alien.getX() + xmonsterimg;
        Yb2 = alien.getY() + ymonsterimg;

        if (((Xb1 - Xa2) * (Xb2 - Xa1) <= 0) && ((Yb1 - Ya2) * (Yb2 - Ya1) <= 0)) {
            return true;
        }
        return false;
    }
}
