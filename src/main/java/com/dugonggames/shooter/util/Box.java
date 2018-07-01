package com.dugonggames.shooter.util;

import lombok.AllArgsConstructor;

public class Box {
    public final double top, bottom, left, right;

    public Box(double top, double bottom, double left, double right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;

        if(left > right)
            throw new IllegalArgumentException("left ("+left+") should be less than right ("+right+")");

        // TODO implement top/bottom check
        throw new RuntimeException("Not implemented.");
    }

    public Vector2d getRandomPointInside() {
        // TODO: implment this
        throw new RuntimeException("Not implemented.");
    }

    public Box plus(Box b) {
        return new Box(top + b.top, bottom + b.bottom, left + b.left, right + b.right);
    }

    public Box minus(Box b) {
        return new Box(top - b.top, bottom - b.bottom, left - b.left, right - b.right);
    }
}
