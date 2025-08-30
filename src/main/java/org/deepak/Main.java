package org.deepak;

import org.deepak.dto.Building;
import org.deepak.dto.ElevatorCar;

import org.deepak.enums.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance();
        Building building = elevatorSystem.getBuilding();
        ElevatorCar inputElevatorCar1 = new ElevatorCar();
        logger.info("Created elevator with ID: {}", inputElevatorCar1.getId());
        ElevatorCar inputElevatorCar2 = new ElevatorCar();
        logger.info("Created elevator with ID: {}", inputElevatorCar2.getId());
        building.setElevators(Arrays.asList(inputElevatorCar1,inputElevatorCar2));
        building.setFloor(4);
        
        inputElevatorCar1.setFloorPanel(4);
        inputElevatorCar2.setFloorPanel(4);
        
        // Register elevators with system
        elevatorSystem.addElevator(inputElevatorCar1);
        elevatorSystem.addElevator(inputElevatorCar2);


        logger.info("=== Testing Elevator System ===");
        logger.info("1st request: Floor 2 calling UP");
        ElevatorCar elevator1 = building.getFloor(2).getPanel().pressButton(Direction.UP);
        if (elevator1 != null) {
            logger.info("Inside elevator, pressing floor 4 button");
            elevator1.pressFloorButton(4);
        }
        logger.info("2nd request: Floor 1 calling UP");
        ElevatorCar elevator2 = building.getFloor(1).getPanel().pressButton(Direction.UP);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("=== System Test Complete ===");
    }
}