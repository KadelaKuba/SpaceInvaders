package thegame;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Controller {

    private Timeline timer;
    private Timeline alienMoving;
    private Timeline alienShots;
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        timer = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                synchronized (model) { // zamek vlákna
                    ArrayList<ModelObject> toDelete = new ArrayList<>();
                    for (ModelObject object : model.getObjects()) {
                        object.process();
                        if (object instanceof PlayerLaser) {
                            if (object.isOutOfSpace()) {
                                model.setNewLaserCanShoot(true);
                                toDelete.add(object);
                            }
                            for (ModelObject alien : model.getAlienObjects()) {
                                if (model.solveCollison(object, alien, view)) {
                                    toDelete.add(alien);
                                    model.setScore(100);
                                    toDelete.add(object);
                                    model.setNewLaserCanShoot(true);
                                }
                            }
                        }
                    }
                    model.getObjects().removeAll(toDelete);
                    model.getAlienObjects().removeAll(toDelete);
                }
                view.update();
            }
        }));

        alienMoving = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (model) {
                    model.moveAliens();
                }
                view.update();
            }
        }));

        alienShots = new Timeline(new KeyFrame(Duration.millis(2000), new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (model) {
                    model.alienShot();
                }
                view.update();
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        alienMoving.setCycleCount(Timeline.INDEFINITE);
        alienShots.setCycleCount(Timeline.INDEFINITE);

        this.model = model;
        this.view = view;
    }

    public boolean isRunning() {
        return timer.getStatus() == Timeline.Status.RUNNING;
    }

    void stop() {
        timer.stop();
        alienMoving.stop();
        alienShots.stop();
    }

    void start() {
        view.update();
        timer.play();
        alienMoving.play();
        alienShots.play();
    }
}
