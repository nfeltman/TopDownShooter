package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.graphics.animations.GfxUtils;
import com.dugonggames.shooter.util.MovingPoint;
import com.dugonggames.shooter.util.Rotation;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.paint.Color;

public class Ship {
    MovingPoint location;
    int health;
    double missileTimer;

    @java.beans.ConstructorProperties({"location", "health", "missileTimer"})
    public Ship(MovingPoint location, int health, double missileTimer) {
        this.location = location;
        this.health = health;
        this.missileTimer = missileTimer;
    }

    public void draw(GfxWrapper gfx, Vector2d target){
        gfx.setColor(Color.LIGHTBLUE);
        gfx.strokeArc(location.location, 120, 5, 0, 360);

        Rotation angle = Rotation.fromVectors(new Vector2d(0, 1), location.location.subtract(target));
        gfx.drawRotatedImage(GameImages.enemyShip, angle, location.location);

        GfxUtils.healthBar(gfx, health, 20.0, location.location, 10);
    }

    public MovingPoint getLocation() {
        return this.location;
    }

    public int getHealth() {
        return this.health;
    }

    public double getMissileTimer() {
        return this.missileTimer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Ship)) return false;
        final Ship other = (Ship) o;
        final Object this$location = this.getLocation();
        final Object other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) return false;
        if (this.getHealth() != other.getHealth()) return false;
        if (Double.compare(this.getMissileTimer(), other.getMissileTimer()) != 0) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $location = this.getLocation();
        result = result * PRIME + ($location == null ? 43 : $location.hashCode());
        result = result * PRIME + this.getHealth();
        final long $missileTimer = Double.doubleToLongBits(this.getMissileTimer());
        result = result * PRIME + (int) ($missileTimer >>> 32 ^ $missileTimer);
        return result;
    }

    public String toString() {
        return "Ship(location=" + this.getLocation() + ", health=" + this.getHealth() + ", missileTimer=" + this.getMissileTimer() + ")";
    }
}
