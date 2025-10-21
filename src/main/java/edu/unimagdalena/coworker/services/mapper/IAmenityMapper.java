package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import org.mapstruct.Mapping;

public interface IAmenityMapper {
    @Mapping(target = "id", ignore = true)
    Amenity toEntity(AmenityCreateRequest req);

    AmenityResponse toResponse(Amenity a);
}
