package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponse create(MemberCreateRequest req);
    MemberResponse get(Long id);
    MemberResponse getByEmail(String email);
    Page<MemberResponse> list(Pageable pageable);
    MemberResponse update(Long id, MemberUpdateRequest req);
    void delete(Long id);
}
