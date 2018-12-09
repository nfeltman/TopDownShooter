package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Vector2d;

public class RookBomb {
    Vector2d location;
    double time;

    @java.beans.ConstructorProperties({"location", "time"})
    public RookBomb(Vector2d location, double time) {
        this.location = location;
        this.time = time;
    }
}
