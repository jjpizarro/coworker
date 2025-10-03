package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.*;
import edu.unimagdalena.coworker.api.dto.RoomDtos.RoomCreateRequest;
import edu.unimagdalena.coworker.api.dto.RoomDtos.RoomResponse;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import java.util.stream.Collectors;

public class RoomMapper {
    public static Room toEntity(RoomCreateRequest req) {
        return Room.builder().name(req.name()).capacity(req.capacity()).build();
    }
    public static RoomResponse toResponse(Room r) {
        var amenities = r.getAmenities() == null ? java.util.Set.<AmenityResponse>of() :
                r.getAmenities().stream()
                        .map(a -> new AmenityResponse(a.getId(), a.getName()))
                        .collect(Collectors.toSet());
        return new RoomResponse(r.getId(), r.getName(), r.getCapacity(),
                r.getSpace() != null ? r.getSpace().getId() : null, amenities);
    }
    public static void addAmenity(Room r, Amenity a) { r.addAmenity(a); }
}
