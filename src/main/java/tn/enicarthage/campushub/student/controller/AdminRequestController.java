package tn.enicarthage.campushub.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.student.model.RequestType;
import tn.enicarthage.campushub.student.service.AdminRequestService;
import tn.enicarthage.campushub.student.model.AdminRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final AdminRequestService requestService;

    @PostMapping
    public ResponseEntity<AdminRequest> submit(@RequestBody Map<String, String> body) {
        AdminRequest request = new AdminRequest();
        request.setType(RequestType.valueOf(body.get("type")));
        request.setDescription(body.get("message"));
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