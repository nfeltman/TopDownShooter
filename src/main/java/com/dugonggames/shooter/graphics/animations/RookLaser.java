package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.paint.Color;

public class RookLaser implements Animation{
    public int getNumFrames(){
        return 20;
    }
    public void drawFrame(GfxWrapper gfx, Vector2d loc, int frame) {
        gfx.setColor(new Color(1, 0, 0, ((double)(getNumFrames() - frame))/getNumFrames()));
        gfx.fillRect(new Box(loc.y - 3, loc.y + 3, 0, 5000));
        gfx.fillRect(new Box(0, 5000, loc.x - 3, loc.x + 3));
    }
}
