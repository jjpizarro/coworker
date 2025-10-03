package edu.unimagdalena.coworker.api;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.services.ReservationService;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationResponse> createDraft(@Valid @RequestBody ReservationCreateRequest req,
                                                                           UriComponentsBuilder uriBuilder) {
        var body = service.createDraft(req);
        var location = uriBuilder.path("/api/reservations/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> listByMember(@RequestParam String email,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        var p = service.listByMember(email, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Integer> cleanup(@RequestParam("before")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime before) {
        int removed = service.deleteOlderThan(before);
        return ResponseEntity.ok(removed);
    }
}
