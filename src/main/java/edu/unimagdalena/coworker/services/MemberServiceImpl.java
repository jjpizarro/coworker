package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.repositories.MemberRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository repo;

    @Override public MemberResponse create(MemberCreateRequest req) {
        Member memberSaved = repo.save(MemberMapper.toEntity(req));
        return MemberMapper.toResponse(memberSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse get(Long id) {
        return repo.findById(id).map(MemberMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Member %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public MemberResponse getByEmail(String email) {
        return repo.findByEmailIgnoreCase(email).map(MemberMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Member email %s not found".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(MemberMapper::toResponse);
    }

    @Override
    public MemberResponse update(Long id, MemberUpdateRequest req) {
        var m = repo.findById(id).orElseThrow(() -> new NotFoundException("Member %d not found".formatted(id)));
        MemberMapper.patch(m, req);
        return MemberMapper.toResponse(m);
    }

    @Override public void delete(Long id) { repo.deleteById(id); }
}
