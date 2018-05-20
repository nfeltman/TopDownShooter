package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Ship {
    MovingPoint location;
    int health;
}
