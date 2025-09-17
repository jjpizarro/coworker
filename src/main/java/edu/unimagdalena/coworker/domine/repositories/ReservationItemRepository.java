package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import edu.unimagdalena.coworker.domine.view.ReservationItemView;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {
    List<ReservationItem> findByRoom_Id(Long roomId);
    @Query("""
        SELECT CASE WHEN COUNT(ri) > 0 THEN TRUE ELSE FALSE END 
        FROM ReservationItem ri
        WHERE ri.room.id = :roomId
        AND ri.startAt < :end
        AND ri.endAt > :start
    """)
    boolean existsOverlap(@Param("roomId") Long roomId,
                          @Param("start")OffsetDateTime start,
                          @Param("end") OffsetDateTime end);
    @Query("""
        SELECT new edu.unimagdalena.coworker.domine.view.ReservationItemView(
            ri.reservation.id, ri.room.id, ri.startAt, ri.endAt
        )
        FROM ReservationItem ri
        WHERE ri.reservation.id = :reservationId
        ORDER BY ri.startAt              
    """)
    List<ReservationItemView> findViewByReservation(@Param("reservationId") Long reservationId);
}
