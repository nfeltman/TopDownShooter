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
    HeldButtonState wasd;
    ArrayList<MovingPoint> bullets;
    int score;
    int maxScore;
    ArrayList<MovingPoint> yourBullets;
    ArrayList<Ship> ships;
    int bulletTimer;
    int shipTimer;
    int pwTimer;
    int speedBoostTime;
    ArrayList<Vector2d> speedPwLocs;
    int shieldBoostTime;
    ArrayList<Vector2d> shieldPwLocs;
    int damageBoostTime;
    ArrayList<Vector2d> damageBoostLocs;
    int tripleShotTime;
    ArrayList<Vector2d> tripleShotLocs;
    boolean paused;

}
