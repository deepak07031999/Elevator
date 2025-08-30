package org.deepak.dto.panels;

import org.deepak.dto.ElevatorCar;
import org.deepak.ElevatorSystem;
import org.deepak.dto.buttons.HallButton;
import org.deepak.dto.dispatcher.ExternalDispatcher;
import org.deepak.enums.Direction;

public class HallPanel {
    private final HallButton up;
    private final HallButton down;
    private final ElevatorSystem elevatorSystem;
    private final int floorNumber;
    private final ExternalDispatcher externalDispatcher;

    public HallPanel(int floorNumber) {
        up = new HallButton(Direction.UP);
        down = new HallButton(Direction.DOWN);
        this.floorNumber = floorNumber;
        elevatorSystem = ElevatorSystem.getInstance();
        this.externalDispatcher = new ExternalDispatcher();
    }
    public ElevatorCar pressButton(Direction direction){
        if(direction == Direction.UP){
            this.up.pressDown();
            return externalDispatcher.submitExternalRequest(floorNumber, Direction.UP);
        }
        else{
            this.down.pressDown();
            return externalDispatcher.submitExternalRequest(floorNumber, Direction.DOWN);
        }
    }
}
