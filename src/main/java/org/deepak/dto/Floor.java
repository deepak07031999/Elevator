package org.deepak.dto;

import lombok.Getter;
import org.deepak.dto.panels.HallPanel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Floor {
    private final int floorNumber;
    private final Map<Integer,Display> display;
    private final HallPanel panel;

    public Floor(int floorNumber){
        this.display = new ConcurrentHashMap<>();
        this.panel = new HallPanel(floorNumber);
        this.floorNumber= floorNumber;
    }
    
    public void addDisplay(int noOfDisplays){
        for(int i=0;i<noOfDisplays;i++){
            display.put(i+1 ,new Display(i+1));
        }
    }
}
