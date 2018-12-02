package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.graphics.animations.AnimationManager;
import com.dugonggames.shooter.graphics.animations.GfxUtils;
import com.dugonggames.shooter.graphics.animations.RookLaser;
import com.dugonggames.shooter.util.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Optional;

import static com.dugonggames.shooter.shooter.BuffsManager.BuffType.*;
import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;
import static com.dugonggames.shooter.shooter.Inventory.Item.*;

public class ShooterSim{


    public ShooterState init(int width, int height) {
        GameImages.loadImages();
        return new ShooterState(new Box(0, height, 0, width), new Box(50, height - 50, 50, width - 50),0, new Vector2d(width/2, height/2), 50, 5, new HeldButtonState(), new EntitySet<MovingPoint>(), 0, 0, new EntitySet<MovingPoint>(), new EntitySet<Ship>(), new EntitySet<HomingMissile>(), new EntitySet<RookBomb>(), new EntitySet<Wall>(), 0, 0 , 15, new Inventory(), new BuffsManager(),  new DropsManager(), new AnimationManager(), false);
    }

    public void stepForward(ShooterState s, double t, ArrayList<KeyEvent> keyPresses, ArrayList<MouseEvent> mouseClicks, int width, int height) {
        if (s.paused) t = 0;
        final double dt = (s.buffsManager.isActiveBuff(TIME_BUFF) ? t/2 : t);
        s.time += dt;
        s.score = (s.score + (dt * 100));
        s.maxScore = Math.max(s.score, s.maxScore);


        s.homingMissiles = HomingMissile.advanceHomingMissiles(s.location, s.homingMissiles, dt);
        s.ships = s.ships.map(ship -> {
            MovingPoint nextEnemyLoc = ship.getLocation().step(dt).bounceInsideBox(s.enemyShipArea);
            if (ship.getMissileTimer() <= 0) {
                s.homingMissiles.add(new HomingMissile(s.location, nextEnemyLoc.location, 3));
                ship = new Ship(ship.getLocation(), ship.getHealth(), 6);
            }

            ship = new Ship(nextEnemyLoc, ship.getHealth(), ship.getMissileTimer() - dt);

            if (s.bulletTimer < 0) {
                s.bullets.add(BulletSpawner.makeBullet(s.location, nextEnemyLoc.location));
                s.bulletTimer = 0.1;
            }
            s.bulletTimer -= dt;

            return ship;
        });

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

        s.rookBombs = s.rookBombs.filterMap(b -> {
            if (b.time < 0){
                s.ships = s.ships.filterMap(ship -> {
                    if (Math.abs(ship.getLocation().location.x - b.location.x) < 120 || Math.abs(ship.getLocation().location.y - b.location.y) < 120) {
                        if (ship.getHealth() > 0)
                            return Optional.of(new Ship(ship.getLocation(), ship.getHealth() - 5, ship.getMissileTimer()));
                        else return Optional.empty();
                    } else return Optional.of(ship);
                });
                s.homingMissiles = s.homingMissiles.filterMap(m -> {
                    if (Math.abs(m.location.location.x - b.location.x) < 30 || Math.abs(m.location.location.y - b.location.y) < 10) {
                        if (m.health > 0)
                            return Optional.of(new HomingMissile(s.location, m.location.location, m.health - 5));
                        else return Optional.empty();
                    } else return Optional.of(m);
                });
                s.animationManager.addAnimation(new RookLaser(), b.location);
                return Optional.empty();
            } else {
                return Optional.of(new RookBomb(b.location, b.time - dt));
            }
        });

        for (MouseEvent event : mouseClicks){
            final Vector2d v = new Vector2d(event.getX(), event.getY());
            if (!s.paused) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.PRIMARY)) {

                    Vector2d nextVelocity = v.subtract(s.location).normalize().scale(1000);
                    s.yourBullets.add(new MovingPoint(s.location, nextVelocity));
                    if (s.buffsManager.isActiveBuff(TRIPLESHOT_BUFF)) {
                        s.yourBullets.add(new MovingPoint(s.location, nextVelocity.rotatePiOver8()));

                        s.yourBullets.add(new MovingPoint(s.location, nextVelocity.rotateNegativePiOver8()));
                    }
                } else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.SECONDARY)) {
                    if (s.rookBombs.size() == 0)
                        s.rookBombs.add(new RookBomb(s.location, 2));
                } else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.MIDDLE)) {
                    s.walls.add(new Wall(s.location, Rotation.fromVectors(new Vector2d(0, 1), v.subtract(s.location)), 5));
                }
            }
        }

        Pair<EntitySet<MovingPoint>, EntitySet<Ship>> bulletShipPair = s.yourBullets.mapCross(s.ships, (b, ship) -> {
            if (Vector2d.distance(b.location, ship.getLocation().location) <= 120){
                s.animationManager.addAnimation(GameImages.explosionSequence, b.location);
                if (ship.getHealth() > 1){
                    return new Pair<>(Optional.empty(), Optional.of(new Ship(ship.getLocation(), ship.getHealth() -  (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1), ship.getMissileTimer())));
                } else {
                    s.score += 500;
                    return new Pair<>(Optional.empty(), Optional.empty());
                }
            } else return new Pair<>(Optional.of(b), Optional.of(ship));
        });
        s.yourBullets = bulletShipPair.getA();
        s.ships = bulletShipPair.getB();

        Pair<EntitySet<MovingPoint>, EntitySet<HomingMissile>> bulletMissilePair = s.yourBullets.mapCross(s.homingMissiles, (b, m) -> {
            if (Vector2d.distance(b.location, m.location.location) <= 16){
                s.animationManager.addAnimation(GameImages.explosionSequence, b.location);
                if (m.health > 1){
                    return new Pair<>(Optional.empty(), Optional.of(new HomingMissile(s.location, m.location.location, m.health -  (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1))));
                } else {
                    s.score += 50;
                    return new Pair<>(Optional.empty(), Optional.empty());
                }
            } else return new Pair<>(Optional.of(b), Optional.of(m));
        });
        s.yourBullets = bulletMissilePair.getA();
        s.homingMissiles = bulletMissilePair.getB();

        s.yourBullets = s.yourBullets.filterMap(b -> {
            if (b.location.inBox(s.playArea)) return Optional.of(b.step(dt));
            else return Optional.empty();
        });

        if (s.pwTimer <= 0) {
            s.dropsManager.placeDrop(new DropsManager.Drop(DropsManager.getRandomDrop(), s.enemyShipArea.randomPoint()));
            s.pwTimer = 15;
        }
        s.pwTimer -= dt;

        EntitySet<DropsManager.Drop> pickedUpDrops = s.dropsManager.pickUpDrops(s.location);

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
        Pair<EntitySet<MovingPoint>, EntitySet<Wall>> bulletWallPair = s.bullets.mapCross(s.walls, (bullet, wall) -> {
            MovingPoint b = wall.angle.rotate(bullet.subtractLoc(wall.location));
            int health = wall.health;
            if (b.location.inBox(new Box(-10, 10, -50, 50)) && b.location.y > 0 != b.velocity.y > 0){
                b = new MovingPoint(b.location, new Vector2d(b.velocity.x, -b.velocity.y));
                health--;
            }
            if (health <= 0) return new Pair<>(Optional.of(wall.angle.negate().rotate(b).addLoc(wall.location)), Optional.empty());
            return new Pair<>(Optional.of(wall.angle.negate().rotate(b).addLoc(wall.location)), Optional.of(new Wall(wall.location, wall.angle, health)));
        });
        s.bullets = bulletWallPair.getA();
        s.bullets = s.bullets.map(b->b.step(dt));
        s.walls = bulletWallPair.getB();

        if (s.bullets.any(b->Vector2d.distance(b.location, s.location) < 6.5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF))) s.health--;
        s.bullets = s.bullets.filterMap(b -> {
            if (Vector2d.distance(b.location, s.location) < 6.5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF)){
                s.health--;
                return Optional.empty();
            }
            return Optional.of(b);
        });
        s.homingMissiles = s.homingMissiles.filterMap(missile -> {
            if (Vector2d.distance(missile.location.location, s.location) < 6.5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF)){
                s.health -= 3;
                return Optional.empty();
            }
            return Optional.of(missile);
        });
        if (s.health <= 0){
            s.bullets = new EntitySet<>();
            s.ships = new EntitySet<>();
            s.homingMissiles = new EntitySet<>();
            s.score = 0;
            s.health = 50;
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
        GfxUtils.healthBar(gfx, s.health, 50.0, s.location, 4);

        s.bullets.applyAll(b->gfx.drawImage(GameImages.enemyBullet, b.location));

        for (MovingPoint bullet : s.yourBullets){
            gfx.drawImage(GameImages.yourBullet, bullet.location);
        }

        for (HomingMissile missile : s.homingMissiles){
            Rotation angle = Rotation.fromVectors(new Vector2d(1, 0), missile.location.location.subtract(s.location));
            gfx.drawRotatedImage(GameImages.homingMissile, angle, missile.location.location);
        }

        for (RookBomb bomb : s.rookBombs){
            gfx.drawImage(GameImages.rookBomb, bomb.location);
        }

        for (Wall wall : s.walls){
            gfx.drawRotatedImage(GameImages.wall, wall.angle, wall.location);
        }

        gfx.setColor(Color.WHITE);
        gfx.drawText((int) Math.floor(s.score) + "", new Vector2d(20, 20));
        gfx.drawText("Max: " + (int) Math.floor(s.maxScore), new Vector2d(100, 20));

        for (Ship ship : s.ships){
            ship.draw(gfx, s.location);
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

    /*public BulletSet CircleAttack(BulletSet b, Vector2d target, Vector2d origin, int number) {

        for (int i = 0; i < number; i++){
            Vector2d relativetarget = target.subtract(origin).normalize().scale(300).rotate(i*2*6.283185/number);

            MovingPoint bullet = new MovingPoint(origin, relativetarget);
            b.add(bullet);
        }

        return b;
    }*/
}

