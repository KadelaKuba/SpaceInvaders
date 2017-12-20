package thegame;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import javax.swing.*;

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
//                    if (model.getLives() <= 0) {
//                        showGameOver();
//                    }
                    if (model.getAlienObjects().isEmpty()) {
                        model.setSectionOfMoving(2);
                        model.setLevel(1);
                        if (model.getLevel() % 3 == 0) {
                            model.initBossFight();
                            model.setBossHP(100);
                        } else {
                            model.initGame();
                        }
                    }
                    ArrayList<ModelObject> toDelete = new ArrayList<>();
                    for (ModelObject object : model.getObjects()) {
                        object.process();
                        if (object.isOutOfSpace()) {
                            if (object instanceof PlayerLaser) {
                                model.setNewLaserCanShoot(true);
                            }
                            toDelete.add(object);
                        }
                        if (object instanceof Player) {
                            for (ModelObject alien : model.getAlienObjects()) {
                                if (model.isCollision(object, alien, view)) {
                                    showGameOver();
                                }
                            }
                        }
                        if (object instanceof PlayerLaser) {
                            for (ModelObject alien : model.getAlienObjects()) {
                                if (model.isCollision(object, alien, view)) {
                                    if (alien instanceof Boss) {
                                        model.setBossHP(-100);
                                        if (model.getBossHP() == 0) {
                                            model.setScore(500);
                                            toDelete.add(alien);
                                        }
                                    } else {
                                        model.setScore(100);
                                        toDelete.add(alien);
                                    }
                                    toDelete.add(object);
                                    model.setNewLaserCanShoot(true);
                                }
                            }
                        }
                        if (object instanceof AlienShot) {
                            for (ModelObject modelObject : model.getObjects()) {
                                if (modelObject instanceof Player) {
                                    if (model.isCollision(object, modelObject, view)) {
                                        toDelete.add(object);
                                        model.setLives(-1);
                                    }
                                }
                            }
                        }
                    }
                    model.getObjects().removeAll(toDelete);
                    model.getAlienObjects().removeAll(toDelete);
                }
                view.update();
                view.showScore(model.getScore(), model.getLives());
            }
        }));

        alienMoving = new Timeline(new KeyFrame(Duration.millis(700), new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (model) {
                    model.moveAliens(view);
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

    public void showGameOver() {
        stop();
        JOptionPane.showMessageDialog(null, "Game over. To start again reopen the app");
    }
}
