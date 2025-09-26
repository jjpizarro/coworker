package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.MemberDtos;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.MemberProfile;

public class MemberMapper {
    public static Member toEntity(MemberDtos.MemberCreateRequest request){
        var profile = request.profile() == null ? null:
                MemberProfile.builder().phone(request.profile().phone())
                        .company(request.profile().company())
                        .build();
        return Member.builder()
                .name(request.name())
                .email(request.email())
                .profile(profile)
                .build();
    }
    public static MemberDtos.MemberResponse toResponse(Member member){
        var p = member.getProfile();
        var dtoProfile = p == null ? null : new MemberDtos.MemberProfileDto(p.getPhone(), p.getCompany());
        return new MemberDtos.MemberResponse(member.getId(), member.getName(), dtoProfile);
    }
    public static void patch(Member entity, MemberDtos.MemberUpdateRequest request){
        if(request.name() != null) entity.setName(request.name());
        if(request.email() != null) entity.setEmail(request.email());
        if(request.profile() != null){
            var p = entity.getProfile();
            if(p == null){
                p = new MemberProfile();
                entity.setProfile(p);
            }
            if(request.profile().phone() != null) p.setPhone(request.profile().phone());
            if(request.profile().company() != null) p.setCompany(request.profile().company());
        }
    }
}
