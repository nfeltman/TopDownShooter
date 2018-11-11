package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.paint.Color;

public class GfxUtils {
    public static void healthBar(GfxWrapper gfx, int health, double healthTotal, Vector2d location, int size){
        if (health > 0) {
            gfx.setColor(Color.RED);
            gfx.fillRect(new Box(location.y - (7 * size), location.y - (6 * size), location.x - (10 * size), location.x + (10 * size)));
            gfx.setColor(Color.GREEN);
            gfx.fillRect(new Box(location.y - (7 * size), location.y - (6 * size), location.x - (10 * size), (location.x - (10 * size)) + (20 * size) * (health / healthTotal)));
        }
    }
}
