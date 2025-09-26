package edu.unimagdalena.coworker.services;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.MemberProfile;
import edu.unimagdalena.coworker.domine.repositories.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    MemberRepository repo;
    @InjectMocks MemberServiceImpl service;

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new MemberCreateRequest("Ana", "ana@d.com", new MemberProfileDto("+57", "Uni"));
        when(repo.save(any())).thenAnswer(inv -> {
            Member m = inv.getArgument(0);
            m.setId(11L);
            return m;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(11L);
        assertThat(res.email()).isEqualTo("ana@d.com");
        verify(repo).save(any(Member.class));
    }

    @Test
    void shouldUpdateViaPatch() {
        var entity = Member.builder().id(7L).name("Old").email("old@d.com")
                .profile(MemberProfile.builder().phone("X").company("Y").build()).build();

        when(repo.findById(7L)).thenReturn(Optional.of(entity));

        var mamberUpdatedRequest = new MemberUpdateRequest("New", null, new MemberProfileDto(null, "Z"));
        var memberUpdated = service.update(7L, mamberUpdatedRequest);

        assertThat(memberUpdated.name()).isEqualTo("New");
        assertThat(memberUpdated.profile().company()).isEqualTo("Z");
    }

    @Test
    void shouldListPagedMappingToDto() {
        var page = new PageImpl<>(java.util.List.of(Member.builder().id(1L).name("A").email("a@d.com").build()));
        when(repo.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var members = service.list(PageRequest.of(0, 5));
        assertThat(members.getTotalElements()).isEqualTo(1);
        assertThat(members.getContent().get(0).email()).isEqualTo("a@d.com");
    }
}