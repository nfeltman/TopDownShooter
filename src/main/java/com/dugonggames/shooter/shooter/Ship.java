package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.util.MovingPoint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Ship {
    MovingPoint location;
    int health;

    public void draw(GraphicsContext gc, double angle){
        double x = location.location.x;
        double y = location.location.y;

        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineWidth(5);
        gc.strokeOval(x-120, y-120, 240, 240);
        /*gc.fillOval(x - 120, y - 120, 240, 240);
        gc.setFill(Color.BLACK);
        gc.fillOval(x - 115, y - 115, 230, 230);*/

        drawRotatedImage(gc, GameImages.enemyShip.getImage(), angle, x-100, y-110);

        gc.setFill(Color.LIGHTBLUE);
        gc.setFill(Color.RED);
        gc.fillRect(x - 100, y - 70, 200, 10);
        gc.setFill(Color.GREEN);
        gc.fillRect(x - 100, y - 70, 200 * (health/20.0), 10);
    }

    public void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }
}
