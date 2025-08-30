package org.deepak.dto.dispatcher;

import org.deepak.dto.ElevatorCar;
import org.deepak.dto.Request;
import org.deepak.enums.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(InternalDispatcher.class);
    public ElevatorCar submitInternalRequest(int destinationFloor, ElevatorCar elevatorCar) {
        if (destinationFloor < 1) {
            throw new IllegalArgumentException("Invalid destination floor: " + destinationFloor);
        }
        
        if (elevatorCar.getCurrentFloorNumber() == destinationFloor) {
            logger.info("Elevator #{} already at floor {}", elevatorCar.getId(), destinationFloor);
            return elevatorCar;
        }
        
        Direction direction = (elevatorCar.getCurrentFloorNumber() < destinationFloor) ? Direction.UP : Direction.DOWN;
        Request request = new Request(destinationFloor, direction, true);
        elevatorCar.getController().addRequest(request);
        
        logger.info("Internal request added: Floor {} for Elevator #{}", destinationFloor, elevatorCar.getId());
        return elevatorCar;
    }
}
