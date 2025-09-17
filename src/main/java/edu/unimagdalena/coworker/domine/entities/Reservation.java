package edu.unimagdalena.coworker.domine.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @OneToMany(mappedBy = "reservation")
    private List<ReservationItem> items = new ArrayList<>();

    public void addItem(ReservationItem item){
        items.add(item);
        item.setReservation(this);
    }


}
