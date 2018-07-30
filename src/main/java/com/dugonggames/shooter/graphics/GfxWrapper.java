package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GfxWrapper {

    @Getter
    private GraphicsContext gc;

    public void setColor(Color c) {
        gc.setFill(c);
    }

    public void fillWedge(Vector2d center, double radius, double startAngle, double endAngle) {
        // TODO: fill this in
    }

    public void strokeArc(Vector2d center, double radius, double thickness, double startAngle, double endAngle) {
        gc.setLineWidth(thickness);
        gc.strokeArc(center.x - radius, center.y - radius, radius * 2, radius * 2, startAngle, endAngle, ArcType.OPEN);
    }

    public void fillCircle(Vector2d center, double radius) {
        gc.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }

    public void drawImage(CenteredImage image, Vector2d center) {
        gc.drawImage(image.getImage(), center.x - image.getCenter().x, center.y - image.getCenter().y);
    }
}
