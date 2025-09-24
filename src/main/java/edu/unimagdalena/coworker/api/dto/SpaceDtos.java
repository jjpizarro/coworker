package edu.unimagdalena.coworker.api.dto;

import java.io.Serializable;

public class SpaceDtos {
    public record SpaceCreateRequest(String name, String addres) implements Serializable{}
    public record SpaceUpdateRequest(String name, String addres) implements Serializable{}
    public record SpaceResponse(Long id, String name, String addres) implements Serializable{}

}
