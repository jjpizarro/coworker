package edu.unimagdalena.coworker.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class ReservationDtos {
    public record ReservationCreateRequest(@NotNull Long memberId) implements Serializable{}
    public record ReservationResponse(Long id, String memberName, String memberEmail, OffsetDateTime createAt, List<ReservationItemResponse> items) implements Serializable{}
    public record ReservationItemResponse(Long id,Long roomId, String roomName, OffsetDateTime startAt, OffsetDateTime endAt ) implements Serializable{}
    public record ReservationItemCreateRequest(
            @NotNull long roomId,
            @NotNull OffsetDateTime startAt,
            @NotNull OffsetDateTime endAt) implements Serializable{}
}
