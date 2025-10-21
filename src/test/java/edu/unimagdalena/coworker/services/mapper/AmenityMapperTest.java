package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class AmenityMapperTest {

    private final AmenityMapper mapper = Mappers.getMapper(AmenityMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Amenity a = mapper.toEntity(new AmenityCreateRequest("WiFi"));
        assertThat(a.getName()).isEqualTo("WiFi");
    }

    @Test
    void toResponse_shouldMapEntity() {
        var a = Amenity.builder().id(5L).name("WiFi").build();
        AmenityResponse dto = mapper.toResponse(a);
        assertThat(dto.id()).isEqualTo(5L);
    }
}
