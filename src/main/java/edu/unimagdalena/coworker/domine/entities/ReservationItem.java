package edu.unimagdalena.coworker.domine.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Entity @Table(name = "reservation_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    @Column(nullable = false)
    private OffsetDateTime startAt;
    @Column(nullable = false)
    private OffsetDateTime endAt;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

}
