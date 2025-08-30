package org.deepak.dto;

import lombok.Getter;
import org.deepak.enums.Direction;

@Getter
public class Request implements Comparable<Request> {
    private final int floor;
    private final Direction direction;
    private final long timestamp;
    private final boolean isInternal;

    public Request(int floor, Direction direction, boolean isInternal) {
        this.floor = floor;
        this.direction = direction;
        this.timestamp = System.currentTimeMillis();
        this.isInternal = isInternal;
    }

    @Override
    public int compareTo(Request other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}