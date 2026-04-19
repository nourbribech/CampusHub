package tn.enicarthage.campushub.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final AdminRequestService requestService;

    @PostMapping
    public ResponseEntity<AdminRequest> submit(@RequestBody AdminRequest request) {
        return ResponseEntity.ok(requestService.submitRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<AdminRequest>> myRequests() {
        return ResponseEntity.ok(requestService.getMyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminRequest> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getMyRequest(id));
    }
}