package org.deepak.dto.buttons;

import lombok.Getter;

@Getter
public abstract class Button {
    private volatile boolean pressed;
    
    public Button(){
        pressed = false;
    }
    
    public synchronized void pressDown(){
        pressed = true;
    }
    
    public synchronized void reset(){
        pressed = false;
    }
}
