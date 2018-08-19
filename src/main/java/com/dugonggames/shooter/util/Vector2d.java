package com.dugonggames.shooter.util;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Vector2d {
    public final double x;
    public final double y;
    private static final double sinpiover8 = 0.382683432365089771728459984030398866761344562485627041433;
    private static final double cospiover8 = 0.923879532511286756128183189396788286822416625863642486115;

    public Vector2d add(Vector2d vector){
        return new Vector2d(x+vector.x, y+vector.y);
    }

    public Vector2d subtract(Vector2d vector){
        return new Vector2d(x-vector.x, y-vector.y);
    }

    public static Vector2d onCircle(double theta){
        return new Vector2d(Math.cos(theta), Math.sin(theta));
    }

    public Vector2d scale (double scalar) {
        return new Vector2d(x * scalar, y * scalar);
    }

    public Vector2d rotate (double theta) { return new Vector2d(x*Math.cos(theta) - y*Math.sin(theta),x*Math.sin(theta) + y*Math.cos(theta)); }

    public static double distance(Vector2d p1, Vector2d p2){
        return Math.sqrt(((p2.x-p1.x)*(p2.x-p1.x)) + ((p2.y-p1.y)*(p2.y-p1.y)));
    }

    public static double distanceSq(Vector2d p1, Vector2d p2){
        return ((p2.x-p1.x)*(p2.x-p1.x)) + ((p2.y-p1.y)*(p2.y-p1.y));
    }

    public double lengthSq(){
        return (x * x) + (y * y);
    }

    public static double dotProduct(Vector2d p1, Vector2d p2){
        return (p1.x * p2.x) + (p1.y * p2.y);
    }

    public double dotProduct(Vector2d other) { return dotProduct(this,other); }

    public Vector2d normalize(){
        return scale(1/Math.sqrt(lengthSq()));
    }

    // TODO: reimplement in terms of Box
    public boolean inBox(Box b){
        return x >= b.left && y >= b.top && x <= b.right && y <= b.bottom;
    }

    // find the nearest point within the box
    public Vector2d clampToBox(Box b) {
       double newX = x;
       double newY = y;
       if (newX > b.right) newX = b.right;
       if (newX < b.left) newX = b.left;
       if (newY > b.bottom) newY = b.bottom;
       if (newY < b.top) newY = b.top;

       return new Vector2d(newX, newY);
    }

    public Vector2d rotatePiOver8(){
        double newX = (x*cospiover8) - (y*sinpiover8);
        double newY = (x*sinpiover8) + (y*cospiover8);
        return new Vector2d(newX, newY);
    }
    public Vector2d rotateNegativePiOver8(){
        double newX = (x*cospiover8) + (y*sinpiover8);
        double newY = (y*cospiover8) - (x*sinpiover8);
        return new Vector2d(newX, newY);
    }

    public double getAngle() {
        return Math.atan(-x/y) * (180.0/Math.PI) + (y > 0 ? 180.0 : 0.0);
    }
}
