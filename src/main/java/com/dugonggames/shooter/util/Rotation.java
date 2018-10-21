package com.dugonggames.shooter.util;

public class Rotation {

    // always length 1
    private Vector2d p;

    public Rotation(double angleInDegrees) {

    }

    // adds the two rotations togetehr
    public Rotation compose(final Rotation r) {
        throw new RuntimeException("Not implemented");
    }

    // rotates the point around the origin
    public Vector2d rotate(final Vector2d p) {
        throw new RuntimeException("Not implemented");
    }

    // rotates the point around anotehr point
    public Vector2d rotate(final Vector2d p, final Vector2d center) {
        throw new RuntimeException("Not implemented");
    }
}
