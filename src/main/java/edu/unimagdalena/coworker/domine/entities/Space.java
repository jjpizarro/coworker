package edu.unimagdalena.coworker.domine.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "spaces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "space")
    private List<Room> rooms = new ArrayList<>();
    public void addRoom(Room r){
        rooms.add(r);
        r.setSpace(this);

    }

}
