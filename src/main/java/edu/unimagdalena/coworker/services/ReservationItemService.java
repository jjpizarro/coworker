package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationItemService {
    ReservationItemResponse addItem(Long reservationId, ReservationItemCreateRequest req);
    List<ReservationItemResponse> listByReservation(Long reservationId);
    void removeItem(Long itemId);
    boolean hasConflict(Long roomId, OffsetDateTime start, OffsetDateTime end);
}
