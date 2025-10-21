package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import edu.unimagdalena.coworker.domine.entities.Room;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationMapperTest {

    private final ReservationMapper mapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void toResponse_shouldMapMemberFieldsAndItems() {
        var m = Member.builder().id(1L).name("Ana").email("a@d.com").build();
        var room = Room.builder().id(2L).name("Sala A").build();
        var item = ReservationItem.builder().id(7L).room(room).startAt(OffsetDateTime.now())
                .endAt(OffsetDateTime.now().plusHours(1)).build();
        var r = Reservation.builder().id(5L).member(m).items(List.of(item)).build();

        ReservationResponse dto = mapper.toResponse(r);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.memberEmail()).isEqualTo("a@d.com");
        assertThat(dto.items()).extracting(ReservationItemResponse::roomId).contains(2L);
    }
}
