package org.deepak.dto;

import lombok.Getter;
import lombok.Setter;
import org.deepak.enums.Direction;
import org.deepak.interfaces.ElevatorObserver;
import org.deepak.enums.ElevatorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

@Getter
@Setter
public class ElevatorController implements ElevatorObserver {
    private static final Logger logger = LoggerFactory.getLogger(ElevatorController.class);
    private final List<ElevatorObserver> observers = new CopyOnWriteArrayList<>();
    private final ElevatorCar elevator;
    private final PriorityQueue<Integer> upRequests; // MinHeap for UP direction
    private final PriorityQueue<Integer> downRequests; // MaxHeap for DOWN direction
    private final Object lock = new Object();

    public ElevatorController(ElevatorCar elevator) {
        this.elevator = elevator;
        this.upRequests = new PriorityQueue<>(); // MinHeap - closest floor first
        this.downRequests = new PriorityQueue<>(Collections.reverseOrder()); // MaxHeap - highest floor first
        elevator.setController(this);
    }

    public void addRequest(Request request) {
        ElevatorState state = elevator.getState();
        int currentFloorNumber = elevator.getCurrentFloorNumber();
        synchronized (lock) {
            if (state == ElevatorState.OUT_OF_SERVICE || state == ElevatorState.MAINTENANCE) {
                return;
            }

            int floor = request.getFloor();
            if (floor > currentFloorNumber || (floor == currentFloorNumber && request.getDirection() == Direction.UP)) {
                upRequests.offer(floor);
            } else {
                downRequests.offer(floor);
            }
            lock.notify();
        }
        processRequests();
    }

    public void processRequests() {
        synchronized (lock) {
            if (elevator.getState() != ElevatorState.IDLE || (upRequests.isEmpty() && downRequests.isEmpty())) return;

            Integer nextFloor = getNextFloor();
            if (nextFloor != null) {
                moveToFloor(nextFloor);
            }
        }
    }
    private void moveToFloor(int destinationFloor) {
        elevator.moveToFloor(destinationFloor);
    }

    private Integer getNextFloor() {
        Direction direction = elevator.getDirection();
        // SCAN algorithm: continue in current direction, then switch
        if (direction == Direction.UP || direction == null) {
            if (!upRequests.isEmpty()) {
                return upRequests.poll();
            }
            if (!downRequests.isEmpty()) {
                elevator.setDirection(Direction.DOWN);
                return downRequests.poll();
            }
        } else { // direction == Direction.DOWN
            if (!downRequests.isEmpty()) {
                return downRequests.poll();
            }
            if (!upRequests.isEmpty()) {
                elevator.setDirection(Direction.UP);
                return upRequests.poll();
            }
        }
        return null;
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ElevatorObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void onElevatorStateChanged(ElevatorCar elevator, ElevatorState newState) {
        observers.forEach(obs -> {
            try {
                obs.onElevatorStateChanged(elevator, newState);
            } catch (Exception e) {
                logger.error("Error notifying observer: {}", e.getMessage());
            }
        });
    }

    @Override
    public void onFloorChanged(ElevatorCar elevator, int newFloor) {
        observers.forEach(obs -> {
            try {
                obs.onFloorChanged(elevator, newFloor);
            } catch (Exception e) {
                logger.error("Error notifying observer: {}", e.getMessage());
            }
        });
    }

    @Override
    public void onRequestCompleted(ElevatorCar elevator, int floor) {
        observers.forEach(obs -> {
            try {
                obs.onRequestCompleted(elevator, floor);
            } catch (Exception e) {
                logger.error("Error notifying observer: {}", e.getMessage());
            }
        });
    }
}