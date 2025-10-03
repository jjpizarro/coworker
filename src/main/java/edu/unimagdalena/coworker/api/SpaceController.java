package edu.unimagdalena.coworker.api;

import edu.unimagdalena.coworker.api.dto.SpaceDtos.*;
import edu.unimagdalena.coworker.services.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
@Validated
public class SpaceController {

    private final SpaceService service;
    @PostMapping
    public ResponseEntity<SpaceResponse> create(@Valid @RequestBody SpaceCreateRequest req,
                                                UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/spaces/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<SpaceResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.list(PageRequest.of(page, size, Sort.by("id").ascending())));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SpaceResponse> update(@PathVariable Long id,
                                                @RequestBody SpaceUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
