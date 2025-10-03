package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.repositories.ReservationItemRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationRepository;
import edu.unimagdalena.coworker.domine.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationItemServiceImplDtoTest {

    @Mock
    ReservationItemRepository itemRepo;
    @Mock
    ReservationRepository reservationRepo;
    @Mock
    RoomRepository roomRepo;
    @InjectMocks ReservationItemServiceImpl service;

    @Test
    void shouldAddItemAndMapToDto() {
        when(itemRepo.existsOverlap(anyLong(), any(), any())).thenReturn(false);
        when(reservationRepo.findById(1L)).thenReturn(Optional.of(Reservation.builder().id(1L).build()));
        when(roomRepo.findById(2L)).thenReturn(Optional.of(Room.builder().id(2L).name("Sala A").build()));
        when(itemRepo.save(any())).thenAnswer(inv -> { ReservationItem it = inv.getArgument(0); it.setId(33L); return it; });

        var start = OffsetDateTime.now(); var end = start.plusHours(1);
        var dto = new ReservationItemCreateRequest(2L, start, end);
        var out = service.addItem(1L, dto);

        assertThat(out.id()).isEqualTo(33L);
        assertThat(out.roomName()).isEqualTo("Sala A");
    }

    @Test
    void shouldListItemsMapping() {
        when(reservationRepo.findById(1L)).thenReturn(Optional.of(Reservation.builder().id(1L).build()));
        when(itemRepo.findByReservation_Id(1L)).thenReturn(List.of(ReservationItem.builder()
                .id(9L).room(Room.builder().id(7L).name("X").build()).build()));

        var list = service.listByReservation(1L);
        assertThat(list).extracting(ReservationItemResponse::id).containsExactly(9L);
    }
}