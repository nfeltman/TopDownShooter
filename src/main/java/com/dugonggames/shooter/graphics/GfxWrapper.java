package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GfxWrapper {

    @Getter
    private GraphicsContext gc;

    public void setColor(Color c) {
        // TODO: fill this in
    }

    public void fillWedge(Vector2d center, double radius, double startAngle, double endAngle) {
        // TODO: fill this in
    }

    public void fillArc(Vector2d center, double radius, double thickness, double startAngle, double endAngle) {
        // TODO: fill this in
    }

    public void fillCircle(Vector2d center, double radius) {
        // TODO: fill this in
    }

    public void drawImage(Vector2d image, double center) {
        // TODO: fill this in
    }
}
