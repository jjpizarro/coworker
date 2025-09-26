package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.SpaceDtos;
import edu.unimagdalena.coworker.domine.entities.Space;

public class SpaceMapper {
    public static Space toEntity(SpaceDtos.SpaceCreateRequest req) {
        return Space.builder().name(req.name()).address(req.address()).build();
    }
    public static void patch(Space s, SpaceDtos.SpaceUpdateRequest req) {
        if (req.name() != null) s.setName(req.name());
        if (req.address() != null) s.setAddress(req.address());
    }
    public static SpaceDtos.SpaceResponse toResponse(Space s) {
        return new SpaceDtos.SpaceResponse(s.getId(), s.getName(), s.getAddress());
    }
}
