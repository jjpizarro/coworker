package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.entities.Space;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
class SpaceRepositoryTest extends AbstractRepositoryIT {

    @Autowired SpaceRepository spaceRepo;
    @Autowired RoomRepository roomRepo;
    @Autowired AmenityRepository amenityRepo;

    @Test
    @DisplayName("Space: búsqueda profunda sin N+1 (rooms + amenities)")
    void shouldSearchSpacesWithRoomsAndAmenitiesWithoutNPlusOne() {
        // Given
        var space = spaceRepo.save(Space.builder().name("Cowork Centro").address("Cra 1").build());
        var r1 = roomRepo.save(Room.builder().name("Sala A").capacity(8).space(space).build());
        var r2 = roomRepo.save(Room.builder().name("Sala B").capacity(10).space(space).build());
        var wifi = amenityRepo.save(Amenity.builder().name("WiFi").build());
        var tv   = amenityRepo.save(Amenity.builder().name("TV").build());
        r1.addAmenity(wifi); r1.addAmenity(tv);
        r2.addAmenity(wifi);
        roomRepo.save(r1); roomRepo.save(r2);

        // When
        var found = spaceRepo.searchSpacesDeep("centro");

        // Then
        assertThat(found).hasSize(1);
        var loaded = found.get(0);
        assertThat(loaded.getRooms()).hasSize(2);
        assertThat(loaded.getRooms().stream()
                .flatMap(r -> r.getAmenities().stream())
                .map(Amenity::getName))
                .contains("WiFi", "TV");
    }
}