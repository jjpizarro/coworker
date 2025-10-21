package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.entities.Space;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoomMapperTest {

    private final RoomMapper mapper = Mappers.getMapper(RoomMapper.class);
    @Test
    void toEntity_shouldIgnoreSpaceAndAmenities() {
        Room r = mapper.toEntity(new RoomCreateRequest("Sala A", 8));
        assertThat(r.getName()).isEqualTo("Sala A");
        assertThat(r.getSpace()).isNull();       // se setea en el servicio
    }

    @Test
    void toResponse_shouldMapSpaceIdAndAmenities() {
        var space = Space.builder().id(3L).name("HQ").build();
        var amenity = Amenity.builder().id(9L).name("WiFi").build();
        var room = Room.builder().id(2L).name("Sala A").capacity(8).space(space)
                .amenities(Set.of(amenity)).build();

        RoomResponse dto = mapper.toResponse(room);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.spaceId()).isEqualTo(3L);
        assertThat(dto.amenities()).extracting(a -> a.name()).contains("WiFi");
    }
}
