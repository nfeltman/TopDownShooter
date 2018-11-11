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
    int health;
    double speed;
    HeldButtonState wasd;
    EntitySet<MovingPoint> bullets;
    double score;
    double maxScore;
    EntitySet<MovingPoint> yourBullets;
    EntitySet<Ship> ships;
    EntitySet<HomingMissile> homingMissiles;
    EntitySet<RookBomb> rookBombs;
    EntitySet<Wall> walls;
    double bulletTimer;
    double shipTimer;
    double pwTimer;
    Inventory inventory;
    BuffsManager buffsManager;
    DropsManager dropsManager;
    AnimationManager animationManager;
    boolean paused;

}
