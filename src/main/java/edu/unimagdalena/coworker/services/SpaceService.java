package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.SpaceDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpaceService {
    SpaceResponse create(SpaceCreateRequest req);
    SpaceResponse get(Long id);
    Page<SpaceResponse> list(Pageable pageable);
    SpaceResponse update(Long id, SpaceUpdateRequest req);
    void delete(Long id);
}
