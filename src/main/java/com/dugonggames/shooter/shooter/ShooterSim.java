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
        Vector2d firstShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
        while (((firstShipLocation.x > (width/2)+200) || (firstShipLocation.x < (width/2) - 100)) && ((firstShipLocation.y > (height/2)+200) || (firstShipLocation.y < (height/2) - 100))){
            firstShipLocation = new Vector2d(Math.random()*width, Math.random()*height);
        }
        Vector2d firstShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
        while (Math.abs(firstShipVelocity.x) < 100 && Math.abs(firstShipVelocity.x) < 66){
            firstShipVelocity = new Vector2d(((Math.random()-0.5)*200), ((Math.random()-0.5)*200));
        }
        ArrayList<Ship> ships = new ArrayList<>();
        ships.add(new Ship(new MovingPoint(firstShipLocation, firstShipVelocity), 20));
        return new ShooterState(0, new Vector2d(width/2, height/2), 2, false, false, false, false, new ArrayList<MovingPoint>(), 0, 0, new ArrayList<MovingPoint>(), ships);
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
            if (Vector2d.distance(bullet.location, s.location) < 6.5) {
                nextBullets.clear();
                nextTime = 0;
                nextBulletsDodged = 0;
                break;
            }
        }

        if (Math.random() < 0.01 * s.time){
            nextBullets.add(makeBullet(50, 100, width, height));
        }

        ArrayList<MovingPoint> yourNextBullets = new ArrayList();
        for (MovingPoint bullet : s.yourBullets){
            if (bullet.location.x >= 0 && bullet.location.x <= width && bullet.location.y >= 0 && bullet.location.y <= height)
                yourNextBullets.add(bullet.step(dt));
            for (int i = 0; i < nextBullets.size(); i++){
                if (Vector2d.distance(bullet.location, nextBullets.get(i).location) < 4)
                    nextBullets.remove(i);
            }
        }

        for (MouseEvent event : mouseClicks){
            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                Vector2d nextLocation = new Vector2d(s.location.x, s.location.y);
                Vector2d nextVelocity = new Vector2d(event.getX(), event.getY()).subtract(nextLocation);
                nextVelocity = nextVelocity.normalize().scale(1000);
                yourNextBullets.add(new MovingPoint(nextLocation, nextVelocity));
            }
        }

        ArrayList<Ship> newShips = new ArrayList<>();
        for (Ship ship : s.ships){
            newShips.add(new Ship(ship.getLocation().step(dt), ship.getHealth()));
        }
        return new ShooterState(nextTime, nextLoc, s.speed, s.wPressed, s.aPressed, s.sPressed, s.dPressed, nextBullets, nextBulletsDodged, Math.max(nextBulletsDodged, s.maxDodged), yourNextBullets, newShips);
    }

    private MovingPoint makeBullet(double velRange, double velLower, int width, int height){
        Vector2d nextLocation;
        Vector2d nextVelocity;
        double rand = Math.random();
        if (rand < 0.25){
            nextLocation = new Vector2d(0, (int) (Math.random()*height)); // left
        } else if (rand < 0.5){
            nextLocation = new Vector2d(width, (int) (Math.random()*height)); // right
        } else if (rand < 0.75){
            nextLocation = new Vector2d((int) (Math.random()*width), 0); // top
        } else {
            nextLocation = new Vector2d((int) (Math.random()*width), height); // bottom
        }

        int xPoint = (width / 2) + ((int) ((Math.random()-0.5)*400));
        int yPoint = (height / 2) + ((int) ((Math.random()-0.5)*400));
        Vector2d target = new Vector2d(xPoint, yPoint);

        nextVelocity = target.subtract(nextLocation);
        nextVelocity = nextVelocity.normalize();
        nextVelocity = nextVelocity.scale((Math.random() * velRange) + velLower);

        return new MovingPoint(nextLocation, nextVelocity);
    }

    private boolean isPressed(KeyCode code, KeyEvent event, boolean isCurrentlyPressed){
        if (event.getCode().equals(code)){
            if (event.getEventType() == KeyEvent.KEY_PRESSED) return true;
            if (event.getEventType() == KeyEvent.KEY_RELEASED) return false;
        }
        return isCurrentlyPressed;
    }


    public void draw(ShooterState s, GraphicsContext gc, double width, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.BLACK);
        gc.fillOval(s.location.x, s.location.y, 10, 10);
        gc.setFill(Color.RED);
        for (MovingPoint bullet : s.bullets){
            gc.fillOval(bullet.location.x, bullet.location.y, 3, 3);
        }
        gc.setFill(Color.BLACK);
        for (MovingPoint bullet : s.yourBullets){
            gc.fillOval(bullet.location.x, bullet.location.y, 5, 5);
        }
        gc.setFill(Color.BLACK);
        gc.fillText(s.bulletsDodged + "", 20, 20);
        gc.fillText("Max: " + s.maxDodged, 100, 20);
        gc.setFill(Color.RED);
        for (Ship ship : s.ships){
            double x = ship.getLocation().location.x;
            double y = ship.getLocation().location.y;
            gc.fillRect(x-100, y-50, 200, 100);
        }
    }
}
