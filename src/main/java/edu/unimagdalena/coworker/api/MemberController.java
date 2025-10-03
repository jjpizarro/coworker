package edu.unimagdalena.coworker.api;

import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService service;

    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberCreateRequest req,
                                                 UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/members/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        var result = service.list(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-email")
    public ResponseEntity<MemberResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody MemberUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}