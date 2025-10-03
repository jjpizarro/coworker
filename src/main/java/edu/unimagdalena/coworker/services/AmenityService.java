package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.AmenityResponse;
import edu.unimagdalena.coworker.api.dto.AmenityDtos.AmenityCreateRequest;
import java.util.List;

public interface AmenityService {
    AmenityResponse create(AmenityCreateRequest req);
    AmenityResponse get(Long id);
    List<AmenityResponse> list();
    void delete(Long id);
}
