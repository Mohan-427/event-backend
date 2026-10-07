package com.event.system.service;

import com.event.system.model.Participant;
import com.event.system.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    public Participant registerParticipant(Participant participant) {
        return repository.save(participant);
    }

    public List<Participant> getAllParticipants(String query) {
        if (query == null || query.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    public Participant getParticipantById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public Participant updateParticipant(Long id, Participant details) {
        Participant p = getParticipantById(id);
        p.setName(details.getName());
        p.setEmail(details.getEmail());
        p.setPhone(details.getPhone());
        p.setCollege(details.getCollege());
        p.setEvent(details.getEvent());
        p.setDepartment(details.getDepartment());
        p.setYear(details.getYear());
        return repository.save(p);
    }

    public void deleteParticipant(Long id) {
        repository.deleteById(id);
    }

    public Participant updateAttendance(Long id, boolean status) {
        Participant p = getParticipantById(id);
        p.setAttendance(status);
        return repository.save(p);
    }

    public Map<String, Long> getDashboardStats() {
        List<Participant> all = repository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalParticipants", (long) all.size());
        stats.put("presentParticipants", all.stream().filter(Participant::isAttendance).count());
        stats.put("absentParticipants", all.stream().filter(p -> !p.isAttendance()).count());
        return stats;
    }
}
