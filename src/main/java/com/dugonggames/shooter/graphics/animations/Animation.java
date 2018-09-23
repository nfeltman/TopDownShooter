package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Vector2d;

public interface Animation {

    double getDuration();
    void drawFrame(GfxWrapper gfx, Vector2d loc, double t);
}
