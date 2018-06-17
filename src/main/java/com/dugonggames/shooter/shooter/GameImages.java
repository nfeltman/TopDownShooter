package com.dugonggames.shooter.shooter;

import javafx.scene.image.Image;

public class GameImages {
    public static Image enemyShip;
    public static Image friendlyShip;
    public static Image shieldPw;

    public static void loadImages(){
        enemyShip = new Image("file:resources/enemy_spaceship.png");
        friendlyShip = new Image("file:resources/friendly_spaceship.png", 30, 30, true, true);
        shieldPw = new Image("file:resources/shield_pw.png", 10, 10, true, true);
    }
}
