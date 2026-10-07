package com.event.system.controller;

import com.event.system.model.*;
import com.event.system.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired private EventService service;

    @GetMapping("/public")
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(service.getAllEvents());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventById(id));
    }

    @PostMapping("/admin")
    public ResponseEntity<Event> create(@RequestBody Event event) {
        return ResponseEntity.ok(service.createEvent(event));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Registration> register(@PathVariable Long id, @RequestParam Long participantId) {
        return ResponseEntity.ok(service.registerParticipant(id, participantId));
    }

    @PatchMapping("/{regId}/attendance")
    public ResponseEntity<Void> markAttendance(@PathVariable Long regId, @RequestParam boolean status) {
        service.markAttendance(regId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventStats(id));
    }
}
