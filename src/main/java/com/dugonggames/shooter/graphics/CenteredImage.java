package com.dugonggames.shooter.graphics;

import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CenteredImage {
    private final Vector2d center;
    private final Image image;

    public CenteredImage(Image im) {
        this(new Vector2d(im.getWidth()/2, im.getHeight()/2), im);
    }
}
