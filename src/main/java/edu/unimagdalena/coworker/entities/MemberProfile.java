package edu.unimagdalena.coworker.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_profiles")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class MemberProfile {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    private String phone;
    private String company;

}
