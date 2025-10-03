package edu.unimagdalena.coworker.api;

import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.services.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class RoomController {

    private final RoomService service;

    @PostMapping("/spaces/{spaceId}/rooms")
    public ResponseEntity<RoomResponse> create(@PathVariable Long spaceId,
                                               @Valid @RequestBody RoomCreateRequest req,
                                               UriComponentsBuilder uriBuilder) {
        var body = service.create(spaceId, req);
        var location = uriBuilder.path("/api/rooms/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/spaces/{spaceId}/rooms")
    public ResponseEntity<List<RoomResponse>> listBySpace(@PathVariable Long spaceId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        var p = service.listBySpace(spaceId);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/rooms/{roomId}/amenities/{amenityId}")
    public ResponseEntity<RoomResponse> addAmenity(@PathVariable Long roomId, @PathVariable Long amenityId) {
        return ResponseEntity.ok(service.addAmenity(roomId, amenityId));
    }

    @DeleteMapping("/rooms/{roomId}/amenities/{amenityId}")
    public ResponseEntity<RoomResponse> removeAmenity(@PathVariable Long roomId, @PathVariable Long amenityId) {
        return ResponseEntity.ok(service.removeAmenity(roomId, amenityId));
    }

    @GetMapping("/spaces/{spaceId}/rooms/available")
    public ResponseEntity<List<RoomResponse>> available(@PathVariable Long spaceId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end,
                                                        @RequestParam(required = false) Integer minCapacity) {
        return ResponseEntity.ok(service.findAvailable(spaceId, start, end, minCapacity));
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
