package org.deepak.interfaces;

import org.deepak.dto.ElevatorCar;
import org.deepak.enums.ElevatorState;

public interface ElevatorObserver {
    void onElevatorStateChanged(ElevatorCar elevator, ElevatorState newState);
    void onFloorChanged(ElevatorCar elevator, int newFloor);
    void onRequestCompleted(ElevatorCar elevator, int floor);
}