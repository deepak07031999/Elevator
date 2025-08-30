package org.deepak.dto;

import lombok.Setter;
import org.deepak.enums.DoorState;

@Setter
public class Door {
    private DoorState state;
    
    public Door(){
        this.state = DoorState.CLOSE;
    }

    public boolean isOpen() {
        return state == DoorState.OPEN;
    }
}
