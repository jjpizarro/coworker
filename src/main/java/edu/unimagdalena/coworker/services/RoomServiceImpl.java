package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.domine.entities.Amenity;
import edu.unimagdalena.coworker.domine.entities.Room;
import edu.unimagdalena.coworker.domine.entities.Space;
import edu.unimagdalena.coworker.domine.repositories.AmenityRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationItemRepository;
import edu.unimagdalena.coworker.domine.repositories.RoomRepository;
import edu.unimagdalena.coworker.domine.repositories.SpaceRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.RoomMapper;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor @Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepo;
    private final SpaceRepository spaceRepo;
    private final AmenityRepository amenityRepo;

    @Override
    public RoomResponse create(Long spaceId, RoomCreateRequest req) {
        Space s = spaceRepo.findById(spaceId).orElseThrow(() -> new NotFoundException("Space %d not found".formatted(spaceId)));
        Room r = RoomMapper.toEntity(req);
        r.setSpace(s);
        return RoomMapper.toResponse(roomRepo.save(r));
    }

    @Override @Transactional(readOnly = true)
    public RoomResponse get(Long id) {
        return roomRepo.findById(id).map(RoomMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Room %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public List<RoomResponse> listBySpace(Long spaceId) {
        Space s = spaceRepo.findById(spaceId).orElseThrow(() -> new NotFoundException("Space %d not found".formatted(spaceId)));
        return roomRepo.findBySpace_Name(s.getName()).stream().map(RoomMapper::toResponse).toList();
    }

    @Override
    public RoomResponse addAmenity(Long roomId, Long amenityId) {
        Room r = roomRepo.findById(roomId).orElseThrow(() -> new NotFoundException("Room %d not found".formatted(roomId)));
        Amenity a = amenityRepo.findById(amenityId).orElseThrow(() -> new NotFoundException("Amenity %d not found".formatted(amenityId)));
        RoomMapper.addAmenity(r, a);
        return RoomMapper.toResponse(r);
    }

    @Override
    public RoomResponse removeAmenity(Long roomId, Long amenityId) {
        Room r = roomRepo.findById(roomId).orElseThrow(() -> new NotFoundException("Room %d not found".formatted(roomId)));
        Amenity a = amenityRepo.findById(amenityId).orElseThrow(() -> new NotFoundException("Amenity %d not found".formatted(amenityId)));
        r.getAmenities().remove(a);
        a.getRooms().remove(r);
        return RoomMapper.toResponse(r);
    }

    @Override @Transactional(readOnly = true)
    public List<RoomResponse> findAvailable(Long spaceId, OffsetDateTime start, OffsetDateTime end, Integer minCapacity) {
        var list = roomRepo.findAvailableInInterval(spaceId, start, end);
        if (minCapacity != null) {
            list = list.stream().filter(r -> r.getCapacity() != null && r.getCapacity() >= minCapacity).toList();
        }
        return list.stream().map(RoomMapper::toResponse).toList();
    }

    @Override public void delete(Long id) { roomRepo.deleteById(id); }
}
