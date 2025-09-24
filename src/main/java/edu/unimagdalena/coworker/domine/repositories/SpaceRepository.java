package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Space;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    Optional<Space> findByName(String name);
    List<Space> findByNameContainingIgnoreCase(String text);

    // JPQL + EntityGraph (rooms + amenities) => quita N+1 al listar espacios completos
    @EntityGraph(attributePaths = {"rooms", "rooms.amenities"})
    @Query("SELECT s FROM Space s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Space> searchSpacesDeep(@Param("q") String q);
}

