package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;

import java.util.ArrayList;

public class HomingMissile {
    MovingPoint location;
    int health;

    public HomingMissile(Vector2d target, Vector2d origin, int health){
        location = new MovingPoint(origin, target.subtract(origin).normalize().scale(300));
        this.health = health;
    }

    public static ArrayList<HomingMissile> advanceHomingMissiles(Vector2d target, ArrayList<HomingMissile> missiles, double dt){
        ArrayList<HomingMissile> nextMissiles = new ArrayList<>();
        for (HomingMissile missile : missiles){
            nextMissiles.add(new HomingMissile(target, missile.location.step(dt).location, missile.health));
        }
        return nextMissiles;
    }
}
