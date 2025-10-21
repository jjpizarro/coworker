package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
class MemberMapperTest {
    private final MemberMapper mapper = Mappers.getMapper(MemberMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new MemberCreateRequest("Ana", "ana@d.com", new MemberProfileDto("+57","Uni"));
        Member entity = mapper.toEntity(req);

        assertThat(entity.getName()).isEqualTo("Ana");
        assertThat(entity.getEmail()).isEqualTo("ana@d.com");
        assertThat(entity.getProfile().getPhone()).isEqualTo("+57");
    }
    @Test
    void toResponse_shouldMapEntity() {
        var m = Member.builder()
                .id(10L).name("Ana").email("ana@d.com")
                .profile(MemberProfile.builder().phone("+57").company("Uni").build()).build();

        MemberResponse dto = mapper.toResponse(m);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.profile().company()).isEqualTo("Uni");
    }

    @Test
    void patch_shouldIgnoreNullsAndCreateProfileIfNeeded() {
        var entity = Member.builder().id(1L).name("Old").email("old@d.com").build();
        var changes = new MemberUpdateRequest("New", null, new MemberProfileDto(null, "CompanyX"));

        mapper.patch(entity, changes);

        assertThat(entity.getName()).isEqualTo("New");
        assertThat(entity.getEmail()).isEqualTo("old@d.com");
        assertThat(entity.getProfile()).isNotNull();
        assertThat(entity.getProfile().getCompany()).isEqualTo("CompanyX");
    }

}