package com.event.system.service;

import com.event.system.dto.ParticipantRequest;
import com.event.system.model.Event;
import com.event.system.model.Participant;
import com.event.system.repository.EventRepository;
import com.event.system.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventRepository eventRepository;

    public Participant registerParticipant(ParticipantRequest request) {
        if (request.getEventId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event ID is required");
        }

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (participantRepository.existsByEmailAndEventId(request.getEmail(), request.getEventId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered for this event");
        }

        Participant participant = Participant.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .college(request.getCollege())
                .department(request.getDepartment())
                .year(request.getYear())
                .event(event)
                .attendance(false)
                .build();

        return participantRepository.save(participant);
    }

    public List<Participant> getParticipantsByEvent(Long eventId, String query) {
        if (query != null && !query.trim().isEmpty()) {
            return participantRepository.findByEventIdAndNameContainingIgnoreCaseOrEventIdAndEmailContainingIgnoreCase(
                    eventId, query.trim(), eventId, query.trim());
        }
        return participantRepository.findByEventId(eventId);
    }

    public List<Participant> getAllParticipants(String query) {
        if (query != null && !query.trim().isEmpty()) {
            return participantRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    query.trim(), query.trim());
        }
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
    }

    public Participant updateParticipant(Long id, ParticipantRequest details) {
        Participant p = getParticipantById(id);
        if (details.getName() != null) p.setName(details.getName());
        if (details.getEmail() != null) p.setEmail(details.getEmail());
        if (details.getPhone() != null) p.setPhone(details.getPhone());
        if (details.getCollege() != null) p.setCollege(details.getCollege());
        if (details.getDepartment() != null) p.setDepartment(details.getDepartment());
        if (details.getYear() != null) p.setYear(details.getYear());
        return participantRepository.save(p);
    }

    public void deleteParticipant(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found");
        }
        participantRepository.deleteById(id);
    }

    public Participant updateAttendance(Long id, boolean status) {
        Participant p = getParticipantById(id);
        p.setAttendance(status);
        return participantRepository.save(p);
    }

    public Map<String, Object> getEventAttendanceStats(Long eventId) {
        long total = participantRepository.countByEventId(eventId);
        long present = participantRepository.countByEventIdAndAttendance(eventId, true);
        long absent = participantRepository.countByEventIdAndAttendance(eventId, false);
        long percentage = total == 0 ? 0 : (present * 100 / total);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("present", present);
        stats.put("absent", absent);
        stats.put("percentage", percentage);
        return stats;
    }
}
