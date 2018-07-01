package com.dugonggames.shooter.util;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Vector2d {
    public final double x;
    public final double y;

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
    public boolean inBox(double width, double height){
        return x >= 0 && y >= 0 && x <= width && y <= height;
    }

    // find the nearest point within the box
    public Vector2d clampToBox(Box b) {
        // TODO: implement this
        throw new RuntimeException("Not implemented.");
    }
    public static Vector2d getPointOnCircle(double degress, double radius, double x, double y) {
        double rads = Math.toRadians(degress);
        double xPosy = (x + Math.cos(rads) * radius);
        double yPosy = (y + Math.sin(rads) * radius);
        return new Vector2d(xPosy, yPosy);
    }
}
