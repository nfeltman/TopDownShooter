package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;

public class BulletSpawner {

    public static MovingPoint makeBullet(Vector2d target, Vector2d origin){
        return new MovingPoint(origin, target.subtract(origin).normalize().scale(300));
    }

}
