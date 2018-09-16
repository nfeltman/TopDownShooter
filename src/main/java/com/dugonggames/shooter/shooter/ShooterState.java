package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.animations.AnimationManager;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class ShooterState {
    Box playArea;
    Box enemyShipArea;
    double time;
    Vector2d location;
    double speed;
    HeldButtonState wasd;
    BulletSet bullets;
    int score;
    int maxScore;
    ArrayList<MovingPoint> yourBullets;
    ArrayList<Ship> ships;
    ArrayList<HomingMissile> homingMissiles;
    ArrayList<RookBomb> rookBombs;
    int bulletTimer;
    int shipTimer;
    int pwTimer;
    Inventory inventory;
    BuffsManager buffsManager;
    DropsManager dropsManager;
    AnimationManager animationManager;
    boolean paused;

}
