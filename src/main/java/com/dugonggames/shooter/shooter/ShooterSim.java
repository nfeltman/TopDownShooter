package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Vector2d;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ShooterSim{

    public ShooterState init(int width, int height) {
        return new ShooterState(0, new Vector2d(width/2, height/2), 2, false, false, false, false, new ArrayList<MovingPoint>(), 0, 0, new ArrayList<MovingPoint>(), new ArrayList<Ship>(), 0, 0, 1, 0,  new ArrayList<Vector2d>(), 0, new ArrayList<Vector2d>());
    }


    public ShooterState stepForward(ShooterState s, double dt, ArrayList<KeyEvent> keyPresses, ArrayList<MouseEvent> mouseClicks, int width, int height) {
        Vector2d nextLoc = s.location;
        double nextTime = s.time + dt;
        int nextBulletsDodged = s.bulletsDodged;
        for (KeyEvent k : keyPresses){
            s.wPressed = isPressed(KeyCode.W, k, s.wPressed);
            s.aPressed = isPressed(KeyCode.A, k, s.aPressed);
            s.sPressed = isPressed(KeyCode.S, k, s.sPressed);
            s.dPressed = isPressed(KeyCode.D, k, s.dPressed);
        }
        if (s.wPressed) nextLoc = new Vector2d(nextLoc.x, nextLoc.y - s.speed);
        if (s.aPressed) nextLoc = new Vector2d(nextLoc.x - s.speed, nextLoc.y );
        if (s.sPressed) nextLoc = new Vector2d(nextLoc.x, nextLoc.y + s.speed);
        if (s.dPressed) nextLoc = new Vector2d(nextLoc.x + s.speed, nextLoc.y);

        ArrayList<MovingPoint> nextBullets = new ArrayList();
        for (MovingPoint bullet : s.bullets) {
            if (bullet.location.x >= 0 && bullet.location.x <= width && bullet.location.y >= 0 && bullet.location.y <= height)
                nextBullets.add(bullet.step(dt));
            else {
                nextBulletsDodged++;
            }
            if (Vector2d.distance(bullet.location, s.location) < 6.5 && s.shieldBoostTime == 0) {
                nextBullets.clear();
                nextTime = 0;
                nextBulletsDodged = 0;
                break;
            }
        }


        ArrayList<Ship> nextShips = new ArrayList<>();
        for (Ship ship : s.ships){
            nextShips.add(new Ship(ship.getLocation().step(dt), ship.getHealth()));
        } for (int i = 0; i < nextShips.size(); i++){
            if (nextShips.get(i).getLocation().location.x < 100)
                nextShips.set(i, new Ship(new MovingPoint(nextShips.get(i).getLocation().location, new Vector2d(Math.abs(nextShips.get(i).getLocation().velocity.x), nextShips.get(i).getLocation().velocity.y)), nextShips.get(i).getHealth()));
            if (nextShips.get(i).getLocation().location.x > width - 100)
                nextShips.set(i, new Ship(new MovingPoint(nextShips.get(i).getLocation().location, new Vector2d(-Math.abs(nextShips.get(i).getLocation().velocity.x), nextShips.get(i).getLocation().velocity.y)), nextShips.get(i).getHealth()));
            if (nextShips.get(i).getLocation().location.y < 50)
                nextShips.set(i, new Ship(new MovingPoint(nextShips.get(i).getLocation().location, new Vector2d(nextShips.get(i).getLocation().velocity.x, Math.abs(nextShips.get(i).getLocation().velocity.y))), nextShips.get(i).getHealth()));
            if (nextShips.get(i).getLocation().location.y > height - 50)
                nextShips.set(i, new Ship(new MovingPoint(nextShips.get(i).getLocation().location, new Vector2d(nextShips.get(i).getLocation().velocity.x, -Math.abs(nextShips.get(i).getLocation().velocity.y))), nextShips.get(i).getHealth()));

            if (s.bulletTimer == 0)
                nextBullets.add(makeBullet(nextLoc, nextShips.get(i).getLocation().location));
        } if (s.shipTimer == 0){
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

        ArrayList<MovingPoint> yourNextBullets = new ArrayList();

        for (MouseEvent event : mouseClicks){
            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                Vector2d nextLocation = new Vector2d(s.location.x, s.location.y);
                Vector2d nextVelocity = new Vector2d(event.getX(), event.getY()).subtract(nextLocation);
                nextVelocity = nextVelocity.normalize().scale(1000);
                yourNextBullets.add(new MovingPoint(nextLocation, nextVelocity));
            }
        }

        for (int i = 0; i < yourNextBullets.size(); i++){
            boolean collided = false;
            for (int j = 0; j < nextBullets.size(); j++){
                if (Vector2d.distance(yourNextBullets.get(i).location, nextBullets.get(j).location) < 4)
                    nextBullets.remove(j);
            }
            for (int j = 0; j < nextShips.size(); j++){
                if (Math.abs(yourNextBullets.get(i).location.x - nextShips.get(j).getLocation().location.x) < 100 && Math.abs(yourNextBullets.get(i).location.y - nextShips.get(j).getLocation().location.y) < 50 ){
                    nextShips.set(j,  new Ship(nextShips.get(j).getLocation(), nextShips.get(j).getHealth() - 1));
                    if (nextShips.get(j).getHealth() <= 0){
                        nextShips.remove(j);
                        collided = true;
                    }
                }
            }
            if (yourNextBullets.get(i).location.x >= 0 && yourNextBullets.get(i).location.x <= width && yourNextBullets.get(i).location.y >= 0 && yourNextBullets.get(i).location.y <= height && !collided)
                yourNextBullets.add(yourNextBullets.get(i).step(dt));
        }

        ArrayList<Vector2d> nextSpeedPwLocs = s.speedPwLocs;
        ArrayList<Vector2d> nextShieldPwLocs = s.shieldPwLocs;
        if (s.pwTimer == 0){
            double whichPw = Math.random();
            if (whichPw < 0.5)
                nextShieldPwLocs.add(new Vector2d((Math.random() * (width - 100)) + 50, (Math.random() * (height - 100)) + 50));
            else
                nextSpeedPwLocs.add(new Vector2d((Math.random() * (width - 100)) + 50, (Math.random() * (height - 100)) + 50));
        }

        double nextSpeed = s.speed;
        int nextSpeedBoostTime = Math.max(s.speedBoostTime - 1, 0);
        for (int i = 0; i < nextSpeedPwLocs.size(); i++){
            if (Vector2d.distance(nextSpeedPwLocs.get(i), s.location) < 6.5) {
                nextSpeed *= 2;
                nextSpeedBoostTime = 500;
                nextSpeedPwLocs.remove(i);
            }
        }
        if (nextSpeedBoostTime == 0) nextSpeed = 2;

        int nextShieldBoostTime = Math.max(s.shieldBoostTime - 1, 0);
        for (int i = 0; i < nextShieldPwLocs.size(); i++){
            if (Vector2d.distance(nextShieldPwLocs.get(i), s.location) < 6.5) {
                nextShieldBoostTime = 250;
                nextShieldPwLocs.remove(i);
            }
        }

        return new ShooterState(nextTime, nextLoc, nextSpeed, s.wPressed, s.aPressed, s.sPressed, s.dPressed, nextBullets, nextBulletsDodged, Math.max(nextBulletsDodged, s.maxDodged), yourNextBullets, nextShips, (s.bulletTimer + 1) % 1, (s.shipTimer + 1) % 500, (s.pwTimer + 1) % 1000, nextSpeedBoostTime, nextSpeedPwLocs, nextShieldBoostTime, nextShieldPwLocs);
    }

    private MovingPoint makeBullet(Vector2d target, Vector2d origin){
        Vector2d velocity;
        velocity = target.subtract(origin);
        velocity = velocity.normalize();
        velocity = velocity.scale(300);

        return new MovingPoint(origin, velocity);
    }

    private boolean isPressed(KeyCode code, KeyEvent event, boolean isCurrentlyPressed){
        if (event.getCode().equals(code)){
            if (event.getEventType() == KeyEvent.KEY_PRESSED) return true;
            if (event.getEventType() == KeyEvent.KEY_RELEASED) return false;
        }
        return isCurrentlyPressed;
    }


    public void draw(ShooterState s, GraphicsContext gc, double width, double height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        if (s.shieldBoostTime % 50 > 10){
            gc.setFill(Color.GRAY);
            gc.fillOval(s.location.x - 13, s.location.y - 13, 26, 26);
            gc.setFill(Color.BLACK);
            gc.fillOval(s.location.x - 10, s.location.y - 10, 20, 20);
        }

        gc.setFill(Color.WHITE);
        gc.fillOval(s.location.x - 5, s.location.y - 5, 10, 10);
        gc.setFill(Color.RED);
        for (MovingPoint bullet : s.bullets){
            gc.fillOval(bullet.location.x, bullet.location.y, 3, 3);
        }
        gc.setFill(Color.WHITE);
        for (MovingPoint bullet : s.yourBullets){
            gc.fillOval(bullet.location.x, bullet.location.y, 5, 5);
        }
        gc.setFill(Color.WHITE);
        gc.fillText(s.bulletsDodged + "", 20, 20);
        gc.fillText("Max: " + s.maxDodged, 100, 20);
        gc.setFill(Color.RED);
        for (Ship ship : s.ships){
            double x = ship.getLocation().location.x;
            double y = ship.getLocation().location.y;
            gc.fillRect(x-100, y-50, 200, 100);
        }
        gc.setFill(Color.LIGHTGREEN);
        for (Vector2d speedPw : s.speedPwLocs)
            gc.fillOval(speedPw.x - 3, speedPw.y - 3, 6, 6);

        gc.setFill(Color.GRAY);
        for (Vector2d shieldPw : s.shieldPwLocs)
            gc.fillOval(shieldPw.x - 3, shieldPw.y - 3, 6, 6);
    }
}
