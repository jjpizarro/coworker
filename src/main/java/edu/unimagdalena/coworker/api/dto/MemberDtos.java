package edu.unimagdalena.coworker.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class MemberDtos {
    public record MemberCreateRequest(
            @NotBlank String name,
            @Email @NotBlank String email, MemberProfileDto profile) implements Serializable {}
    public record MemberProfileDto (
            @Size(max = 24) String phone,
            @Size(max = 80) String company) implements Serializable {}
    public record MemberUpdateRequest(String name, String email, MemberProfileDto profile) implements Serializable {}
    public record MemberResponse(Long id, String name, String email, MemberProfileDto profile) implements  Serializable {}

}
