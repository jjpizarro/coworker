package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.repositories.ReservationItemRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationRepository;
import edu.unimagdalena.coworker.domine.repositories.RoomRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationItemServiceImpl implements ReservationItemService {

    private final ReservationItemRepository itemRepo;
    private final ReservationRepository reservationRepo;
    private final RoomRepository roomRepo;

    @Override
    public ReservationItemResponse addItem(Long reservationId, ReservationItemCreateRequest req) {
        if (!req.startAt().isBefore(req.endAt()))
            throw new IllegalArgumentException("startAt must be before endAt");

        boolean conflict = itemRepo.existsOverlap(req.roomId(), req.startAt(), req.endAt());
        if (conflict) throw new IllegalStateException("Room %d has a conflicting reservation".formatted(req.roomId()));

        Reservation res = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation %d not found".formatted(reservationId)));
        Room room = roomRepo.findById(req.roomId())
                .orElseThrow(() -> new NotFoundException("Room %d not found".formatted(req.roomId())));

        ReservationItem item = ReservationItem.builder()
                .reservation(res).room(room).startAt(req.startAt()).endAt(req.endAt()).build();

        return ReservationMapper.toItemResponse(itemRepo.save(item));
    }

    @Override @Transactional(readOnly = true)
    public List<ReservationItemResponse> listByReservation(Long reservationId) {
        Reservation res = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation %d not found".formatted(reservationId)));
        return itemRepo.findByReservation_Id(res.getId()).stream()
                .map(ReservationMapper::toItemResponse).toList();
    }

    @Override
    public void removeItem(Long itemId) { itemRepo.deleteById(itemId); }

    @Override @Transactional(readOnly = true)
    public boolean hasConflict(Long roomId, OffsetDateTime start, OffsetDateTime end) {
        return itemRepo.existsOverlap(roomId, start, end);
    }
}
