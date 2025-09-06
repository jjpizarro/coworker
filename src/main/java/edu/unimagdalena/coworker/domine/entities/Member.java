package edu.unimagdalena.coworker.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    private String email;
    @OneToOne @JoinColumn(name = "profile_id", unique = true)
    private MemberProfile profile;

}
