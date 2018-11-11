package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.graphics.animations.GfxUtils;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Rotation;
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

    public void draw(GfxWrapper gfx, Vector2d target){
        gfx.setColor(Color.LIGHTBLUE);
        gfx.strokeArc(location.location, 120, 5, 0, 360);

        Rotation angle = Rotation.fromVectors(new Vector2d(0, 1), location.location.subtract(target));
        gfx.drawRotatedImage(GameImages.enemyShip, angle, location.location);

        GfxUtils.healthBar(gfx, health, 20.0, location.location, 10);
    }
}
