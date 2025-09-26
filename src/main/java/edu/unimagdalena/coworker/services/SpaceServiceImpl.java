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

    @Override public SpaceResponse create(SpaceCreateRequest req) {
        Space saved = repo.save(SpaceMapper.toEntity(req));
        return SpaceMapper.toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public SpaceResponse get(Long id) {
        return repo.findById(id).map(SpaceMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Space %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public Page<SpaceResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(SpaceMapper::toResponse);
    }

    @Override
    public SpaceResponse update(Long id, SpaceUpdateRequest req) {
        var s = repo.findById(id).orElseThrow(() -> new NotFoundException("Space %d not found".formatted(id)));
        SpaceMapper.patch(s, req);
        return SpaceMapper.toResponse(s);
    }

    @Override public void delete(Long id) { repo.deleteById(id); }
}
