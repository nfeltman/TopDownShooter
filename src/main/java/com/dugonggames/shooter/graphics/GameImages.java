package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.graphics.animations.ImageSequence;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.image.Image;

public class GameImages {
    public static CenteredImage background;
    public static CenteredImage enemyShip;
    public static CenteredImage enemyShip2;
    public static CenteredImage enemyShip3;
    public static CenteredImage homingMissile;
    public static CenteredImage rookBomb;
    public static CenteredImage friendlyShip;
    public static CenteredImage yourBullet;
    public static CenteredImage enemyBullet;
    public static CenteredImage shieldPw;
    public static CenteredImage shieldPwBig;
    public static CenteredImage speedPw;
    public static CenteredImage speedPwBig;
    public static CenteredImage damageBoost;
    public static CenteredImage damageBoostBig;
    public static CenteredImage tripleShot;
    public static CenteredImage tripleShotBig;
    public static CenteredImage wall;
    public static ImageSequence explosionSequence;

    public static void loadImages(){
        background = new CenteredImage(new Image("file:resources/background.png"));
        enemyShip = new CenteredImage(new Vector2d(100,120), new Image("file:resources/enemy_spaceship.png"));
        enemyShip2 = new CenteredImage(new Vector2d(100,120), new Image("file:resources/enemyShip2.png"));
        enemyShip3 = new CenteredImage(new Vector2d(100,120), new Image("file:resources/enemyShip3.png"));
        homingMissile = new CenteredImage(new Image("file:resources/homingMissile.png", 60, 60, true, true));
        rookBomb = new CenteredImage(new Image("file:resources/RookBomb.png", 30, 30, true, true));
        friendlyShip = new CenteredImage(new Image("file:resources/friendly_spaceship.png", 30, 30, true, true));
        yourBullet = new CenteredImage(new Image("file:resources/yourBullet.png", 20, 20, true, true));
        enemyBullet = new CenteredImage(new Image("file:resources/enemyBullet.png", 20, 20, true, true));
        shieldPw = new CenteredImage(new Image("file:resources/shield_pw.png", 15, 15, true, true));
        shieldPwBig = new CenteredImage(new Image("file:resources/shield_pw.png", 45, 45, true, true));
        speedPw = new CenteredImage(new Image("file:resources/speed_pw.png", 20, 20, true, true));
        speedPwBig = new CenteredImage(new Image("file:resources/speed_pw.png", 60, 60, true, true));
        damageBoost = new CenteredImage(new Image("file:resources/damageBoost.png", 20, 20, true, true));
        damageBoostBig = new CenteredImage(new Image("file:resources/damageBoost.png", 60, 60, true, true));
        tripleShot = new CenteredImage(new Image("file:resources/tripleShot.png", 20, 20, true, true));
        tripleShotBig = new CenteredImage(new Image("file:resources/tripleShot.png", 50, 50, true, true));
        wall = new CenteredImage(new Image("file:resources/wall.png", 100, 20, true, true));
        explosionSequence = new ImageSequence(ImageSequence.createSequence(new Image("file:resources/explosion1.png", 4096, 4096, true, true), 8, 8, 0), 0.8);

    }
}
