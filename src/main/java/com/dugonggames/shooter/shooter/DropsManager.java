package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;
import com.dugonggames.shooter.util.Vector2d;

import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;

public class DropsManager{

    private EntitySet<Drop> currentDrops;

    public EntitySet<Drop> getCurrentDrops() {
        return this.currentDrops;
    }

    public enum DropType {
        SPEED_DROP, SHIELD_DROP, DAMAGE_DROP, TRIPLESHOT_DROP}

    public static class Drop {
        public final DropType dropType;
        public final Vector2d position;

        @java.beans.ConstructorProperties({"dropType", "position"})
        public Drop(DropType dropType, Vector2d position) {
            this.dropType = dropType;
            this.position = position;
        }

        public DropType getDropType() {
            return this.dropType;
        }

        public Vector2d getPosition() {
            return this.position;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Drop)) return false;
            final Drop other = (Drop) o;
            final Object this$dropType = this.getDropType();
            final Object other$dropType = other.getDropType();
            if (this$dropType == null ? other$dropType != null : !this$dropType.equals(other$dropType)) return false;
            final Object this$position = this.getPosition();
            final Object other$position = other.getPosition();
            if (this$position == null ? other$position != null : !this$position.equals(other$position)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $dropType = this.getDropType();
            result = result * PRIME + ($dropType == null ? 43 : $dropType.hashCode());
            final Object $position = this.getPosition();
            result = result * PRIME + ($position == null ? 43 : $position.hashCode());
            return result;
        }

        public String toString() {
            return "DropsManager.Drop(dropType=" + this.getDropType() + ", position=" + this.getPosition() + ")";
        }
    }

    public DropsManager(){
        currentDrops = new EntitySet<Drop>();
    }

    public void placeDrop(Drop d) {
        currentDrops.add(d);
    }

    public EntitySet<Drop> pickUpDrops(Vector2d playerLoc) {
        Pair<EntitySet<Drop>, EntitySet<Drop>> split = currentDrops.split(b -> Vector2d.distance(b.position, playerLoc) < 20);
        currentDrops = split.getB();
        return split.getA();
    }

    public static DropsManager.DropType getRandomDrop() {
        DropsManager.DropType random_drop;
        double whichPw = Math.random();
        if (whichPw < 0.25)
            random_drop = SPEED_DROP;
        else if (whichPw < 0.50)
            random_drop = SHIELD_DROP;
        else if (whichPw < 0.75)
            random_drop = DAMAGE_DROP;
        else
            random_drop = TRIPLESHOT_DROP;

        return random_drop;
    }
}
