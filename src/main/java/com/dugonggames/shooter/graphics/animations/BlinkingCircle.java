package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.paint.Color;

public class BlinkingCircle implements Animation {
    @Override
    public int getNumFrames() {
        return 15;
    }

    @Override
    public void drawFrame(GfxWrapper gfx, Vector2d loc, int frame) {
        if (frame % 10 < 5) {
            gfx.setColor(Color.ORANGE);
            gfx.fillCircle(loc, frame);
        }
    }
}
