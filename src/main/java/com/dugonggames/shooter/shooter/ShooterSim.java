package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.graphics.animations.AnimationManager;
import com.dugonggames.shooter.graphics.animations.BlinkingCircle;
import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

import static com.dugonggames.shooter.shooter.Inventory.Item.*;
import static com.dugonggames.shooter.shooter.BuffsManager.BuffType.*;
import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;

public class ShooterSim{


    public ShooterState init(int width, int height) {
        GameImages.loadImages();
        return new ShooterState(new Box(0, height, 0, width), new Box(50, height - 50, 100, width - 100),0, new Vector2d(width/2, height/2), 2, new HeldButtonState(), new BulletSet(), 0, 0, new ArrayList<MovingPoint>(), new ArrayList<Ship>(), 0, 0, 1, new Inventory(), new BuffsManager(),  new DropsManager(), new AnimationManager(), false);
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
        for (Ship ship : s.ships){
            MovingPoint nextEnemyLoc = ship.getLocation().step(dt).bounceInsideBox(s.enemyShipArea);
            nextShips.add(new Ship(nextEnemyLoc, ship.getHealth()));

            if (s.bulletTimer == 0)
                nextBulletSet.add(makeBullet(nextLoc, nextEnemyLoc.location));
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
            nextShips.add(new Ship(new MovingPoint(nextShipLocation, nextShipVelocity), 20));
        }

        ArrayList<MovingPoint> yourNextBullets = new ArrayList<>();

        for (MouseEvent event : mouseClicks){
            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
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
            }
        }

        for (int i = 0; i < s.yourBullets.size(); i++){
            boolean collided = false;
            for (int j = 0; j < nextShips.size(); j++){
                if (Vector2d.distance(s.yourBullets.get(i).location, nextShips.get(j).getLocation().location) <= 120){
                    nextShips.set(j,  new Ship(nextShips.get(j).getLocation(), nextShips.get(j).getHealth() - (s.buffsManager.isActiveBuff(DAMAGE_BUFF) ? 2 : 1)));
                    s.animationManager.addAnimation(new BlinkingCircle(), s.yourBullets.get(i).location);
                    if (nextShips.get(j).getHealth() <= 0){
                        nextShips.remove(j);
                        nextScore += 500;
                    }
                    collided = true;
                }
            }
            if (s.yourBullets.get(i).location.inBox(s.playArea) && !collided)
                yourNextBullets.add(s.yourBullets.get(i).step(dt));
        }

        if (s.pwTimer == 0) {
            double whichPw = Math.random();
            if (whichPw < 0.25)
                s.dropsManager.placeDrop(new DropsManager.Drop(SPEED_DROP, s.enemyShipArea.randomPoint()));
            else if (whichPw < 0.50)
                s.dropsManager.placeDrop(new DropsManager.Drop(SHIELD_DROP, s.enemyShipArea.randomPoint()));
            else if (whichPw < 0.75)
                s.dropsManager.placeDrop(new DropsManager.Drop(DAMAGE_DROP, s.enemyShipArea.randomPoint()));
            else
                s.dropsManager.placeDrop(new DropsManager.Drop(TRIPLESHOT_DROP, s.enemyShipArea.randomPoint()));
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

        if (!s.buffsManager.isActiveBuff(SPEED_BUFF)) nextSpeed = 2;

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
        if (s.wasd.wPressed) nextLoc = new Vector2d(nextLoc.x, nextLoc.y - s.speed);
        if (s.wasd.aPressed) nextLoc = new Vector2d(nextLoc.x - s.speed, nextLoc.y );
        if (s.wasd.sPressed) nextLoc = new Vector2d(nextLoc.x, nextLoc.y + s.speed);
        if (s.wasd.dPressed) nextLoc = new Vector2d(nextLoc.x + s.speed, nextLoc.y);

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
            nextScore = 0;
        }

        s.buffsManager.tickTimer();

        if (!paused)
            return new ShooterState(s.playArea, s.enemyShipArea, nextTime, nextLoc, nextSpeed, s.wasd, nextBulletSet, nextScore, Math.max(nextScore, s.maxScore), yourNextBullets, nextShips, (s.bulletTimer + 1) % 1, nextShipTimer, (s.pwTimer + 1) % 1000, s.inventory, s.buffsManager, s.dropsManager,s.animationManager, false);
        else {
            s.paused = true;
            return s;
        }
    }

    private MovingPoint makeBullet(Vector2d target, Vector2d origin){
        Vector2d velocity;
        velocity = target.subtract(origin);
        velocity = velocity.normalize();
        velocity = velocity.scale(300);

        return new MovingPoint(origin, velocity);
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
            gfx.fillRect(new Box(width/2 - 100, width/2 - 50, height/2 - 150, height/2 + 150));
            gfx.fillRect(new Box(width/2 + 50,width/2 + 100,height/2 - 150, height/2 + 150));
        }
    }
}
