package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    RoomResponse create(Long spaceId, RoomCreateRequest req);
    RoomResponse get(Long id);
    List<RoomResponse> listBySpace(Long spaceId);
    RoomResponse addAmenity(Long roomId, Long amenityId);
    RoomResponse removeAmenity(Long roomId, Long amenityId);
    List<RoomResponse> findAvailable(Long spaceId, OffsetDateTime start, OffsetDateTime end, Integer minCapacity);
    void delete(Long id);
}
