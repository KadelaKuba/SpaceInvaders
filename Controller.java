package thegame;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Controller {

    private Timeline timer;
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
                        if (object.isOutOfSpace()) {
                            model.setNewLaserCanShoot(true);
                            toDelete.add(object);
                        }
                    }
                    model.getObjects().removeAll(toDelete);
                }
                view.update();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        this.model = model;
        this.view = view;
    }

    public boolean isRunning() {
        return timer.getStatus() == Timeline.Status.RUNNING;
    }

    void stop() {
        timer.stop();
    }

    void start() {
        view.update();
        timer.play();
    }
}
