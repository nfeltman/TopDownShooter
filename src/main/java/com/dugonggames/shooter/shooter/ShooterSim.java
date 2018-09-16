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
        return new ShooterState(new Box(0, height, 0, width), new Box(50, height - 50, 100, width - 100),0, new Vector2d(width/2, height/2), 5, new HeldButtonState(), new BulletSet(), 0, 0, new ArrayList<MovingPoint>(), new ArrayList<Ship>(), new ArrayList<HomingMissile>(), new ArrayList<RookBomb>(), 0, 0 , 1, new Inventory(), new BuffsManager(),  new DropsManager(), new AnimationManager(), false);
    }

    public ShooterState stepForward(ShooterState s, double dt, ArrayList<KeyEvent> keyPresses, ArrayList<MouseEvent> mouseClicks, int width, int height) {
        boolean paused = s.paused;
        Vector2d nextLoc = s.location;
        double nextTime = s.time + dt;
        int nextScore = s.score + 1;
        BulletSet nextBulletSet = new BulletSet();
        final BulletSet nextBulletSetC = nextBulletSet;
        s.bullets.applyAll(b->nextBulletSetC.add(b));


        ArrayList<Ship> nextShips = new ArrayList<>();
        ArrayList<HomingMissile> nextHomingMissiles = HomingMissile.advanceHomingMissiles(nextLoc, s.homingMissiles, dt);
        for (Ship ship : s.ships){
            MovingPoint nextEnemyLoc = ship.getLocation().step(dt).bounceInsideBox(s.enemyShipArea);
            nextShips.add(new Ship(nextEnemyLoc, ship.getHealth(), (ship.getMissileTimer() + 1) % 400));
            if (ship.getMissileTimer() == 399)
                nextHomingMissiles.add(new HomingMissile(nextLoc, nextEnemyLoc.location, 3));

            if (s.bulletTimer == 0)
                nextBulletSet.add(BulletSpawner.makeBullet(nextLoc, nextEnemyLoc.location));

                //nextBulletSet = CircleAttack(nextBulletSet, nextLoc, nextEnemyLoc.location, 8);
                // This method doesnt work- no pauses in between bullets
        }
        if (s.shipTimer == 0){
            Vector2d nextShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
            while (((nextShipLocation.x > (width/2)+200) || (nextShipLocation.x < (width/2) - 100)) && ((nextShipLocation.y > (height/2)+200) || (nextShipLocation.y < (height/2) - 100))){
                nextShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
            }
            Vector2d nextShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
            while (Math.abs(nextShipVelocity.x) < 100 && Math.abs(nextShipVelocity.x) < 66){
                nextShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
            }
            nextShips.add(new Ship(new MovingPoint(nextShipLocation, nextShipVelocity), 20, 0));
        }

        ArrayList<MovingPoint> yourNextBullets = new ArrayList<>();
        ArrayList<RookBomb> nextRookBombs = new ArrayList<>();
        for (RookBomb bomb : s.rookBombs){
            if (bomb.time > 0)
                nextRookBombs.add(new RookBomb(bomb.location, bomb.time - dt));
            else {
                for (int i = 0; i < nextShips.size(); i++){
                    if (Math.abs(nextShips.get(i).getLocation().location.x - bomb.location.x) < 120 || Math.abs(nextShips.get(i).getLocation().location.y - bomb.location.y) < 120) {
                        nextShips.set(i, new Ship(nextShips.get(i).getLocation(), nextShips.get(i).getHealth() - 5, nextShips.get(i).getMissileTimer()));
                        if (nextShips.get(i).getHealth() <= 0)
                            nextShips.remove(i);
                    }
                }
                for (int i = 0; i < nextHomingMissiles.size(); i++){
                    if (Math.abs(nextHomingMissiles.get(i).location.location.x - bomb.location.x) < 30 || Math.abs(nextHomingMissiles.get(i).location.location.y - bomb.location.y) < 10) {
                        nextHomingMissiles.set(i, new HomingMissile(s.location, nextHomingMissiles.get(i).location.location, nextHomingMissiles.get(i).health - 1));
                        if (nextHomingMissiles.get(i).health <= 0)
                            nextHomingMissiles.remove(i);
                    }
                }
                s.animationManager.addAnimation(new RookLaser(), bomb.location);
            }
        }

        for (MouseEvent event : mouseClicks){
            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.PRIMARY)){
                double x = event.getX();
                double y = event.getY();

                Vector2d nextLocation = new Vector2d(s.location.x, s.location.y);
                Vector2d nextVelocity = new Vector2d(x, y).subtract(nextLocation);
                nextVelocity = nextVelocity.normalize().scale(1000);
                yourNextBullets.add(new MovingPoint(nextLocation, nextVelocity));
                if (s.buffsManager.isActiveBuff(TRIPLESHOT_BUFF)){
                    double distance = Vector2d.distance(s.location, new Vector2d(x, y));

                    nextLocation = new Vector2d(s.location.x, s.location.y);
                    nextVelocity = nextVelocity.rotatePiOver8();
                    yourNextBullets.add(new MovingPoint(nextLocation, nextVelocity));

                    nextLocation = new Vector2d(s.location.x, s.location.y);
                    nextVelocity = nextVelocity.rotateNegativePiOver8().rotateNegativePiOver8();
                    yourNextBullets.add(new MovingPoint(nextLocation, nextVelocity));
                }
            } else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.SECONDARY)){
                double x = event.getX();
                double y = event.getY();

                if (nextRookBombs.size() == 0)
                    nextRookBombs.add(new RookBomb(s.location, 2));
            }
        }

        for (int i = 0; i < s.yourBullets.size(); i++){
            boolean collided = false;
            for (int j = 0; j < nextShips.size(); j++){
                if (Vector2d.distance(s.yourBullets.get(i).location, nextShips.get(j).getLocation().location) <= 120){
                    nextShips.set(j,  new Ship(nextShips.get(j).getLocation(), nextShips.get(j).getHealth() - (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1), nextShips.get(j).getMissileTimer()));
                    s.animationManager.addAnimation(GameImages.explosionSequence, s.yourBullets.get(i).location);
                    if (nextShips.get(j).getHealth() <= 0){
                        nextShips.remove(j);
                        nextScore += 500;
                    }
                    collided = true;
                }
            }
            for (int j = 0; j < nextHomingMissiles.size(); j++){
                if (Vector2d.distance(s.yourBullets.get(i).location, nextHomingMissiles.get(j).location.location) <= 16){
                    nextHomingMissiles.set(j,  new HomingMissile(s.location, nextHomingMissiles.get(j).location.location, nextHomingMissiles.get(j).health - (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1)));
                    s.animationManager.addAnimation(GameImages.explosionSequence, s.yourBullets.get(i).location);
                    if (nextHomingMissiles.get(j).health <= 0){
                        nextHomingMissiles.remove(j);
                        nextScore += 50;
                    }
                    collided = true;
                }
            }
            if (s.yourBullets.get(i).location.inBox(s.playArea) && !collided)
                yourNextBullets.add(s.yourBullets.get(i).step(dt));
        }


        if (s.pwTimer == 0) {
            s.dropsManager.placeDrop(new DropsManager.Drop(DropsManager.getRandomDrop(), s.enemyShipArea.randomPoint()));
        }

        ArrayList<DropsManager.Drop> pickedUpDrops = s.dropsManager.pickUpDrops(s.location);

        double nextSpeed = s.speed;
        if (s.buffsManager.buffTimeLeft(SPEED_BUFF) == 499) nextSpeed *= 2;

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

        if (!s.buffsManager.isActiveBuff(SPEED_BUFF)) nextSpeed = 5;

        for (KeyEvent k : keyPresses){
            s.wasd.updateWithEvent(k);
            if (KeyCode.ESCAPE == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED) paused = !paused;
            if (KeyCode.DIGIT1 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(SPEED_BOOST)){
                s.inventory.decrement(SPEED_BOOST);
                s.buffsManager.activateBuff(SPEED_BUFF, 500);
            }
            if (KeyCode.DIGIT2 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(SHIELD)){
                s.inventory.decrement(SHIELD);
                s.buffsManager.activateBuff(SHIELD_BUFF, 250);
            }
            if (KeyCode.DIGIT3 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(DAMAGE_BOOST)){
                s.inventory.decrement(DAMAGE_BOOST);
                s.buffsManager.activateBuff(DAMAGE_BUFF, 500);
            }
            if (KeyCode.DIGIT4 == k.getCode() && k.getEventType() == KeyEvent.KEY_RELEASED && s.inventory.hasAtLeastOne(TRIPLE_SHOT)){
                s.inventory.decrement(TRIPLE_SHOT);
                s.buffsManager.activateBuff(TRIPLESHOT_BUFF, 500);
            }
        }

        Vector2d nextMoveTo = new Vector2d(0, 0);
        if (s.wasd.wPressed) nextMoveTo = new Vector2d(nextMoveTo.x, nextMoveTo.y - s.speed);
        if (s.wasd.aPressed) nextMoveTo = new Vector2d(nextMoveTo.x - s.speed, nextMoveTo.y);
        if (s.wasd.sPressed) nextMoveTo = new Vector2d(nextMoveTo.x, nextMoveTo.y + s.speed);
        if (s.wasd.dPressed) nextMoveTo = new Vector2d(nextMoveTo.x + s.speed, nextMoveTo.y);
        nextMoveTo = nextMoveTo.normalize();
        nextLoc = nextLoc.add(nextMoveTo.scale(s.speed));

        int nextShipTimer = s.shipTimer + 1;
        if (nextScore <= 2500) nextShipTimer %= 500;
        else if (nextScore <= 5000) nextShipTimer %= 400;
        else if (nextScore <= 7500) nextShipTimer %= 300;
        else if (nextScore <= 10000) nextShipTimer %= 250;
        else nextShipTimer %= 200;

        nextBulletSet = nextBulletSet.filter(b->b.location.inBox(s.playArea));
        nextBulletSet = nextBulletSet.map(b->b.step(dt));
        if (nextBulletSet.any(b->Vector2d.distance(b.location, s.location) < 6.5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF))) {
            nextBulletSet = new BulletSet();
            nextShips.clear();
            nextHomingMissiles.clear();
            nextScore = 0;
        }
        for (HomingMissile missile : nextHomingMissiles){
            if (Vector2d.distance(missile.location.location, s.location) <= 5 && !s.buffsManager.isActiveBuff(SHIELD_BUFF)){
                nextBulletSet = new BulletSet();
                nextShips.clear();
                nextHomingMissiles.clear();
                nextScore = 0;
                break;
            }
        }

        s.buffsManager.tickTimer();

        if (!paused)
            return new ShooterState(s.playArea, s.enemyShipArea, nextTime, nextLoc, nextSpeed, s.wasd, nextBulletSet, nextScore, Math.max(nextScore, s.maxScore), yourNextBullets, nextShips, nextHomingMissiles, nextRookBombs, (s.bulletTimer + 1) % 10, nextShipTimer, (s.pwTimer + 1) % 1000, s.inventory, s.buffsManager, s.dropsManager,s.animationManager, false);
        else {
            s.paused = true;
            return s;
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
        gfx.drawText(s.score + "", new Vector2d(20, 20));
        gfx.drawText("Max: " + s.maxScore, new Vector2d(100, 20));

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

        s.animationManager.drawAndAdvance(gfx);

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

