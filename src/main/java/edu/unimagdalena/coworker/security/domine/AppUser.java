package edu.unimagdalena.coworker.security.domine;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity @Table(name="users")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=120)
    private String email;

    @Column(nullable=false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    private Set<Role> roles;
}