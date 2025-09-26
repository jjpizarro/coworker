package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.Reservation;
import edu.unimagdalena.coworker.domine.repositories.MemberRepository;
import edu.unimagdalena.coworker.domine.repositories.ReservationRepository;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service @RequiredArgsConstructor @Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;
    private final MemberRepository memberRepo;

    @Override
    public ReservationResponse createDraft(ReservationCreateRequest req) {
        Member m = memberRepo.findById(req.memberId())
                .orElseThrow(() -> new NotFoundException("Member %d not found".formatted(req.memberId())));
        Reservation r = repo.save(Reservation.builder().member(m).createdAt(OffsetDateTime.now()).build());
        return ReservationMapper.toResponse(r);
    }

    @Override @Transactional(readOnly = true)
    public ReservationResponse get(Long id) {
        var r = repo.fetchGraphById(id);
        return ReservationMapper.toResponse(r);
    }

    @Override @Transactional(readOnly = true)
    public Page<ReservationResponse> listByMember(String email, Pageable pageable) {
        return repo.findByMember_EmailIgnoreCaseOrderByCreatedAtDesc(email, pageable)
                .map(ReservationMapper::toResponse);
    }

    @Override
    public int deleteOlderThan(OffsetDateTime threshold) { return repo.deleteOlderThan(threshold); }

    @Override public void delete(Long id) { repo.deleteById(id); }
}
