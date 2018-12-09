package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.image.Image;

public class CenteredImage {
    private final Vector2d center;
    private final Image image;

    public CenteredImage(Image im) {
        this(new Vector2d(im.getWidth()/2, im.getHeight()/2), im);
    }

    @java.beans.ConstructorProperties({"center", "image"})
    public CenteredImage(Vector2d center, Image image) {
        this.center = center;
        this.image = image;
    }

    public Vector2d getCenter() {
        return this.center;
    }

    public Image getImage() {
        return this.image;
    }
}
