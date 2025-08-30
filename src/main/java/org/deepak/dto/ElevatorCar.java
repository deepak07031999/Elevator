package org.deepak.dto;

import lombok.Getter;
import lombok.Setter;
import org.deepak.dto.dispatcher.InternalDispatcher;
import org.deepak.dto.panels.ElevatorPanel;
import org.deepak.enums.Direction;
import org.deepak.enums.ElevatorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ElevatorCar {
    private static final Logger logger = LoggerFactory.getLogger(ElevatorCar.class);
    private final int id;
    private volatile int currentFloorNumber;
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private final Display display;
    private final ElevatorPanel elevatorPanel;
    @Getter
    @Setter
    private volatile Direction direction;
    private final Door door;
    private volatile ElevatorState state;
    private final InternalDispatcher internalDispatcher;
    private final int capacity;
    private volatile int currentLoad;
    private final Object lock = new Object();
    private ElevatorController controller;

    public ElevatorCar() {
        this.elevatorPanel = new ElevatorPanel();
        this.door = new Door();
        this.id = atomicInteger.incrementAndGet();
        this.currentFloorNumber = 0;
        this.display = new Display(this.id);
        this.internalDispatcher = new InternalDispatcher();
        this.state = ElevatorState.IDLE;
        this.capacity = 10;
        this.currentLoad = 0;
    }

    public void setController(ElevatorController controller) {
        this.controller = controller;
        controller.addObserver(display);
    }

    public void moveToFloor(int destinationFloor) {
        if (currentFloorNumber == destinationFloor) {
            logger.info("Elevator #{} already at floor {}", id, destinationFloor);
            return;
        }

        state = ElevatorState.MOVING;
        direction = (currentFloorNumber < destinationFloor) ? Direction.UP : Direction.DOWN;
        controller.onElevatorStateChanged(this, ElevatorState.MOVING);
        
        ScheduledExecutorService moveExecutor = new ScheduledThreadPoolExecutor(1, 
            new ThreadPoolExecutor.AbortPolicy());
        moveExecutor.scheduleAtFixedRate(() -> {
            if (currentFloorNumber != destinationFloor) {
                currentFloorNumber += (direction == Direction.UP) ? 1 : -1;
                controller.onFloorChanged(this, currentFloorNumber);
            } else {
                logger.info("Elevator #{} arrived at floor {}", id, destinationFloor);
                state = ElevatorState.IDLE;
                controller.onElevatorStateChanged(this, ElevatorState.IDLE);
                controller.onRequestCompleted(this, destinationFloor);
                moveExecutor.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public boolean hasCapacity() { return currentLoad < capacity; }
    
    public void setFloorPanel(int noOfFloors) {
        this.elevatorPanel.setFloorButtons(noOfFloors);
    }

    public ElevatorCar pressFloorButton(int destination) {
        elevatorPanel.getFloorButtons().get(destination-1).pressDown();
        return internalDispatcher.submitInternalRequest(destination, this);
    }

}
