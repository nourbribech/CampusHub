package tn.enicarthage.campushub.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.student.model.Event;
import tn.enicarthage.campushub.student.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getApprovedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> register(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.registerForEvent(id));
    }

    @DeleteMapping("/{id}/register")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.cancelRegistration(id));
    }

    @PostMapping("/submit")
    public ResponseEntity<Event> submit(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.submitEvent(event));
    }

    @GetMapping("/my-registrations")
    public ResponseEntity<List<Event>> myRegistrations() {
        return ResponseEntity.ok(eventService.getMyRegistrations());
    }
}