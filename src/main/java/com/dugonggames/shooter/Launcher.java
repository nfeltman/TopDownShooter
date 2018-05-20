package com.dugonggames.shooter;

import com.dugonggames.shooter.shooter.ShooterSim;
import com.dugonggames.shooter.shooter.ShooterState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Launcher extends Application {
    // Size of the canvas for the Mandelbrot set
    private static final int CANVAS_WIDTH = 1500;
    private static final int CANVAS_HEIGHT = 1000;
    long lastNanoTime;
    ArrayList<KeyEvent> keyPressed = new ArrayList<KeyEvent>();
    ArrayList<MouseEvent> mouseClicked = new ArrayList<MouseEvent>();
    ShooterSim sim = new ShooterSim();
    ShooterState state;

    @Override
    public void start(Stage theStage) {
        theStage.setTitle( "shooted" );

        Group root = new Group();
        Scene scene = new Scene( root );
        theStage.setScene( scene );

        Canvas canvas = new Canvas( CANVAS_WIDTH, CANVAS_HEIGHT );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        lastNanoTime = System.nanoTime();
        state = sim.init(CANVAS_WIDTH, CANVAS_HEIGHT);

        scene.setOnKeyPressed(e -> keyPressed.add(e));
        scene.setOnKeyReleased(e -> keyPressed.add(e));
        scene.setOnMousePressed(e -> mouseClicked.add(e));
        scene.setOnMouseReleased(e -> mouseClicked.add(e));

        new AnimationTimer() {
            public void handle(long currentNanoTime){
                long actualDT = currentNanoTime - lastNanoTime;
                lastNanoTime = currentNanoTime;

                state = sim.stepForward(state, actualDT/1000000000.0, keyPressed, mouseClicked, CANVAS_WIDTH, CANVAS_HEIGHT);
                keyPressed = new ArrayList<KeyEvent>();
                mouseClicked = new ArrayList<MouseEvent>();

                // draw
                sim.draw(state, gc, CANVAS_WIDTH, CANVAS_HEIGHT);
            }
        }.start();

        theStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}