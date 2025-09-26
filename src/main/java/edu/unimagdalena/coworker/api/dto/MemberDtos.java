package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;

public class MemberDtos {
    public record MemberCreateRequest(String name, String email, MemberProfileDto profile) implements Serializable {}
    public record MemberProfileDto (String phone, String company) implements Serializable {}
    public record MemberUpdateRequest(String name, String email, MemberProfileDto profile) implements Serializable {}
    public record MemberResponse(Long id, String name, String email, MemberProfileDto profile) implements  Serializable {}

}
