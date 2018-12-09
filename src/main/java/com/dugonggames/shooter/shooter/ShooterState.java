package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.animations.AnimationManager;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;

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

    @java.beans.ConstructorProperties({"playArea", "enemyShipArea", "time", "location", "health", "speed", "wasd", "bullets", "score", "maxScore", "yourBullets", "ships", "homingMissiles", "rookBombs", "walls", "bulletTimer", "shipTimer", "pwTimer", "inventory", "buffsManager", "dropsManager", "animationManager", "paused"})
    public ShooterState(Box playArea, Box enemyShipArea, double time, Vector2d location, int health, double speed, HeldButtonState wasd, EntitySet<MovingPoint> bullets, double score, double maxScore, EntitySet<MovingPoint> yourBullets, EntitySet<Ship> ships, EntitySet<HomingMissile> homingMissiles, EntitySet<RookBomb> rookBombs, EntitySet<Wall> walls, double bulletTimer, double shipTimer, double pwTimer, Inventory inventory, BuffsManager buffsManager, DropsManager dropsManager, AnimationManager animationManager, boolean paused) {
        this.playArea = playArea;
        this.enemyShipArea = enemyShipArea;
        this.time = time;
        this.location = location;
        this.health = health;
        this.speed = speed;
        this.wasd = wasd;
        this.bullets = bullets;
        this.score = score;
        this.maxScore = maxScore;
        this.yourBullets = yourBullets;
        this.ships = ships;
        this.homingMissiles = homingMissiles;
        this.rookBombs = rookBombs;
        this.walls = walls;
        this.bulletTimer = bulletTimer;
        this.shipTimer = shipTimer;
        this.pwTimer = pwTimer;
        this.inventory = inventory;
        this.buffsManager = buffsManager;
        this.dropsManager = dropsManager;
        this.animationManager = animationManager;
        this.paused = paused;
    }
}
