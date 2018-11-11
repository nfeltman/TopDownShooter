package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.util.Box;
import com.dugonggames.shooter.util.Rotation;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class GfxWrapper {

    @Getter
    private GraphicsContext gc;
    public final double width;
    public final double height;

    public void setColor(Color c) {
        gc.setFill(c);
        gc.setStroke(c);
    }

    public void strokeArc(Vector2d center, double radius, double thickness, double startAngle, double endAngle) {
        gc.setLineWidth(thickness);
        gc.strokeArc(center.x - radius, center.y - radius, radius * 2, radius * 2, startAngle, endAngle, ArcType.OPEN);
    }

    public void fillCircle(Vector2d center, double radius) {
        gc.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }

    public void fillRect(Box rect){
        gc.fillRect(rect.left, rect.top,  rect.right - rect.left, rect.bottom - rect.top);
    }

    public void drawImage(CenteredImage image, Vector2d center) {
        gc.drawImage(image.getImage(), center.x - image.getCenter().x, center.y - image.getCenter().y);
    }

    public void drawRotatedImage(CenteredImage image, Rotation rotation, Vector2d location) {
        gc.save(); // saves the current state on stack, including the current transform
        gc.setTransform(rotation.getX(), -rotation.getY(), rotation.getY(), rotation.getX(),
                location.x,
                location.y);
        gc.drawImage(image.getImage(),-image.getCenter().x, -image.getCenter().y);
        gc.restore(); // back to original state (before rotation)
    }
    public void drawText(String text, Vector2d topLeft){
        gc.fillText(text, topLeft.x, topLeft.y);
    }
}
