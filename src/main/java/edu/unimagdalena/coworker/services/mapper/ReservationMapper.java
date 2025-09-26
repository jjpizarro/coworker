package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import java.util.List;

public class ReservationMapper {
    public static ReservationResponse toResponse(Reservation r) {
        var items = r.getItems() == null ? List.<ReservationItemResponse>of()
                : r.getItems().stream().map(ReservationMapper::toItemResponse).toList();

        var memberName  = r.getMember() != null ? r.getMember().getName()  : null;
        var memberEmail = r.getMember() != null ? r.getMember().getEmail() : null;

        return new ReservationResponse(r.getId(), memberName, memberEmail, r.getCreatedAt(), items);
    }

    public static ReservationItemResponse toItemResponse(ReservationItem i) {
        return new ReservationItemResponse(
                i.getId(),
                i.getRoom() != null ? i.getRoom().getId() : null,
                i.getRoom() != null ? i.getRoom().getName() : null,
                i.getStartAt(),
                i.getEndAt()
        );
    }
}
