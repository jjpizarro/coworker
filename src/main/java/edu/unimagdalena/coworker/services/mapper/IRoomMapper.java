package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.AmenityResponse;
import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRoomMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "space", ignore = true) // se setea en el servicio
    @Mapping(target = "amenities", ignore = true)
    Room toEntity(RoomCreateRequest req);
    @Mapping(target = "spaceId", source = "space.id")
    @Mapping(target = "amenities", source = "amenities")
    RoomResponse toResponse(Room r);
    Set<AmenityResponse> toAmenityDtos(Set<Amenity> amenities);
    AmenityResponse toAmenityDto(Amenity a);

}
