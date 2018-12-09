package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Rotation;
import com.dugonggames.shooter.util.Vector2d;

public class Wall {
    Vector2d location;
    Rotation angle;
    int health;

    @java.beans.ConstructorProperties({"location", "angle", "health"})
    public Wall(Vector2d location, Rotation angle, int health) {
        this.location = location;
        this.angle = angle;
        this.health = health;
    }
}
