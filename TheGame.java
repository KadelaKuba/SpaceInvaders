package thegame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TheGame extends Application {

    private Model model;
    private View view;
    private Controller controller;

    public TheGame() {
        model = new Model();
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane basePane = new AnchorPane();
        Button btnStart = new Button();
        btnStart.setText("Start game");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (controller.isRunning()) {
                    //action.stop();
                    controller.stop();
                    btnStart.setText("Start game");
                } else {
                    model.initGame();
                    model.initBossFight();
                    controller.start();
                    btnStart.setText("Stop game");
                }
            }
        });
        basePane.getChildren().add(btnStart);
        AnchorPane.setTopAnchor(btnStart, 0.0);
        AnchorPane.setLeftAnchor(btnStart, 0.0);
        AnchorPane.setRightAnchor(btnStart, 0.0);

        Pane root = new Pane();
        Canvas canvas = new Canvas(View.WIDTH, View.HEIGHT);
        root.getChildren().add(canvas);
        canvas.scaleXProperty().bind(root.widthProperty().multiply(1.0 / View.WIDTH));
        canvas.scaleYProperty().bind(root.heightProperty().multiply(1.0 / View.HEIGHT));
        canvas.translateXProperty().bind(root.widthProperty().subtract(View.WIDTH).divide(2));
        canvas.translateYProperty().bind(root.heightProperty().subtract(View.HEIGHT).divide(2));
        canvas.setFocusTraversable(true);

        view = new View(canvas.getGraphicsContext2D(), model);
        controller = new Controller(view, model);

        basePane.getChildren().add(root);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setTopAnchor(root, 30.0);

        Scene scene = new Scene(basePane, 800, 630);
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    model.movePlayer(-10);
                    view.update();
                }
                if (event.getCode() == KeyCode.RIGHT) {
                    model.movePlayer(10);
                    view.update();
                }
                if (event.getCode() == KeyCode.SHIFT) {
                    model.playerShot();
                    view.update();
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}