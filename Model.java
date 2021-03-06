package thegame;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Model {

    private ArrayList<ModelObject> objects = new ArrayList<>();
    private ArrayList<ModelObject> alienList = new ArrayList<>();
    private ModelObject player;
    private boolean newLaserCanShoot, moveRight, isOnlyBoss, isNewGame;
    private int score, lives, sectionOfMoving, level, bossHP;

    public ArrayList<ModelObject> getObjects() {
        return this.objects;
    }

    public ArrayList<ModelObject> getAlienObjects() {
        return this.alienList;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public void setNewLaserCanShoot(boolean newLaserCanShoot) {
        this.newLaserCanShoot = newLaserCanShoot;
    }

    public boolean getNewLaserCanShoot() {
        return this.newLaserCanShoot;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives += lives;
    }

    public int getSectionOfMoving() {
        return sectionOfMoving;
    }

    public void setSectionOfMoving(int sectionOfMoving) {
        this.sectionOfMoving += sectionOfMoving;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level += level;
    }

    public int getBossHP() {
        return bossHP;
    }

    public void setBossHP(int bossHP) {
        this.bossHP += bossHP;
    }

    public Model() {
        this.newLaserCanShoot = true;
        this.score = 0;
        this.lives = 3;
        this.sectionOfMoving = 1;
        this.moveRight = true;
        this.isOnlyBoss = false;
        this.level = 1;
        this.bossHP = 1000;
    }

    public synchronized void initGame() {
        objects.clear();
        alienList.clear();
        moveRight = true;
        newLaserCanShoot = true;
        isOnlyBoss = false;
        player = new Player(new Point2D(View.WIDTH / 2, 500), new Point2D(100, 0));
        objects.add(player);

        for (int height = 50; height <= 300; height += 100) {
            for (int width = 200; width <= View.WIDTH - 200; width += 100) {
                ModelObject alien = new Alien(new Point2D(width, height), new Point2D(100, 0));
                alienList.add(alien);
            }
        }
    }

    public synchronized void newGame() {
        newLaserCanShoot = true;
        score = 0;
        lives = 3;
        sectionOfMoving = 1;
        moveRight = true;
        isOnlyBoss = false;
        level = 1;
        bossHP = 1000;

        objects.clear();
        alienList.clear();

        player = new Player(new Point2D(View.WIDTH / 2, 500), new Point2D(100, 0));
        objects.add(player);

        for (int height = 50; height <= 300; height += 100) {
            for (int width = 200; width <= View.WIDTH - 200; width += 100) {
                ModelObject alien = new Alien(new Point2D(width, height), new Point2D(100, 0));
                alienList.add(alien);
            }
        }
    }

    public synchronized void initBossFight() {
        objects.clear();
        alienList.clear();
        moveRight = true;
        isOnlyBoss = true;
        bossHP = 1000;
        ModelObject boss = new Boss(new Point2D(View.WIDTH / 2, 200), new Point2D(100, 0), new Point2D(20, 0));
        alienList.add(boss);
        player = new Player(new Point2D(View.WIDTH / 2, 500), new Point2D(100, 0));
        objects.add(player);
    }

    public synchronized void movePlayer(int direction) {
        if (player != null) {
            if (player.getX() + direction > 10 && player.getX() + direction < View.WIDTH - 10) {
                player.move(direction, 0);
            }
        }
    }

    public synchronized void playerShot() {
        if (player != null) {
            if (this.getNewLaserCanShoot()) {
                Point2D target = new Point2D(player.getX(), 0);
                Point2D player = this.player.getPosition();
                Point2D speed = target.subtract(player);

                speed = speed.normalize();
                speed = speed.multiply(10);
                Point2D laserStartPosition = player.add(speed.multiply(7));

                PlayerLaser playerLaser = new PlayerLaser(laserStartPosition, new Point2D(100, 0), speed);
                playerLaser.setDirectionToPoint(laserStartPosition.add(speed.multiply(10)));
                objects.add(playerLaser);
                setNewLaserCanShoot(false);
            }
        }
    }

    public synchronized void alienShot() {
        if (!alienList.isEmpty()) {
            Random rand = new Random();
            int indexOfAlien = rand.nextInt((alienList.size() - 1) + 1);
            Point2D target = new Point2D(alienList.get(indexOfAlien).getX(), View.HEIGHT);
            Point2D alien = this.alienList.get(indexOfAlien).getPosition();
            Point2D speed = target.subtract(alien);

            speed = speed.normalize();
            speed = speed.multiply(10);
            Point2D laserStartPosition = alien.add(speed.multiply(7));

            AlienShot alienShot = new AlienShot(laserStartPosition, new Point2D(100, 0), speed);
            alienShot.setDirectionToPoint(laserStartPosition.add(speed.multiply(10)));
            objects.add(alienShot);
        }
    }

    public void moveAliens(View view) {
        if (!alienList.isEmpty()) {
            Image monster = (isOnlyBoss) ? view.getBoss() : view.getAlien();

            if (this.moveRight) {
                for (ModelObject alien : alienList) {
                    alien.move(10 * getSectionOfMoving(), 0);
                }
                if (getMostRightAlienInList().getX() + 10 * getSectionOfMoving() + monster.getWidth() / 2 >= 800) {
                    this.moveRight = false;
                    moveAliensDown();
                }
            } else {
                for (ModelObject alien : alienList) {
                    alien.move(-10 * getSectionOfMoving(), 0);
                }

                if (getMostLeftAlienInList().getX() - 10 * getSectionOfMoving() - monster.getWidth() / 2 <= 0) {
                    this.moveRight = true;
                    moveAliensDown();
                }
            }
        }
    }

    public boolean isCollision(ModelObject object1, ModelObject object2, View view) {
        double Xa1, Ya1, Xa2, Ya2, Xb1, Yb1, Xb2, Yb2;

        Xa1 = object1.getX() - object1.getImage(view).getWidth() / 2;
        Ya1 = object1.getY() - object1.getImage(view).getHeight() / 2;
        Xa2 = object1.getX() + object1.getImage(view).getWidth() / 2;
        Ya2 = object1.getY() + object1.getImage(view).getHeight() / 2;

        Xb1 = object2.getX() - object2.getImage(view).getWidth() / 2;
        Yb1 = object2.getY() - object2.getImage(view).getHeight() / 2;
        Xb2 = object2.getX() + object2.getImage(view).getWidth() / 2;
        Yb2 = object2.getY() + object2.getImage(view).getHeight() / 2;

        return ((Xb1 - Xa2) * (Xb2 - Xa1) <= 0) && ((Yb1 - Ya2) * (Yb2 - Ya1) <= 0);
    }

    private void moveAliensDown() {
        for (ModelObject alien : alienList) {
            alien.move(0, 20);
        }
    }

    public ModelObject getMostRightAlienInList() {
        ModelObject tmp;
        tmp = getAlienObjects().get(0);
        for (ModelObject alien : getAlienObjects()) {
            if (alien.getX() > tmp.getX()) {
                tmp = alien;
            }
        }

        return tmp;
    }

    public ModelObject getMostLeftAlienInList() {
        ModelObject tmp;
        tmp = getAlienObjects().get(0);
        for (ModelObject alien : getAlienObjects()) {
            if (alien.getX() < tmp.getX()) {
                tmp = alien;
            }
        }

        return tmp;
    }
}
