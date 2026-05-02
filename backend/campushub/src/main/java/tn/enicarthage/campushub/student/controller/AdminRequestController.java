package tn.enicarthage.campushub.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.admin.model.DemandeAdmin;
import tn.enicarthage.campushub.student.records.SubmitRequestDto;
import tn.enicarthage.campushub.student.service.AdminRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final AdminRequestService requestService;

    @PostMapping
    public ResponseEntity<DemandeAdmin> submit(@RequestBody SubmitRequestDto body) {
        return ResponseEntity.ok(requestService.submitRequest(
                DemandeAdmin.Type.valueOf(body.type()), body.message()));
    }

    @GetMapping
    public ResponseEntity<List<DemandeAdmin>> myRequests() {
        return ResponseEntity.ok(requestService.getMyRequests());
    }

    @GetMapping("/my")
    public ResponseEntity<List<DemandeAdmin>> myRequestsAlias() {
        return ResponseEntity.ok(requestService.getMyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeAdmin> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getMyRequest(id));
    }
}