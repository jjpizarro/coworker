package edu.unimagdalena.coworker.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

public class RoomDtos {
    public record RoomCreateRequest(
            @NotBlank String name, @Min(1) Integer capacity) implements Serializable{}
    public record RoomResponse(Long id, String name, Integer capacity, Long spaceId, Set<AmenityDtos.AmenityResponse> amenities) implements Serializable{}
}
