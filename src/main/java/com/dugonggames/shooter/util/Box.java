package com.dugonggames.shooter.util;

public class Box {
    public final double top, bottom, left, right;

    public Box(double top, double bottom, double left, double right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;

        if(left > right)
            throw new IllegalArgumentException("left ("+left+") should be less than right ("+right+")");
        if (top > bottom)
            throw new IllegalArgumentException("top ("+top+") should be less than bottom ("+bottom+")");
    }

    public Vector2d randomPoint() {
        double x = left + (Math.random() * (right - left));
        double y = top + (Math.random() * (bottom - top));
        return new Vector2d(x, y);
    }
}
