package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Ship {
    MovingPoint location;
    int health;

    public void draw(GraphicsContext gc){
        double x = location.location.x;
        double y = location.location.y;

        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineWidth(5);
        gc.strokeOval(x-120, y-120, 240, 240);
        /*gc.fillOval(x - 120, y - 120, 240, 240);
        gc.setFill(Color.BLACK);
        gc.fillOval(x - 115, y - 115, 230, 230);*/

        gc.drawImage(GameImages.enemyShip, x-100, y-110);

        gc.setFill(Color.LIGHTBLUE);
        gc.setFill(Color.RED);
        gc.fillRect(x - 100, y - 70, 200, 10);
        gc.setFill(Color.GREEN);
        gc.fillRect(x - 100, y - 70, 200 * (health/20.0), 10);
    }
}
