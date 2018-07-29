package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Vector2d;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;

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
            if (Vector2d.distance(currentDrops.get(i).position, playerLoc) < 10) {
                pickedUp.add(currentDrops.remove(i));
                i--;
            }
        return pickedUp;
    }
}
