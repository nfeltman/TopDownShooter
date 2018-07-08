package com.dugonggames.shooter.shooter;

import lombok.Value;

import java.util.List;

public class BuffsManager {

    public enum BuffType {SPEED_BUFF, SHIELD_BUFF, DAMAGE_BUFF, TRIPLESHOT_BUFF}
    private int[] ticksLeft;

    public BuffsManager(){
        ticksLeft = new int[4];
    }

    public void tickTimer() {
        for (int i = 0; i < ticksLeft.length; i++){
            ticksLeft[i] = Math.max(ticksLeft[i] - 1, 0);
        }
    }

    public void activateBuff(BuffType b, int duration) {
        ticksLeft[b.ordinal()] = duration;
    }

    public boolean isActiveBuff(BuffType b){
        return ticksLeft[b.ordinal()] > 0;
    }

    public int buffTimeLeft(BuffType b){
        return ticksLeft[b.ordinal()];
    }
}
