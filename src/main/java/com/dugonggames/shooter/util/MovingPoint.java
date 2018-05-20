package com.dugonggames.shooter.util;

public class MovingPoint {
    public final Vector2d location;
    public final Vector2d velocity;

    public MovingPoint(Vector2d location, Vector2d velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    public MovingPoint subtract(MovingPoint p){
        return new MovingPoint(location.subtract(p.location), velocity.subtract(p.velocity));
    }

    // step a timestep
    // assumes no external forces on the
    public MovingPoint step(double deltaT) {
        return new MovingPoint(location.add((velocity.scale(deltaT))), velocity);
    }

    // step a timestep, with an external force
    // uses a linear approximation
    public MovingPoint stepForceApprox(double deltaT, Vector2d externalForces) {
        return new MovingPoint(location.add(velocity.scale(deltaT)), velocity.add(externalForces.scale(deltaT)));
    }

    // step a certain timestep with a constant external force
    public MovingPoint stepForceExact(double deltaT, Vector2d externalForces) {
        return new MovingPoint(location.add(velocity.scale(deltaT)).add(externalForces.scale(deltaT*deltaT/2)),
                velocity.add(externalForces.scale(deltaT)));
    }

    public static Vector2d elasticCollision(MovingPoint p1, MovingPoint p2, double m1, double m2){
        Vector2d x1 = p1.location;
        Vector2d x2 = p2.location;
        Vector2d v1 = p1.velocity;
        Vector2d v2 = p2.velocity;

        Vector2d result = v1;
        Vector2d secondTerm = x1.subtract(x2);
        secondTerm = secondTerm.scale(Vector2d.dotProduct(v1.subtract(v2), x1.subtract(x2))/x2.subtract(x1).lengthSq());
        secondTerm = secondTerm.scale((2*m2)/(m1+m2));
        return result.subtract(secondTerm);
    }
}
