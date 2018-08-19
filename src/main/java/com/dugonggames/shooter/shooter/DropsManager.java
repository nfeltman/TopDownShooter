package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Vector2d;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;

import static com.dugonggames.shooter.shooter.DropsManager.DropType.*;

@Getter
public class DropsManager {

    private ArrayList<Drop> currentDrops;

    public enum DropType {
        SPEED_DROP, SHIELD_DROP, DAMAGE_DROP, TRIPLESHOT_DROP}

    @Value
    public static class Drop {
        public final DropType dropType;
        public final Vector2d position;
    }

    public DropsManager(){
        currentDrops = new ArrayList<Drop>();
    }

    public void placeDrop(Drop d) {
        currentDrops.add(d);
    }

    public ArrayList<Drop> pickUpDrops(Vector2d playerLoc) {
        ArrayList<Drop> pickedUp = new ArrayList<>();
        for (int i = 0; i < currentDrops.size(); i++)
            if (Vector2d.distance(currentDrops.get(i).position, playerLoc) < 20) {
                pickedUp.add(currentDrops.remove(i));
                i--;
            }
        return pickedUp;
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
