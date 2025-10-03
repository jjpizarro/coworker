package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.AmenityDtos.*;
import edu.unimagdalena.coworker.domine.repositories.AmenityRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.AmenityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository repo;

    @Override public AmenityResponse create(AmenityCreateRequest req) {
        return AmenityMapper.toResponse(repo.save(AmenityMapper.toEntity(req)));
    }

    @Override @Transactional(readOnly = true)
    public AmenityResponse get(Long id) {
        return repo.findById(id).map(AmenityMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Amenity %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public List<AmenityResponse> list() {
        return repo.findAll().stream().map(AmenityMapper::toResponse).toList();
    }

    @Override public void delete(Long id) { repo.deleteById(id); }
}
