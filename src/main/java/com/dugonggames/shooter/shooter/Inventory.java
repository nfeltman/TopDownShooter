package com.dugonggames.shooter.shooter;

public class Inventory {

    public enum Item {SPEED_BOOST, SHIELD, DAMAGE_BOOST, TRIPLE_SHOT}
    private int[] itemCount;

    public Inventory(){
        itemCount = new int[4];
    }

    public boolean hasAtLeastOne(Item i) {
        return itemCount[i.ordinal()] >= 1;
    }

    public int getCount(Item i) {
        return itemCount[i.ordinal()];
    }

    public void increment(Item i) {
        itemCount[i.ordinal()]++;
    }

    public void decrement(Item i) {
        itemCount[i.ordinal()]--;
    }
}
