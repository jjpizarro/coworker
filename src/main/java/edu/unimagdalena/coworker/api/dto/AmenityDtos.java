package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;

public class AmenityDtos {
    public record AmenityCreateRequest(String name) implements Serializable{}
    public record AmenityResponse(Long id,String name) implements Serializable {}
}
