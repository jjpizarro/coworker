package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
