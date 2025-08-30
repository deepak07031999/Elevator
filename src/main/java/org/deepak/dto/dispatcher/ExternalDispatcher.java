package org.deepak.dto.dispatcher;

import org.deepak.dto.ElevatorCar;
import org.deepak.ElevatorSystem;
import org.deepak.dto.Request;
import org.deepak.enums.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ExternalDispatcher.class);
    public ElevatorCar submitExternalRequest(int destinationFloor, Direction direction) {
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance();
        ElevatorCar elevatorCar = elevatorSystem.selectBestElevatorCar(destinationFloor, direction);
        
        Request request = new Request(destinationFloor, direction, false);
        elevatorCar.getController().addRequest(request);
        
        if (elevatorCar.getCurrentFloorNumber() == destinationFloor) {
            logger.info("Elevator #{} already at floor {}, opening doors", elevatorCar.getId(), destinationFloor);
        } else {
            logger.info("External request assigned to Elevator #{} for floor {}", elevatorCar.getId(), destinationFloor);
        }
        
        return elevatorCar;
    }
}
