package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;
import com.dugonggames.shooter.util.Vector2d;
import lombok.Getter;
import lombok.Value;

import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;

@Getter
public class DropsManager{

    private EntitySet<Drop> currentDrops;

    public enum DropType {
        SPEED_DROP, SHIELD_DROP, DAMAGE_DROP, TRIPLESHOT_DROP}

    @Value
    public static class Drop {
        public final DropType dropType;
        public final Vector2d position;
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
