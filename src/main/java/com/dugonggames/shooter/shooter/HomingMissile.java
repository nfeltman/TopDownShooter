package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;

public class HomingMissile {
    MovingPoint location;
    int health;

    public HomingMissile(Vector2d target, Vector2d origin, int health){
        location = new MovingPoint(origin, target.subtract(origin).normalize().scale(300));
        this.health = health;
    }

    public static EntitySet<HomingMissile> advanceHomingMissiles(Vector2d target, EntitySet<HomingMissile> missiles, double dt){
        return missiles.map(m -> new HomingMissile(target, m.location.step((3*dt)/4).location, m.health));
    }
}
