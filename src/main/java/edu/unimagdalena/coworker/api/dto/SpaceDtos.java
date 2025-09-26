package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;

public class SpaceDtos {
    public record SpaceCreateRequest(String name, String address) implements Serializable{}
    public record SpaceUpdateRequest(String name, String address) implements Serializable{}
    public record SpaceResponse(Long id, String name, String address) implements Serializable{}

}
