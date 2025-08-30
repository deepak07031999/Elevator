package org.deepak.dto.panels;

import org.deepak.dto.ElevatorCar;
import org.deepak.dto.Floor;
import org.deepak.dto.buttons.DoorButton;
import org.deepak.dto.buttons.ElevatorButton;
import org.deepak.dto.dispatcher.InternalDispatcher;
import org.deepak.enums.Direction;
import org.deepak.enums.DoorState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElevatorPanel {
    private final List<ElevatorButton> floorButtons;
    private final DoorButton openButton;
    private final DoorButton closeButton;

    public ElevatorPanel(){
        floorButtons = new ArrayList<>();
        this.openButton = new DoorButton(DoorState.OPEN);
        this.closeButton = new DoorButton(DoorState.CLOSE);
    }

    public void setFloorButtons(int noOfFloors){
        if (noOfFloors <= 0) {
            throw new IllegalArgumentException("Number of floors must be positive: " + noOfFloors);
        }
        for(int i=0;i<noOfFloors;i++)this.floorButtons.add(new ElevatorButton(i+1));
    }

    public List<ElevatorButton> getFloorButtons() {
        return Collections.unmodifiableList(floorButtons);
    }
}
