package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Ship {
    MovingPoint location;
    int health;
    double missileTimer;

    public void draw(GfxWrapper gfx, double angle){
        double x = location.location.x;
        double y = location.location.y;

        gfx.setColor(Color.LIGHTBLUE);
        gfx.strokeArc(new Vector2d(x, y), 120, 5, 0, 360);

        gfx.drawRotatedImage(GameImages.enemyShip.getImage(), angle, x-100, y-110);

        gfx.setColor(Color.RED);
        gfx.fillRect(new Box(y - 70,y - 60, x - 100,  x + 100));
        gfx.setColor(Color.GREEN);
        gfx.fillRect(new Box(y - 70, y - 60,x - 100,  (x - 100) + 200 * (health/20.0)));
    }
}
