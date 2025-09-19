package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Reservation;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMember_EmailIgnoreCaseOrderByCreatedAtDesc(String email, Pageable pageable);

    List<Reservation> findByMember_EmailIgnoreCaseAndCreatedAtBetween(
            String email, OffsetDateTime from, OffsetDateTime to);

    // JPQL: traer Reservation con sus items y room (fetch) para una sola consulta
    @EntityGraph(attributePaths = {"items", "items.room", "member"})
    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Reservation fetchGraphById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reservations WHERE created_at < :threshold", nativeQuery = true)
    int deleteOlderThan(@Param("threshold") OffsetDateTime threshold);
}
