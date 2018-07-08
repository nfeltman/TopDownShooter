package com.dugonggames.shooter.shooter;

import lombok.Value;

import java.util.List;

public class BuffsManager {

    public enum BuffType {
        SPEEDBOOST
    }

    public void tickTimer() {

    }

    @Value
    public static class ActiveBuff {
        final int timeRemaining;
        final BuffType type;
    }

    public List<ActiveBuff> getActiveBuffs(BuffType b, int duration) {
        return null;
    }
}
