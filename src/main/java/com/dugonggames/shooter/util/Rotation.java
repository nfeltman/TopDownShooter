package com.dugonggames.shooter.util;

import lombok.Getter;

@Getter
public class Rotation {

    // always length 1
    private double x;
    private double y;

    private Rotation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Rotation fromCounterClockwiseAngle(double angleInDegrees) {
        double angleInRadians = angleInDegrees / 180 * Math.PI;
        return new Rotation(Math.cos(angleInRadians), Math.sin(angleInRadians));
    }

    public static Rotation fromVectors(final Vector2d from, final Vector2d to) {
        Vector2d a = from.normalize();
        Vector2d b = to.normalize();
        return new Rotation(a.x, a.y).compose(new Rotation(b.x, b.y).negate());
    }

    // gets the rotation corresponding to (abc)
    public static Rotation fromPoints(final Vector2d a, final Vector2d b, final Vector2d c) {
        return fromVectors(a.subtract(b), c.subtract(b));
    }

    public Rotation negate() {
        return new Rotation(x, -y);
    }

    public Rotation clockwise90() {
        return new Rotation(-y, x);
    }

    // adds the two rotations together
    public Rotation compose(final Rotation r) {
        return new Rotation((x * r.x) - (y * r.y), (x * r.y) + (y * r.x));
    }

    // rotates the point around the origin
    public Vector2d rotate(final Vector2d p) {
        return new Vector2d((p.x * x) - (p.y * y), (p.x * y) + (p.y * x));
    }

    // rotates the point around another point
    public Vector2d rotate(final Vector2d p, final Vector2d center) {
        return rotate(p.subtract(center)).add(center);
    }
}
