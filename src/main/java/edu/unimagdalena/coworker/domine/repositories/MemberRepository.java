package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailIgnoreCase(String email);

    // JPQL: traer el Member con su MemberProfile (evitar N+1)
    @Query("""
         SELECT m FROM Member m
         LEFT JOIN FETCH m.profile
         WHERE LOWER(m.email) = LOWER(:email)
         """)
    Optional<Member> fetchWithProfileByEmail(@Param("email") String email);
}
