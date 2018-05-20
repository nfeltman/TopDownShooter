package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class ShooterState {
    double time;
    Vector2d location;
    double speed;
    boolean wPressed;
    boolean aPressed;
    boolean sPressed;
    boolean dPressed;
    ArrayList<MovingPoint> bullets;
    int bulletsDodged;
    int maxDodged;
    ArrayList<MovingPoint> yourBullets;
    ArrayList<Ship> ships;

}
