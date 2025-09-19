package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    ReservationRepository reservationRepo;
    @Autowired ReservationItemRepository reservationItemRepo;
    @Autowired MemberRepository memberRepo;
    @Autowired SpaceRepository spaceRepo;
    @Autowired RoomRepository roomRepo;

    @Test
    @DisplayName("Reservation: fetch graph (member + items + room) en una sola carga lógica")
    void shouldFetchReservationGraphWithMemberItemsAndRoom() {
        // Given
        var member = memberRepo.save(Member.builder().name("Maria").email("maria@demo.com").build());
        var space  = spaceRepo.save(Space.builder().name("HQ Centro").build());
        var room   = roomRepo.save(Room.builder().name("Sala A").capacity(8).space(space).build());

        var now = OffsetDateTime.now(ZoneOffset.UTC);
        var res = reservationRepo.save(Reservation.builder().member(member).createdAt(now).build());
        reservationItemRepo.save(ReservationItem.builder()
                .reservation(res).room(room).startAt(now.plusHours(1)).endAt(now.plusHours(2)).build());

        // When
        var loaded = reservationRepo.fetchGraphById(res.getId());

        // Then
        assertThat(loaded.getMember().getEmail()).isEqualTo("maria@demo.com");
        assertThat(loaded.getItems()).hasSize(1);
        assertThat(loaded.getItems().get(0).getRoom().getName()).isEqualTo("Sala A");
    }

    @Test
    @DisplayName("Reservation: pagina por email de member ordenado desc por createdAt")
    void shouldPageReservationsByMemberOrderedByCreatedAtDesc() {
        // Given
        var m = memberRepo.save(Member.builder().name("Ana").email("ana@demo.com").build());
        reservationRepo.save(Reservation.builder().member(m).createdAt(OffsetDateTime.now(ZoneOffset.UTC).minusDays(1)).build());
        reservationRepo.save(Reservation.builder().member(m).createdAt(OffsetDateTime.now(ZoneOffset.UTC)).build());

        // When
        var page = reservationRepo.findByMember_EmailIgnoreCaseOrderByCreatedAtDesc("ANA@demo.com", PageRequest.of(0, 1));

        // Then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent().get(0).getCreatedAt())
                .isAfterOrEqualTo(page.getContent().get(page.getNumberOfElements() - 1).getCreatedAt());
    }

    @Test
    @DisplayName("Reservation: @Modifying nativa elimina reservas anteriores al umbral")
    void shouldDeleteReservationsOlderThanThreshold() {
        // Given
        var m = memberRepo.save(Member.builder().name("User").email("u@d.com").build());
        var oldRes = reservationRepo.save(Reservation.builder().member(m)
                .createdAt(OffsetDateTime.now(ZoneOffset.UTC).minusDays(10)).build());
        var newRes = reservationRepo.save(Reservation.builder().member(m)
                .createdAt(OffsetDateTime.now(ZoneOffset.UTC).minusDays(1)).build());

        // When
        int deleted = reservationRepo.deleteOlderThan(OffsetDateTime.now(ZoneOffset.UTC).minusDays(5));

        // Then
        assertThat(deleted).isEqualTo(1);
        assertThat(reservationRepo.findById(oldRes.getId())).isEmpty();
        assertThat(reservationRepo.findById(newRes.getId())).isPresent();
    }

}