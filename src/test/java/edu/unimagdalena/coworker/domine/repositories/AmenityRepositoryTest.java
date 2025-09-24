package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Amenity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
class AmenityRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AmenityRepository amenityRepo;

    @Test
    @DisplayName("Amenity: encuentra por nombre y por lista de nombres")
    void shouldFindByNameAndByNameInList() {
        // Given
        amenityRepo.save(Amenity.builder().name("WiFi").build());
        amenityRepo.save(Amenity.builder().name("TV").build());

        // When / Then
        assertThat(amenityRepo.findByName("WiFi")).isPresent();
        assertThat(amenityRepo.findByNameIn(List.of("WiFi", "TV", "Proyector")))
                .extracting(Amenity::getName)
                .contains("WiFi", "TV");
    }

}