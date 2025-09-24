package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationItemRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    ReservationItemRepository reservationItemRepo;
    @Autowired ReservationRepository reservationRepo;
    @Autowired RoomRepository roomRepo;
    @Autowired SpaceRepository spaceRepo;
    @Autowired MemberRepository memberRepo;

    @Test
    @DisplayName("ReservationItem: TRUE si hay solape; FALSE en el borde (end=start)")
    void shouldDetectOverlapAndNotOnBoundary() {
        // Given
        var m = memberRepo.save(Member.builder().name("User").email("u@d.com").build());
        var s = spaceRepo.save(Space.builder().name("Sede").build());
        var r = roomRepo.save(Room.builder().name("Sala").capacity(6).space(s).build());

        var base = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).withMinute(0).withSecond(0).withNano(0);
        var res = reservationRepo.save(Reservation.builder().member(m).createdAt(base.minusHours(2)).build());
        reservationItemRepo.save(ReservationItem.builder()
                .reservation(res).room(r).startAt(base.withHour(10)).endAt(base.withHour(12)).build());

        // When / Then
        assertThat(reservationItemRepo.existsOverlap(r.getId(), base.withHour(11), base.withHour(13))).isTrue(); // 11-13 solapa
        assertThat(reservationItemRepo.existsOverlap(r.getId(), base.withHour(12), base.withHour(13))).isFalse(); // 12-13 no (borde)
    }

    @Test
    @DisplayName("ReservationItem: lista solapes ordenados con room y reservation precargados (fetch join)")
    void shouldListOverlappingBlocksSortedWithPreloadedAssociations() {
        // Given
        var m = memberRepo.save(Member.builder().name("User").email("u@d.com").build());
        var s = spaceRepo.save(Space.builder().name("Sede").build());
        var r = roomRepo.save(Room.builder().name("Sala").capacity(6).space(s).build());

        var today = OffsetDateTime.now(ZoneOffset.UTC).plusDays(2).withMinute(0).withSecond(0).withNano(0);
        var r1 = reservationRepo.save(Reservation.builder().member(m).createdAt(today.minusHours(3)).build());
        var r2 = reservationRepo.save(Reservation.builder().member(m).createdAt(today.minusHours(2)).build());

        reservationItemRepo.save(ReservationItem.builder().reservation(r1).room(r)
                .startAt(today.withHour(9)).endAt(today.withHour(11)).build());
        reservationItemRepo.save(ReservationItem.builder().reservation(r2).room(r)
                .startAt(today.withHour(10)).endAt(today.withHour(12)).build());

        // When
        var list = reservationItemRepo.findOverlaps(r.getId(), today.withHour(10), today.withHour(13));

        // Then
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getStartAt()).isBeforeOrEqualTo(list.get(1).getStartAt());
        assertThat(list.get(0).getRoom().getName()).isEqualTo("Sala");
        assertThat(list.get(0).getReservation().getId()).isNotNull();
    }

    @Test
    @DisplayName("ReservationItem: recupera ítems por roomId")
    void shouldFindItemsByRoomId() {
        // Given
        var m = memberRepo.save(Member.builder().name("User").email("u@d.com").build());
        var s = spaceRepo.save(Space.builder().name("Sede").build());
        var r = roomRepo.save(Room.builder().name("Sala").capacity(6).space(s).build());

        var res = reservationRepo.save(Reservation.builder().member(m).createdAt(OffsetDateTime.now(ZoneOffset.UTC)).build());
        reservationItemRepo.saveAll(List.of(
                ReservationItem.builder().reservation(res).room(r)
                        .startAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                        .endAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(2)).build(),
                ReservationItem.builder().reservation(res).room(r)
                        .startAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(3))
                        .endAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(4)).build()
        ));

        // When
        var items = reservationItemRepo.findByRoom_Id(r.getId());

        // Then
        assertThat(items).hasSize(2);
    }

}