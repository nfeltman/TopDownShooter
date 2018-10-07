package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.graphics.animations.AnimationManager;
import com.dugonggames.shooter.graphics.animations.RookLaser;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.dugonggames.shooter.shooter.BuffsManager.BuffType.*;
import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;
import static com.dugonggames.shooter.shooter.Inventory.Item.*;

public class ShooterSim{


    public ShooterState init(int width, int height) {
        GameImages.loadImages();
        return new ShooterState(new Box(0, height, 0, width), new Box(50, height - 50, 100, width - 100),0, new Vector2d(width/2, height/2), 5, new HeldButtonState(), new BulletSet(), 0, 0, new ArrayList<MovingPoint>(), new ArrayList<Ship>(), new ArrayList<HomingMissile>(), new ArrayList<RookBomb>(), 0, 0 , 15, new Inventory(), new BuffsManager(),  new DropsManager(), new AnimationManager(), false);
    }

    public void stepForward(ShooterState s, double t, ArrayList<KeyEvent> keyPresses, ArrayList<MouseEvent> mouseClicks, int width, int height) {
        if (s.paused) t = 0;
        final double dt = (s.buffsManager.isActiveBuff(TIME_BUFF) ? t/2 : t);
        s.time += dt;
        s.score = (s.score + (dt * 100));
        s.maxScore = Math.max(s.score, s.maxScore);


        s.homingMissiles = HomingMissile.advanceHomingMissiles(s.location, s.homingMissiles, dt);
        for (int i = 0; i < s.ships.size(); i++){
            MovingPoint nextEnemyLoc = s.ships.get(i).getLocation().step(dt).bounceInsideBox(s.enemyShipArea);
            if (s.ships.get(i).getMissileTimer() <= 0) {
                s.homingMissiles.add(new HomingMissile(s.location, nextEnemyLoc.location, 3));
                s.ships.set(i, new Ship(s.ships.get(i).getLocation(), s.ships.get(i).getHealth(), 6));
            }
            s.ships.set(i, new Ship(nextEnemyLoc, s.ships.get(i).getHealth(), s.ships.get(i).getMissileTimer() - dt));

            if (s.bulletTimer < 0) {
                s.bullets.add(BulletSpawner.makeBullet(s.location, nextEnemyLoc.location));
                s.bulletTimer = 0.1;
            }
            s.bulletTimer -= dt;
                //nextBulletSet = CircleAttack(nextBulletSet, s.location, nextEnemyLoc.location, 8);
                // This method doesnt work- no pauses in between bullets
        }

        if (s.shipTimer < 0){
            Vector2d nextShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
            while (((nextShipLocation.x > (width/2)+200) || (nextShipLocation.x < (width/2) - 100)) && ((nextShipLocation.y > (height/2)+200) || (nextShipLocation.y < (height/2) - 100))){
                nextShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
            }
            Vector2d nextShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
            while (Math.abs(nextShipVelocity.x) < 100 && Math.abs(nextShipVelocity.x) < 66){
                nextShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
            }
            s.ships.add(new Ship(new MovingPoint(nextShipLocation, nextShipVelocity), 20, 6));
            if (s.score <= 25) s.shipTimer = 7.5;
            else if (s.score <= 5000) s.shipTimer = 6;
            else if (s.score <= 7500) s.shipTimer = 5.5;
            else if (s.score <= 10000) s.shipTimer = 3.25;
            else s.shipTimer = 3;
        }
        s.shipTimer -= dt;

        for (int i = 0; i < s.rookBombs.size(); i++){
            if (s.rookBombs.get(i).time < 0){
                for (int j = 0; j < s.ships.size(); j++){
                    if (Math.abs(s.ships.get(j).getLocation().location.x - s.rookBombs.get(i).location.x) < 120 || Math.abs(s.ships.get(j).getLocation().location.y - s.rookBombs.get(i).location.y) < 120) {
                        s.ships.set(j, new Ship(s.ships.get(j).getLocation(), s.ships.get(j).getHealth() - 5, s.ships.get(j).getMissileTimer()));
                        if (s.ships.get(j).getHealth() <= 0)
                            s.ships.remove(j);
                    }
                }
                for (int j = 0; j < s.homingMissiles.size(); j++){
                    if (Math.abs(s.homingMissiles.get(j).location.location.x - s.rookBombs.get(i).location.x) < 30 || Math.abs(s.homingMissiles.get(j).location.location.y - s.rookBombs.get(i).location.y) < 10) {
                        s.homingMissiles.set(j, new HomingMissile(s.location, s.homingMissiles.get(j).location.location, s.homingMissiles.get(j).health - 1));
                        if (s.homingMissiles.get(j).health <= 0)
                            s.homingMissiles.remove(j);
                    }
                }
                s.animationManager.addAnimation(new RookLaser(), s.rookBombs.get(i).location);
                s.rookBombs.remove(i);
            } else {
                s.rookBombs.set(i, new RookBomb(s.rookBombs.get(i).location, s.rookBombs.get(i).time - dt));
            }
        }

        for (MouseEvent event : mouseClicks){
            if (!s.paused) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.PRIMARY)) {
                    double x = event.getX();
                    double y = event.getY();

                    Vector2d nextVelocity = new Vector2d(x, y).subtract(s.location).normalize().scale(1000);
                    s.yourBullets.add(new MovingPoint(s.location, nextVelocity));
                    if (s.buffsManager.isActiveBuff(TRIPLESHOT_BUFF)) {
                        s.yourBullets.add(new MovingPoint(s.location, nextVelocity.rotatePiOver8()));

                        s.yourBullets.add(new MovingPoint(s.location, nextVelocity.rotateNegativePiOver8()));
                    }
                } else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.SECONDARY)) {
                    if (s.rookBombs.size() == 0)
                        s.rookBombs.add(new RookBomb(s.location, 2));
                } else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.MIDDLE)) {
                    s.buffsManager.activateBuff(TIME_BUFF, 500);
                }
            }
        }

        for (int i = 0; i < s.yourBullets.size(); i++){
            boolean collided = false;
            for (int j = 0; j < s.ships.size(); j++){
                if (Vector2d.distance(s.yourBullets.get(i).location, s.ships.get(j).getLocation().location) <= 120){
                    s.ships.set(j,  new Ship(s.ships.get(j).getLocation(), s.ships.get(j).getHealth() - (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1), s.ships.get(j).getMissileTimer()));
                    s.animationManager.addAnimation(GameImages.explosionSequence, s.yourBullets.get(i).location);
                    if (s.ships.get(j).getHealth() <= 0){
                        s.ships.remove(j);
                        s.score += 500;
                    }
                    collided = true;
                }
            }
            for (int j = 0; j < s.homingMissiles.size(); j++){
                if (Vector2d.distance(s.yourBullets.get(i).location, s.homingMissiles.get(j).location.location) <= 16){
                    s.homingMissiles.set(j,  new HomingMissile(s.location, s.homingMissiles.get(j).location.location, s.homingMissiles.get(j).health - (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1)));
                    s.animationManager.addAnimation(GameImages.explosionSequence, s.yourBullets.get(i).location);
                    if (s.homingMissiles.get(j).health <= 0){
                        s.homingMissiles.remove(j);
                        s.score += 50;
                    }
                    collided = true;
                }
            }
            if (!s.yourBullets.get(i).location.inBox(s.playArea) || collided)
                s.yourBullets.remove(i);
            else {
                s.yourBullets.set(i, s.yourBullets.get(i).step(dt));
            }
        }


        if (s.pwTimer <= 0) {
            s.dropsManager.placeDrop(new DropsManager.Drop(DropsManager.getRandomDrop(), s.enemyShipArea.randomPoint()));
            s.pwTimer = 15;
        }
        s.pwTimer -= dt;

        ArrayList<DropsManager.Drop> pickedUpDrops = s.dropsManager.pickUpDrops(s.location);

        if (s.buffsManager.buffTimeLeft(SPEED_BUFF) == 499) s.speed *= 2;

        for (DropsManager.Drop drop : pickedUpDrops) {
            if (drop.dropType == SPEED_DROP)
                s.inventory.increment(SPEED_BOOST);
            if (drop.dropType == SHIELD_DROP)
                s.inventory.increment(SHIELD);
            if (drop.dropType == DAMAGE_DROP)
                s.inventory.increment(DAMAGE_BOOST);
            if (drop.dropType == TRIPLESHOT_DROP)
                s.inventory.increment(TRIPLE_SHOT);
        }

        if (!s.buffsManager.isActiveBuff(SPEED_BUFF)) s.speed = 5;

        for (KeyEvent k : keyPresses){
            if (KeyCode.ESCAPE == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED) s.paused = !s.paused;
            s.wasd.updateWithEvent(k, s.paused);
            if (!s.paused) {
                if (KeyCode.DIGIT1 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(SPEED_BOOST)) {
                    s.inventory.decrement(SPEED_BOOST);
                    s.buffsManager.activateBuff(SPEED_BUFF, 500);
                }
                if (KeyCode.DIGIT2 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(SHIELD)) {
                    s.inventory.decrement(SHIELD);
                    s.buffsManager.activateBuff(SHIELD_BUFF, 250);
                }
                if (KeyCode.DIGIT3 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(DAMAGE_BOOST)) {
                    s.inventory.decrement(DAMAGE_BOOST);
                    s.buffsManager.activateBuff(DAMAGE_BUFF, 500);
                }
                if (KeyCode.DIGIT4 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(TRIPLE_SHOT)) {
                    s.inventory.decrement(TRIPLE_SHOT);
                    s.buffsManager.activateBuff(TRIPLESHOT_BUFF, 500);
                }
            }
        }

        Vector2d nextMoveTo = new Vector2d(0, 0);
        if (s.wasd.wPressed) nextMoveTo = new Vector2d(nextMoveTo.x, nextMoveTo.y - s.speed);
        if (s.wasd.aPressed) nextMoveTo = new Vector2d(nextMoveTo.x - s.speed, nextMoveTo.y);
        if (s.wasd.sPressed) nextMoveTo = new Vector2d(nextMoveTo.x, nextMoveTo.y + s.speed);
        if (s.wasd.dPressed) nextMoveTo = new Vector2d(nextMoveTo.x + s.speed, nextMoveTo.y);
        nextMoveTo = nextMoveTo.normalize();
        if (s.buffsManager.isActiveBuff(TIME_BUFF)) nextMoveTo = nextMoveTo.scale(0.5);
        s.location = s.location.add(nextMoveTo.scale(s.speed));

        s.bullets = s.bullets.filter(b->b.location.inBox(s.playArea));
        s.bullets = s.bullets.map(b->b.step(dt));
        if (s.bullets.any(b->Vector2d.distance(b.location, s.location) < 6.5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF))) {
            s.bullets = new BulletSet();
            s.ships.clear();
            s.homingMissiles.clear();
            s.score = 0;
        }
        for (HomingMissile missile : s.homingMissiles){
            if (Vector2d.distance(missile.location.location, s.location) <= 5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF)){
                s.bullets = new BulletSet();
                s.ships.clear();
                s.homingMissiles.clear();
                s.score = 0;
                break;
            }
        }

        s.buffsManager.tickTimer();

        if (!s.paused) {
            s.animationManager.advance(dt);
        } else {
            s.paused = true;
        }
    }

    public void draw(ShooterState s, GfxWrapper gfx, double width, double height) {

        gfx.drawImage(GameImages.background, new Vector2d(width / 2, height / 2));

        gfx.setColor(Color.YELLOW);
        gfx.strokeArc(s.location, 25, 5, 0, ((((double) s.buffsManager.buffTimeLeft(SPEED_BUFF)) / 500) * 360));

        if (s.buffsManager.buffTimeLeft(SHIELD_BUFF) % 50 > 10){
            gfx.setColor(Color.LIGHTBLUE);
            gfx.fillCircle(s.location, 30);
            gfx.setColor(Color.BLACK);
            gfx.fillCircle(s.location, 26);
        }

        gfx.drawImage(GameImages.friendlyShip, s.location);

        s.bullets.applyAll(b->gfx.drawImage(GameImages.enemyBullet, b.location));

        for (MovingPoint bullet : s.yourBullets){
            gfx.drawImage(GameImages.yourBullet, bullet.location);
        }

        for (HomingMissile missile : s.homingMissiles){
            double angle = s.location.subtract(missile.location.location).getAngle();
            gfx.drawRotatedImage(GameImages.homingMissile.getImage(), angle + 90, missile.location.location.x, missile.location.location.y);
        }

        for (RookBomb bomb : s.rookBombs){
            gfx.drawImage(GameImages.rookBomb, bomb.location);
        }

        gfx.setColor(Color.WHITE);
        gfx.drawText((int) Math.floor(s.score) + "", new Vector2d(20, 20));
        gfx.drawText("Max: " + (int) Math.floor(s.maxScore), new Vector2d(100, 20));

        for (Ship ship : s.ships){
            double angle = s.location.subtract(ship.getLocation().location).getAngle();
            ship.draw(gfx, angle);
        }

        for (DropsManager.Drop drop : s.dropsManager.getCurrentDrops()){
            if (drop.dropType == SPEED_DROP)
                gfx.drawImage(GameImages.speedPw, drop.position);
            if (drop.dropType == SHIELD_DROP)
                gfx.drawImage(GameImages.shieldPw, drop.position);
            if (drop.dropType == DAMAGE_DROP)
                gfx.drawImage(GameImages.damageBoost, drop.position);
            if (drop.dropType == TRIPLESHOT_DROP)
                gfx.drawImage(GameImages.tripleShot, drop.position);
        }

        if (s.inventory.hasAtLeastOne(SPEED_BOOST))
            gfx.drawImage(GameImages.speedPwBig, new Vector2d(30, height - 40));
        if (s.inventory.hasAtLeastOne(SHIELD))
            gfx.drawImage(GameImages.shieldPwBig, new Vector2d(87, height - 35));
        if (s.inventory.hasAtLeastOne(DAMAGE_BOOST))
            gfx.drawImage(GameImages.damageBoostBig, new Vector2d(150, height - 40));
        if (s.inventory.hasAtLeastOne(TRIPLE_SHOT))
            gfx.drawImage(GameImages.tripleShotBig, new Vector2d(220, height - 35));

        gfx.setColor(Color.WHITE);
        if (s.inventory.getCount(SPEED_BOOST) >= 2)
            gfx.drawText(s.inventory.getCount(SPEED_BOOST) + "", new Vector2d(27, height - 70));
        if (s.inventory.getCount(SHIELD) >= 2)
            gfx.drawText(s.inventory.getCount(SHIELD) + "", new Vector2d(84, height - 70));
        if (s.inventory.getCount(DAMAGE_BOOST) >= 2)
            gfx.drawText(s.inventory.getCount(DAMAGE_BOOST) + "", new Vector2d(147, height - 70));
        if (s.inventory.getCount(TRIPLE_SHOT) >= 2)
            gfx.drawText(s.inventory.getCount(TRIPLE_SHOT) + "", new Vector2d(217, height - 70));

        s.animationManager.draw(gfx);

        gfx.setColor(Color.DARKRED);
        if (s.paused){
            gfx.fillRect(new Box(height/2 - 150, height/2 + 150, width/2 - 100, width/2 - 50));
            gfx.fillRect(new Box(height/2 - 150,height/2 + 150,width/2 + 50, width/2 + 100));
        }
    }

    public BulletSet CircleAttack(BulletSet b, Vector2d target, Vector2d origin, int number) {

        for (int i = 0; i < number; i++){
            Vector2d relativetarget = target.subtract(origin).normalize().scale(300).rotate(i*2*6.283185/number);

            MovingPoint bullet = new MovingPoint(origin, relativetarget);
            b.add(bullet);
        }

        return b;
    }
}

