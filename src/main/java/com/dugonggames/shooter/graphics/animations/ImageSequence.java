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
    public ImageSequence(ArrayList<CenteredImage> images){
        this.images = images;
    }

    @Override
    public int getNumFrames() {
        return images.size() - 1;
    }

    @Override
    public void drawFrame(GfxWrapper gfx, Vector2d loc, int frame) {
        gfx.drawImage(images.get((frame)), loc);
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
