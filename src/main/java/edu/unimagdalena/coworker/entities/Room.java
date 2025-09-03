package edu.unimagdalena.coworker.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id
    @GeneratedValue
            (strategy = GenerationType.AUTO)
    private Long id;
    private Space space;
}
