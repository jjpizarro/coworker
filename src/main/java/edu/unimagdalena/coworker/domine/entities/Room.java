package edu.unimagdalena.coworker.domine.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue
            (strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;
    private String name;
    private Integer capacity;
    @ManyToMany
    @JoinTable(name = "room_amenities",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities = new HashSet<>();

    public void addAmenity(Amenity a){
        amenities.add(a);
        a.getRooms().add(this);
    }
}
