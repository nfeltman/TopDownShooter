package com.dugonggames.shooter.graphics.animations;

import com.dugonggames.shooter.graphics.GfxWrapper;
import com.dugonggames.shooter.util.Vector2d;

import java.util.ArrayList;

public class AnimationManager {

    private static class AnimatedMoment{
        private Animation animation;
        private int frame;
        private Vector2d location;

        private AnimatedMoment(Animation animation, Vector2d location){
            this.animation = animation;
            this.frame = 0;
            this.location = location
        }

        private void increment(){
            frame++;
        }
    }

    ArrayList<AnimatedMoment> activeAnimations;

    public AnimationManager(){
        activeAnimations = new ArrayList<AnimatedMoment>;
    }

    public void addAnimation(Animation animation, Vector2d location) {
        activeAnimations.add(new AnimatedMoment(animation, location));
    }

    // draws all animations, and advances by a frame
    public void drawAndAdvance(GfxWrapper gfx) {
        for (int i = 0; i < activeAnimations.size(); i++){
            activeAnimations.get(i).animation.drawFrame(gfx, activeAnimations.get(i).location, activeAnimations.get(i).frame);
            activeAnimations.get(i).increment();
            if (activeAnimations.get(i).frame > activeAnimations.get(i).animation.getNumFrames())
                activeAnimations.remove(i);
        }
    }
}
