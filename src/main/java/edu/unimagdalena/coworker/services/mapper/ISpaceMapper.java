package edu.unimagdalena.coworker.services.mapper;
import edu.unimagdalena.coworker.api.dto.SpaceDtos.*;
import edu.unimagdalena.coworker.domine.entities.Space;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface ISpaceMapper {
    @Mapping(target = "id", ignore = true)
    Space toEntity(SpaceCreateRequest req);

    SpaceResponse toResponse(Space s);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Space target, SpaceUpdateRequest changes);
}
