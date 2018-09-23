package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.CenteredImage;
import com.dugonggames.shooter.graphics.GameImages;
import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ImageSequence implements Animation {

    ArrayList<CenteredImage> images;
    double duration;
    public ImageSequence(ArrayList<CenteredImage> images, double duration){
        this.images = images;
        this.duration = duration;
    }

    @Override
    public double getDuration() {
        return duration;
    }

    @Override
    public void drawFrame(GfxWrapper gfx, Vector2d loc, double t) {
        gfx.drawImage(images.get((int) ((t / duration) * images.size())), loc);
    }

    public static ArrayList<CenteredImage> createSequence(Image image, int gridWidth, int gridHeight, int skip){
        ArrayList<CenteredImage> newImages = new ArrayList<>();
        PixelReader reader = image.getPixelReader();

        for (int j = 0; j < gridHeight; j++){
            for (int i = 0; i < gridWidth; i++){
                int imageWidth = (int) (image.getWidth()/gridWidth);
                int imageHeight = (int) (image.getHeight()/gridHeight);
                WritableImage newImage = new WritableImage(reader, imageWidth * i, imageHeight * j, imageWidth, imageHeight);
                newImages.add(new CenteredImage(newImage));
            }
        }

        return newImages;
    }
}
