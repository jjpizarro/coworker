package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
