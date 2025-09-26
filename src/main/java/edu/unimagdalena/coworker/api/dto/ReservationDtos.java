package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class ReservationDtos {
    public record ReservationCreateRequest(Long memberId) implements Serializable{}
    public record ReservationResponse(Long id, String memberName, String memberEmail, OffsetDateTime createAt, List<ReservationItemResponse> items) implements Serializable{}
    public record ReservationItemResponse(Long id,Long roomId, String roomName, OffsetDateTime startAt, OffsetDateTime endAt ) implements Serializable{}
    public record ReservationItemCreateRequest(long roomId, OffsetDateTime startAt, OffsetDateTime endAt) implements Serializable{}
}
