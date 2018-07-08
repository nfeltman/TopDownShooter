package com.dugonggames.shooter.shooter;

import javafx.scene.image.Image;

public class GameImages {
    public static Image background;
    public static Image enemyShip;
    public static Image friendlyShip;
    public static Image yourBullet;
    public static Image enemyBullet;
    public static Image shieldPw;
    public static Image shieldPwBig;
    public static Image speedPw;
    public static Image speedPwBig;
    public static Image damageBoost;
    public static Image damageBoostBig;
    public static Image tripleShot;
    public static Image tripleShotBig;

    public static void loadImages(){
        background = new Image("file:resources/background.png");
        enemyShip = new Image("file:resources/enemy_spaceship.png");
        friendlyShip = new Image("file:resources/friendly_spaceship.png", 30, 30, true, true);
        yourBullet = new Image("file:resources/yourBullet.png", 20, 20, true, true);
        enemyBullet = new Image("file:resources/enemyBullet.png", 20, 20, true, true);
        shieldPw = new Image("file:resources/shield_pw.png", 15, 15, true, true);
        shieldPwBig = new Image("file:resources/shield_pw.png", 45, 45, true, true);
        speedPw = new Image("file:resources/speed_pw.png", 20, 20, true, true);
        speedPwBig = new Image("file:resources/speed_pw.png", 60, 60, true, true);
        damageBoost = new Image("file:resources/damageBoost.png", 20, 20, true, true);
        damageBoostBig = new Image("file:resources/damageBoost.png", 60, 60, true, true);
        tripleShot = new Image("file:resources/tripleShot.png", 20, 20, true, true);
        tripleShotBig = new Image("file:resources/tripleShot.png", 50, 50, true, true);
    }
}
