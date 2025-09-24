package edu.unimagdalena.coworker.domine.view;

import java.time.OffsetDateTime;

public record ReservationItemView(Long reservationId, Long roomId,
                                  OffsetDateTime startAt,
                                  OffsetDateTime endAt) {
}
