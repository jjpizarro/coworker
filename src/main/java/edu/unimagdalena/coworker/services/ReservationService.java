package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    ReservationResponse createDraft(ReservationCreateRequest req);
    ReservationResponse get(Long id);
    Page<ReservationResponse> listByMember(String email, Pageable pageable);
    int deleteOlderThan(OffsetDateTime threshold);
    void delete(Long id);
}
