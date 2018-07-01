package com.dugonggames.shooter.shooter;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HeldButtonState {
    boolean wPressed;
    boolean aPressed;
    boolean sPressed;
    boolean dPressed;

    public void updateWithEvent(KeyEvent k) {
        wPressed = isPressed(KeyCode.W, k, wPressed);
        aPressed = isPressed(KeyCode.A, k, aPressed);
        sPressed = isPressed(KeyCode.S, k, sPressed);
        dPressed = isPressed(KeyCode.D, k, dPressed);
    }

    private boolean isPressed(KeyCode code, KeyEvent event, boolean isCurrentlyPressed){
        if (event.getCode().equals(code)){
            if (event.getEventType() == KeyEvent.KEY_PRESSED) return true;
            if (event.getEventType() == KeyEvent.KEY_RELEASED) return false;
        }
        return isCurrentlyPressed;
    }
}
