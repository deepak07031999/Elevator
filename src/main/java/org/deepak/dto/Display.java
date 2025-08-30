package org.deepak.dto;

import lombok.Getter;
import lombok.Setter;
import org.deepak.enums.Direction;
import org.deepak.enums.ElevatorState;
import org.deepak.interfaces.ElevatorObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class Display implements ElevatorObserver {
    private static final Logger logger = LoggerFactory.getLogger(Display.class);
    private int capacity;
    private Direction direction;
    private int floorNumber;
    private final int elevatorId;
    
    public Display(int elevatorId){
        this.capacity = 0;
        this.floorNumber = 0;
        this.direction = Direction.DOWN;
        this.elevatorId = elevatorId;
    }

    @Override
    public void onElevatorStateChanged(ElevatorCar elevator, ElevatorState newState) {
        if (elevator.getId() == elevatorId) {
            logger.info("Display #{}: Elevator state changed to {}", elevatorId, newState);
        }
    }

    @Override
    public void onFloorChanged(ElevatorCar elevator, int newFloor) {
        if (elevator.getId() == elevatorId) {
            this.floorNumber = newFloor;
            this.direction = elevator.getDirection();
            showDisplay();
        }
    }

    @Override
    public void onRequestCompleted(ElevatorCar elevator, int floor) {
        if (elevator.getId() == elevatorId) {
            logger.info("Display #{}: Request completed for floor {}", elevatorId, floor);
        }
    }

    private void showDisplay(){
        logger.info("Display #{}: Floor {} | Direction {}", elevatorId, floorNumber, direction);
    }
}
