package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.SpaceDtos.*;
import edu.unimagdalena.coworker.domine.entities.Space;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class SpaceMapperTest {

    private final SpaceMapper mapper = Mappers.getMapper(SpaceMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        var req = new SpaceCreateRequest("HQ", "Calle 1");
        Space s = mapper.toEntity(req);
        assertThat(s.getName()).isEqualTo("HQ");
        assertThat(s.getAddress()).isEqualTo("Calle 1");
    }

    @Test
    void toResponse_shouldMapEntity() {
        var s = Space.builder().id(2L).name("HQ").address("Calle 1").build();
        SpaceResponse dto = mapper.toResponse(s);
        assertThat(dto.id()).isEqualTo(2L);
    }

    @Test
    void patch_shouldIgnoreNulls() {
        var s = Space.builder().id(1L).name("Old").address("Addr").build();
        mapper.patch(s, new SpaceUpdateRequest("New", null));
        assertThat(s.getName()).isEqualTo("New");
        assertThat(s.getAddress()).isEqualTo("Addr");
    }
}
