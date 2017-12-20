package thegame;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

public class View {

    public final static int HEIGHT = 600;
    public final static int WIDTH = 800;

    private final GraphicsContext context;
    private final Image background, player, laser, alien, boss;

    private final Model model;

    public Image getLaser() {
        return laser;
    }

    public Image getAlien() {
        return alien;
    }

    public Image getBoss() {
        return boss;
    }

    View(GraphicsContext context, Model model) {
        this.context = context;
        background = new Image("file:src/thegame/image/backgroundSkin.jpg");
        player = new Image("file:src/thegame/image/player.png");
        laser = new Image("file:src/thegame/image/laser.png");
        alien = new Image("file:src/thegame/image/alien.png");
        boss = new Image("file:src/thegame/image/boss.png");
        this.model = model;
        update();
    }

    private void drawImage(Image image, Point2D point) {
        context.drawImage(image, point.getX() - image.getWidth() / 2, point.getY() - image.getHeight() / 2);
    }

    private void rotate(double angle, Point2D center) {
        Rotate r = new Rotate(angle, center.getX(), center.getY());
        context.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public void update() {
        context.drawImage(background, 0, 0, WIDTH, HEIGHT);
        context.setStroke(Color.BLACK);
        context.setLineWidth(2);
        context.strokeRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        synchronized (model) {
            for (ModelObject object : model.getObjects()) {
                context.save();
                rotate(object.getDirection(), object.getPosition());
                if (object instanceof Player) {
                    drawImage(player, object.getPosition());
                } else if (object instanceof PlayerLaser) {
                    drawImage(laser, object.getPosition());
                } else if (object instanceof AlienShot) {
                    drawImage(laser, object.getPosition());
                }
                context.restore();
            }
            for (ModelObject object : model.getAlienObjects()) {
                context.save();
                rotate(object.getDirection(), object.getPosition());
                if (object instanceof Alien) {
                    drawImage(alien, object.getPosition());
                } else if (object instanceof Boss) {
                    drawImage(boss, object.getPosition());
                }
                context.restore();
            }
        }
    }

    public void showScore(int score, int lives) {
        String scoreString = Integer.toString(score);
        this.context.setFont(new Font("Times New Roman", 20));
        this.context.setFill(Color.GRAY);
        this.context.fillText("Score: " + scoreString, View.WIDTH - 120, View.HEIGHT - 30);

        String livesString = Integer.toString(lives);
        this.context.setFont(new Font("Times New Roman", 20));
        this.context.setFill(Color.GRAY);
        this.context.fillText("Lives: " + livesString, 20, View.HEIGHT - 30);
    }
}
