package edu.unimagdalena.coworker.services.mapper;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.domine.entities.Member;
import edu.unimagdalena.coworker.domine.entities.MemberProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IMemberMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target= "profile", source = "profile")
    Member toEntity(MemberCreateRequest req);
    MemberProfile toEntity(MemberProfileDto dto);
    @Mapping(target = "profile", source = "profile")
    MemberResponse toResponse(Member entity);

    MemberProfileDto toDto(MemberProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Member target, MemberUpdateRequest changes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchProfile(@MappingTarget MemberProfile target, MemberProfileDto changes);

    // Post-patch para crear el profile si viene en null y el request lo trae
    @AfterMapping
    default void ensureProfile(@MappingTarget Member target, MemberUpdateRequest changes) {
        if (changes != null && changes.profile() != null) {
            if (target.getProfile() == null) {
                target.setProfile(new MemberProfile());
            }
            patchProfile(target.getProfile(), changes.profile());
        }
    }
}
