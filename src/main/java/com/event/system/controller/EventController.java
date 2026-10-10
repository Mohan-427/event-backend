package com.event.system.controller;

import com.event.system.model.Event;
import com.event.system.model.User;
import com.event.system.repository.UserRepository;
import com.event.system.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired
    private EventService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/setup-demo")
    public ResponseEntity<String> setupDemo() {
        if (!service.getAllEvents().isEmpty()) {
            return ResponseEntity.ok("Events already exist.");
        }

        User organizer = userRepository.findByUsername("organizer").orElse(null);

        Event e1 = Event.builder()
                .name("National Level Hackathon 2026")
                .description("A 36-hour non-stop coding challenge to solve real-world problems using AI and Cloud.")
                .category("Hackathon")
                .date(LocalDateTime.now().plusDays(15))
                .time("09:00 AM")
                .startTime(LocalDateTime.now().plusDays(15).withHour(9).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(16).withHour(18).withMinute(0))
                .venue("Main Engineering Block, Hall A")
                .maxParticipants(200)
                .deadline(LocalDateTime.now().plusDays(10))
                .bannerUrl("/images/hackathon.jpg")
                .status(Event.EventStatus.OPEN)
                .active(true)
                .organizer(organizer)
                .build();

        Event e2 = Event.builder()
                .name("AI & ML Workshop")
                .description("Deep dive into Neural Networks and Large Language Models with industry experts.")
                .category("Workshop")
                .date(LocalDateTime.now().plusDays(20))
                .time("10:00 AM")
                .startTime(LocalDateTime.now().plusDays(20).withHour(10).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(20).withHour(16).withMinute(0))
                .venue("CS Seminar Hall")
                .maxParticipants(60)
                .deadline(LocalDateTime.now().plusDays(12))
                .bannerUrl("/images/workshop.jpg")
                .status(Event.EventStatus.OPEN)
                .active(true)
                .organizer(organizer)
                .build();

        Event e3 = Event.builder()
                .name("Annual Cultural Fest 2026")
                .description("The biggest celebration of art, music, drama, and dance in the university.")
                .category("Cultural")
                .date(LocalDateTime.now().plusDays(30))
                .time("06:00 PM")
                .startTime(LocalDateTime.now().plusDays(30).withHour(18).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(31).withHour(22).withMinute(0))
                .venue("Open Air Theatre")
                .maxParticipants(1000)
                .deadline(LocalDateTime.now().plusDays(15))
                .bannerUrl("/images/cultural_fest.jpg")
                .status(Event.EventStatus.OPEN)
                .active(true)
                .organizer(organizer)
                .build();

        service.createEvent(e1);
        service.createEvent(e2);
        service.createEvent(e3);

        return ResponseEntity.ok("Demo events added successfully!");
    }

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

    @PutMapping("/admin/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(service.updateEvent(id, event));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventStats(id));
    }
}
