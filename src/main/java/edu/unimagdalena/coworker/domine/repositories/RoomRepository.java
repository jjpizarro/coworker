package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Room;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findBySpace_Name(String spaceName); // por nombre de Space
    Page<Room> findByCapacityGreaterThanEqual(Integer minCapacity, Pageable pageable);
    List<Room> findDistinctByAmenities_NameIn(Collection<String> amenityNames);

    // JPQL: Salas disponibles SIN solapamientos en el intervalo [start, end)
    @Query("""
         SELECT r FROM Room r
         WHERE r.space.id = :spaceId
           AND NOT EXISTS (
             SELECT 1 FROM ReservationItem ri
             WHERE ri.room = r
               AND ri.startAt < :end
               AND ri.endAt   > :start
           )
         """)
    List<Room> findAvailableInInterval(@Param("spaceId") Long spaceId,
                                       @Param("start") OffsetDateTime start,
                                       @Param("end")   OffsetDateTime end);

    // Nativa: Salas que tienen TODAS las amenidades dadas (and lógico)
    @Query(value = """
      SELECT r.* FROM rooms r
      JOIN room_amenities ra ON ra.room_id = r.id
      JOIN amenities a ON a.id = ra.amenity_id
      WHERE a.name IN (:amenities)
      GROUP BY r.id
      HAVING COUNT(DISTINCT a.name) = :requiredCount
      """, nativeQuery = true)
    List<Room> findRoomsWithAllAmenities(@Param("amenities") Collection<String> amenities,
                                         @Param("requiredCount") long requiredCount);

    // EntityGraph para cargar amenities al traer rooms (anti N+1)
    @Override
    @EntityGraph(attributePaths = {"amenities", "space"})
    List<Room> findAll();

}
