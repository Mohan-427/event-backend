package com.event.system.service;

import com.event.system.model.*;
import com.event.system.repository.*;
import com.event.system.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

@Service
public class EventService {
    @Autowired private EventRepository eventRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private ParticipantRepository participantRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Registration registerParticipant(Long eventId, Long participantId) {
        if (registrationRepository.existsByParticipantIdAndEventId(participantId, eventId)) {
            throw new RuntimeException("Already registered for this event");
        }
        Event event = getEventById(eventId);
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        Registration reg = Registration.builder()
                .event(event)
                .participant(participant)
                .registrationDate(LocalDateTime.now())
                .registrationId("REG" + System.currentTimeMillis())
                .build();
        return registrationRepository.save(reg);
    }

    public void markAttendance(Long registrationId, boolean status) {
        Attendance att = attendanceRepository.findByRegistrationId(registrationId)
                .orElseGet(() -> {
                    Registration reg = registrationRepository.findById(registrationId)
                            .orElseThrow(() -> new RuntimeException("Registration not found"));
                    return Attendance.builder().registration(reg).build();
                });
        att.setStatus(status);
        att.setMarkedAt(LocalDateTime.now());
        attendanceRepository.save(att);
    }

    public Map<String, Object> getEventStats(Long eventId) {
        long total = registrationRepository.findByEventId(eventId).size();
        long present = attendanceRepository.findAll().stream()
                .filter(a -> a.getRegistration().getEvent().getId().equals(eventId) && a.isStatus())
                .count();
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("present", present);
        stats.put("absent", total - present);
        stats.put("percentage", total == 0 ? 0 : (present * 100 / total));
        return stats;
    }
}
