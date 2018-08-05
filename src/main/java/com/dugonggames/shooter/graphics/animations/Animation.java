package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Vector2d;

public interface Animation {

    int getNumFrames();
    void drawFrame(GfxWrapper gfx, Vector2d loc, int frame);
}
