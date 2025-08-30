package org.deepak.dto.buttons;

import lombok.Getter;

@Getter
public class ElevatorButton extends Button {
    private final int destinationFloor;
    
    public ElevatorButton(int destinationFloor){
        super();
        this.destinationFloor = destinationFloor;
    }
}
