package edu.unimagdalena.coworker.services;

import static org.junit.jupiter.api.Assertions.*;

import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.entities.Space;
import edu.unimagdalena.coworker.domine.repositories.AmenityRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationItemRepository;
import edu.unimagdalena.coworker.domine.repositories.RoomRepository;
import edu.unimagdalena.coworker.domine.repositories.SpaceRepository;
import edu.unimagdalena.coworker.services.mapper.RoomMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    RoomRepository roomRepo;
    @Mock
    SpaceRepository spaceRepo;
    @Mock
    AmenityRepository amenityRepo;
    @Mock
    ReservationItemRepository itemRepo;
    @InjectMocks
    RoomServiceImpl service;
    @Spy
    private RoomMapper mapper = Mappers.getMapper(RoomMapper.class);
    @Test
    void shouldCreateUnderSpaceReturningDto() {
        when(spaceRepo.findById(1L)).thenReturn(Optional.of(Space.builder().id(1L).name("HQ").build()));
        when(roomRepo.save(any())).thenAnswer(inv -> {
            Room r = inv.getArgument(0);
            r.setId(100L);
            return r;
        });

        var res = service.create(1L, new RoomCreateRequest("Sala A", 8));

        assertThat(res.id()).isEqualTo(100L);
        assertThat(res.spaceId()).isEqualTo(1L);
        assertThat(res.name()).isEqualTo("Sala A");
    }

    @Test
    void shouldAddAmenityAndMapResponse() {
        var room = Room.builder().id(5L).name("R").amenities(new HashSet<>()).build();
        var amenity = Amenity.builder().id(2L).name("WiFi").rooms(new HashSet<>()).build();

        when(roomRepo.findById(anyLong())).thenReturn(Optional.of(room));
        when(amenityRepo.findById(2L)).thenReturn(Optional.of(amenity));

        var roomResponse = service.addAmenity(5L, 2L);

        assertThat(roomResponse.amenities()).extracting(a -> a.name()).contains("WiFi");
    }

    @Test
    void shouldFindAvailableAndFilterByCapacity() {
        var r1 = Room.builder().id(1L).name("A").capacity(6).build();
        var r2 = Room.builder().id(2L).name("B").capacity(10).build();
        when(roomRepo.findAvailableInInterval(anyLong(), any(), any())).thenReturn(List.of(r1, r2));

        var start = OffsetDateTime.now(); var end = start.plusHours(2);
        var roomResponses = service.findAvailable(1L, start, end, 8);

        assertThat(roomResponses).extracting(RoomResponse::id).containsExactly(2L);
    }
}