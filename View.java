package thegame;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class View {

    public final static int HEIGHT = 600;
    public final static int WIDTH = 800;

    private final GraphicsContext context;
    private final Image backgroud, player, laser, alien;

    private final Model model;

    public Image getLaser() {
        return laser;
    }

    public Image getAlien() {
        return alien;
    }

    View(GraphicsContext context, Model model) {
        this.context = context;
        backgroud = new Image("file:src/thegame/image/backgroundSkin.jpg");
        player = new Image("file:src/thegame/image/player.png");
        laser = new Image("file:src/thegame/image/laser.png");
        alien = new Image("file:src/thegame/image/alien.png");
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
        context.drawImage(backgroud, 0, 0, WIDTH, HEIGHT);
        context.setStroke(Color.BLACK);
        context.setLineWidth(2);
        context.strokeRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        synchronized (model) {
            for (ModelObject object : model.getObjects()) {
                context.save();
                rotate(object.getDirection(), object.getPosition());
                if (object instanceof Player) {
                    drawImage(player, object.getPosition());
                } else if (object instanceof Laser) {
                    drawImage(laser, object.getPosition());
                }
                context.restore();
            }
            for (ModelObject object : model.getAlienObjects()) {
                context.save();
                rotate(object.getDirection(), object.getPosition());
                drawImage(alien, object.getPosition());
                context.restore();
            }
        }
    }
}
