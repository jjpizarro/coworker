package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
class RoomRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    RoomRepository roomRepo;
    @Autowired SpaceRepository spaceRepo;
    @Autowired AmenityRepository amenityRepo;
    @Autowired MemberRepository memberRepo;
    @Autowired ReservationRepository reservationRepo;
    @Autowired ReservationItemRepository reservationItemRepo;

    @Test
    @DisplayName("Room: consultas derivadas por space, capacidad y amenidades")
    void shouldFindBySpaceNameCapacityAndDistinctAmenities() {
        // Given
        var s = spaceRepo.save(Space.builder().name("HQ Norte").build());
        var a = roomRepo.save(Room.builder().name("Norte-1").capacity(6).space(s).build());
        var b = roomRepo.save(Room.builder().name("Norte-2").capacity(10).space(s).build());

        var wifi = amenityRepo.save(Amenity.builder().name("WiFi").build());
        var tv   = amenityRepo.save(Amenity.builder().name("TV").build());
        a.addAmenity(wifi); a.addAmenity(tv);
        b.addAmenity(wifi);
        roomRepo.save(a); roomRepo.save(b);

        // When / Then
        assertThat(roomRepo.findBySpace_Name("HQ Norte")).hasSize(2);

        assertThat(roomRepo.findByCapacityGreaterThanEqual(8, Pageable.unpaged()).getContent())
                .extracting(Room::getName).containsExactly("Norte-2");

        assertThat(roomRepo.findDistinctByAmenities_NameIn(List.of("TV")))
                .extracting(Room::getName).containsExactly("Norte-1");
    }

    @Test
    @DisplayName("Room: devuelve solo salas disponibles en intervalo [start,end)")
    void shouldReturnOnlyRoomsAvailableInInterval() {
        // Given
        var space = spaceRepo.save(Space.builder().name("Sede Centro").build());
        var a = roomRepo.save(Room.builder().name("Sala A").capacity(8).space(space).build());
        var b = roomRepo.save(Room.builder().name("Sala B").capacity(8).space(space).build());

        var member = memberRepo.save(Member.builder().name("Ana").email("ana@demo.com").build());
        var base = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        var res = reservationRepo.save(Reservation.builder().member(member).createdAt(OffsetDateTime.now(ZoneOffset.UTC)).build());
        reservationItemRepo.save(ReservationItem.builder().reservation(res).room(a)
                .startAt(base).endAt(base.plusHours(2)).build()); // 10-12

        // When
        var avail = roomRepo.findAvailableInInterval(space.getId(), base.plusHours(1), base.plusHours(3));

        // Then
        assertThat(avail).extracting(Room::getName).containsExactly("Sala B");
    }

    @Test
    @DisplayName("Room: nativa exige TODAS las amenidades listadas")
    void shouldReturnRoomsHavingAllAmenities() {
        // Given
        var s = spaceRepo.save(Space.builder().name("Sede Sur").build());
        var a = roomRepo.save(Room.builder().name("Sala 1").capacity(8).space(s).build());
        var b = roomRepo.save(Room.builder().name("Sala 2").capacity(8).space(s).build());

        var wifi = amenityRepo.save(Amenity.builder().name("WiFi").build());
        var tv   = amenityRepo.save(Amenity.builder().name("TV").build());
        a.addAmenity(wifi); a.addAmenity(tv);
        b.addAmenity(wifi);
        roomRepo.save(a); roomRepo.save(b);

        // When
        var all = roomRepo.findRoomsWithAllAmenities(List.of("WiFi", "TV"), 2);

        // Then
        assertThat(all).extracting(Room::getName).containsExactly("Sala 1");
    }
}