package edu.unimagdalena.coworker.api;

import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.services.ReservationItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/reservations/{reservationId}/items")
@RequiredArgsConstructor
@Validated
public class ReservationItemController {

    private final ReservationItemService service;
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<ReservationItemResponse> add(@PathVariable Long reservationId,
                                                       @Valid @RequestBody ReservationItemCreateRequest req,
                                                       UriComponentsBuilder uriBuilder) {
        var body = service.addItem(reservationId, req);
        var location = uriBuilder
                .path("/api/reservations/{reservationId}/items/{itemId}")
                .buildAndExpand(reservationId, body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    // Solo ADMIN o MANAGER listan por correo
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<List<ReservationItemResponse>> list(@PathVariable Long reservationId) {
        return ResponseEntity.ok(service.listByReservation(reservationId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long reservationId, @PathVariable Long itemId) {
        service.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
