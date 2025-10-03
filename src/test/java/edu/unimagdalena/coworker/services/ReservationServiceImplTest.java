package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.repositories.MemberRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    ReservationRepository repo;
    @Mock
    MemberRepository memberRepo;
    @InjectMocks
    ReservationServiceImpl service;

    @Test
    void shouldCreateDraftReturningDto() {
        when(memberRepo.findById(1L)).thenReturn(Optional.of(Member.builder().id(1L).name("Ana").email("a@d.com").build()));
        when(repo.save(any())).thenAnswer(inv -> { Reservation r = inv.getArgument(0); r.setId(9L); return r; });

        var reservationResponse = service.createDraft(new ReservationCreateRequest(1L));
        assertThat(reservationResponse.id()).isEqualTo(9L);
        assertThat(reservationResponse.memberEmail()).isEqualTo("a@d.com");
    }

    @Test
    void shouldPageByMemberMapping() {
        var page = new PageImpl<>(java.util.List.of(Reservation.builder().id(3L).createdAt(OffsetDateTime.now()).build()));
        when(repo.findByMember_EmailIgnoreCaseOrderByCreatedAtDesc(eq("u@d.com"), any())).thenReturn(page);

        var res = service.listByMember("u@d.com", PageRequest.of(0, 10));
        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getContent().get(0).id()).isEqualTo(3L);
    }
}