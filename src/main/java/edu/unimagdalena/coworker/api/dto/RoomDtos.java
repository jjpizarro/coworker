package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;
import java.util.Set;

public class RoomDtos {
    public record RoomCreateRequest(String name, Integer capacity) implements Serializable{}
    public record RoomResponse(Long id, String name, Integer capacity, Long spaceId, Set<AmenityDtos.AmenityResponse> amenities) implements Serializable{}
}
