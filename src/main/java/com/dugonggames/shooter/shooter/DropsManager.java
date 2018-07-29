package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Vector2d;
import lombok.Value;

import java.util.ArrayList;

public class DropsManager {

    private ArrayList<Drop> currentDrops;

    public enum DropType {
        SPEED_BUFF
        // TODO: fill this in
    }

    @Value
    public static class Drop {
        DropType dropType;
        Vector2d position;
    }

    public void placeDrop(Drop d) {
        // TODO: fill this in
    }

    public ArrayList<Drop> pickUpDrops(Vector2d playerLoc) {
        // TODO: fill this in
        return null;
    }
}
