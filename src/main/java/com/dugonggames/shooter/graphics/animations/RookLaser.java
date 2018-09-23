package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.paint.Color;

import java.util.Random;

public class RookLaser implements Animation{
    long seed;
    private static final double DURATION = 0.8;
    public RookLaser(){
        seed = (new Random()).nextLong();
    }
    public double getDuration(){
        return DURATION;
    }
    public void drawFrame(GfxWrapper gfx, Vector2d loc, double t) {
        gfx.setColor(new Color(0.7, 0, 0, 1 - (t / DURATION)));
        Random r = new Random();
        for (int i = 0; i < 200; i++) {
            Vector2d point = new Vector2d(r.nextDouble() * gfx.width, loc.y + (r.nextGaussian()));
            gfx.fillCircle(point, 7);
        }
        for (int i = 0; i < 200; i++) {
            Vector2d point = new Vector2d(loc.x + r.nextGaussian(), r.nextDouble() * gfx.height);
            gfx.fillCircle(point, 7);
        }
        /*
        gfx.setColor(new Color(1, 0, 0, ((double) (getNumFrames() - frame)) / getNumFrames()));gfx.fillRect(new Box(loc.y - 3, loc.y + 3, 0, gfx.width));
        gfx.fillRect(new Box(0, gfx.height, loc.x - 3, loc.x + 3));
        Random r = new Random(seed);
        for (int i = 0; i < 200; i++) {
            MovingPoint point = new MovingPoint(new Vector2d(r.nextDouble() * gfx.width, loc.y + (r.nextGaussian())),
                    new Vector2d(r.nextGaussian() * 10, r.nextGaussian() * 10));
            gfx.fillCircle((point.step(frame / 20.0)).location, 2);
        }
        for (int i = 0; i < 200; i++) {
            MovingPoint point = new MovingPoint(new Vector2d(loc.x + r.nextGaussian(), r.nextDouble() * gfx.height),
                    new Vector2d(r.nextGaussian() * 10, r.nextGaussian() * 10));
            gfx.fillCircle((point.step(frame / 20.0)).location, 2);
        }
        */
    }
}
