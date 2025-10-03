package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.AmenityDtos;
import edu.unimagdalena.coworker.domine.entities.Amenity;

public class AmenityMapper {
    public static Amenity toEntity(AmenityDtos.AmenityCreateRequest req) {
        return Amenity.builder().name(req.name()).build();
    }
    public static AmenityDtos.AmenityResponse toResponse(Amenity a) {
        return new AmenityDtos.AmenityResponse(a.getId(), a.getName());
    }
}
