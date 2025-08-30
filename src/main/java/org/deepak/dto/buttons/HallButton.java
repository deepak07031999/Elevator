package org.deepak.dto.buttons;

import lombok.Getter;
import org.deepak.enums.Direction;

@Getter
public class HallButton extends Button{
    private final Direction buttonSign;
    
    public HallButton(Direction buttonSign){
        super();
        this.buttonSign = buttonSign;
    }
}
