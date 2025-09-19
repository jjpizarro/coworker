package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.MemberProfile;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends AbstractRepositoryIT {

    @Autowired MemberRepository memberRepo;

    @Test
    @DisplayName("Member: encuentra por email (ignore case) y hace fetch del profile")
    void shouldFindByEmailIgnoreCaseAndFetchProfileByEmail() {
        // Given
        var profile = MemberProfile.builder().phone("+57-300").company("UniMagdalena").build();
        var member  = Member.builder().name("Julian").email("JULIAN@DEMO.COM").profile(profile).build();
        memberRepo.save(member);

        // When
        Optional<Member> byEmail = memberRepo.findByEmailIgnoreCase("julian@demo.com");
        Optional<Member> withProfile = memberRepo.fetchWithProfileByEmail("julian@demo.com");

        // Then
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getEmail()).isEqualTo("JULIAN@DEMO.COM");
        assertThat(withProfile).isPresent();
        assertThat(withProfile.get().getProfile().getCompany()).isEqualTo("UniMagdalena");
    }
}