package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.SpaceDtos.*;
import edu.unimagdalena.coworker.domine.entities.Space;
import edu.unimagdalena.coworker.domine.repositories.SpaceRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.SpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository repo;
    private final SpaceMapper mapper;
    @Override public SpaceResponse create(SpaceCreateRequest req) {
        Space saved = repo.save(mapper.toEntity(req));
        return mapper.toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public SpaceResponse get(Long id) {
        return repo.findById(id).map(s -> mapper.toResponse(s))
                .orElseThrow(() -> new NotFoundException("Space %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public Page<SpaceResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(s->mapper.toResponse(s));
    }

    @Override
    public SpaceResponse update(Long id, SpaceUpdateRequest req) {
        var s = repo.findById(id).orElseThrow(() -> new NotFoundException("Space %d not found".formatted(id)));
        mapper.patch(s, req);
        return mapper.toResponse(s);
    }

    @Override public void delete(Long id) { repo.deleteById(id); }
}
