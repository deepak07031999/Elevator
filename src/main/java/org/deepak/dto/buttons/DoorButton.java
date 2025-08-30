package org.deepak.dto.buttons;

import lombok.Getter;
import org.deepak.enums.DoorState;

@Getter
public class DoorButton extends Button {
    private final DoorState operation;
    
    public DoorButton(DoorState operation){
        this.operation = operation;
    }
}
