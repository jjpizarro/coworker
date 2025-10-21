package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.entities.ReservationItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface IReservationMapper {
    @Mapping(target = "memberName",  expression = "java(reservation.getMember() != null ? reservation.getMember().getName() : null)")
    @Mapping(target = "memberEmail", expression = "java(reservation.getMember() != null ? reservation.getMember().getEmail() : null)")
    @Mapping(target = "items", source = "items")
    ReservationResponse toResponse(Reservation reservation);

    @Mapping(target = "roomId",   source = "room.id")
    @Mapping(target = "roomName", source = "room.name")
    ReservationItemResponse toItemResponse(ReservationItem item);

    List<ReservationItemResponse> toItemResponses(List<ReservationItem> items);
}
