package com.dugonggames.shooter.util;

public class Rotation {

    // always length 1
    private Vector2d p;

    public Rotation(double angleInDegrees) {
        p = new Vector2d(Math.cos(angleInDegrees), Math.sin(angleInDegrees));
    }

    private Rotation(double x, double y){
        p = new Vector2d(x, y);
    }
    // adds the two rotations together

    public Rotation compose(final Rotation r) {
        return new Rotation((p.x * r.p.x) - (p.y * r.p.y), (p.x * r.p.y) + (p.y * r.p.x));
    }
    /*
    a + bi * c + di
    ac + adi + bci - bd
     */

    // rotates the point around the origin
    public Vector2d rotate(final Vector2d p) {
        return new Vector2d((p.x * this.p.x) - (p.y * this.p.y), (p.x * this.p.y) + (p.y * this.p.x));
    }

    // rotates the point around another point
    public Vector2d rotate(final Vector2d p, final Vector2d center) {
        return rotate(p.subtract(center)).add(center);
    }
}
