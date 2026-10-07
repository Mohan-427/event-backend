package com.event.system.controller;

import com.event.system.model.Participant;
import com.event.system.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "*")
public class ParticipantController {
    @Autowired
    private ParticipantService service;

    @PostMapping
    public ResponseEntity<Participant> register(@RequestBody Participant participant) {
        return ResponseEntity.ok(service.registerParticipant(participant));
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAll(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(service.getAllParticipants(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getParticipantById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> update(@PathVariable Long id, @RequestBody Participant participant) {
        return ResponseEntity.ok(service.updateParticipant(id, participant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/attendance")
    public ResponseEntity<Participant> updateAttendance(@PathVariable Long id, @RequestParam boolean status) {
        return ResponseEntity.ok(service.updateAttendance(id, status));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }
}
