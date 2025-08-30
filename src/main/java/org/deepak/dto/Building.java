package org.deepak.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

public class Building {
    @Getter
    @Setter
    private List<Floor> floors = new ArrayList<>();
    
    @Getter
    @Setter
    private List<ElevatorCar> elevators = new ArrayList<>();

    private static volatile Building building = null;

    public static Building getInstance() {
        if (building == null) {
            synchronized (Building.class) {
                if (building == null) {
                    building = new Building();
                }
            }
        }
        return building;
    }
    
    public Floor getFloor(int floorNumber) {
        if (floorNumber < 1 || floorNumber > floors.size()) {
            throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
        }
        return floors.get(floorNumber-1);
    }

    public void setFloor(int noOfFloor) {
        this.floors = new ArrayList<>();
        for(int i=0;i<noOfFloor;i++){
            Floor floor = new Floor(i+1);
            floor.addDisplay(elevators.size());
            this.floors.add(floor);
        }
    }

    public void addElevator(ElevatorCar elevator){
        this.elevators.add(elevator);
    }
}
