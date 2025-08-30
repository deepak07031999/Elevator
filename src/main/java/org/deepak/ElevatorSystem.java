package org.deepak;

import org.deepak.dto.Building;
import org.deepak.dto.ElevatorCar;
import org.deepak.dto.ElevatorController;
import org.deepak.enums.Direction;
import org.deepak.enums.ElevatorState;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ElevatorSystem {
    private final List<ElevatorController> controllers = new CopyOnWriteArrayList<>();
    private final Building building;

    private ElevatorSystem() {
        this.building = Building.getInstance();
    }

    public Building getBuilding() {
        return building;
    }

    private static volatile ElevatorSystem system = null;

    public static ElevatorSystem getInstance() {
        if (system == null) {
            synchronized (ElevatorSystem.class) {
                if (system == null) {
                    system = new ElevatorSystem();
                }
            }
        }
        return system;
    }
    public ElevatorCar selectBestElevatorCar(int destinationFloor, Direction direction) {
        ElevatorCar bestElevator = null;
        int bestScore = Integer.MAX_VALUE;
        
        for (ElevatorCar elevator : building.getElevators()) {
            if (elevator.getState() == ElevatorState.OUT_OF_SERVICE || 
                elevator.getState() == ElevatorState.MAINTENANCE ||
                !elevator.hasCapacity()) {
                continue;
            }
            
            int score = calculateElevatorScore(elevator, destinationFloor, direction);
            if (score < bestScore) {
                bestScore = score;
                bestElevator = elevator;
            }
        }
        
        if (bestElevator == null) {
            throw new RuntimeException("No available elevator for request");
        }
        
        return bestElevator;
    }
    
    private int calculateElevatorScore(ElevatorCar elevator, int destinationFloor, Direction direction) {
        int currentFloor = elevator.getCurrentFloorNumber();
        int distance = Math.abs(destinationFloor - currentFloor);
        int score = distance;
        
        // Prefer idle elevators
        if (elevator.getState() == ElevatorState.IDLE) {
            score -= 50;
        }
        
        // Prefer elevators moving in same direction and can serve the request
        if (elevator.getDirection() == direction) {
            if ((direction == Direction.UP && currentFloor <= destinationFloor) ||
                (direction == Direction.DOWN && currentFloor >= destinationFloor)) {
                score -= 30;
            }
        }
        
        // Consider load factor
        score += (elevator.getCurrentLoad() * 5);
        
        return score;
    }

    public void addElevator(ElevatorCar elevator) {
        ElevatorController controller = new ElevatorController(elevator);
        controllers.add(controller);
    }


}
