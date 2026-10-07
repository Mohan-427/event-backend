package com.event.system.service;

import com.event.system.dto.DashboardStatsDto;
import com.event.system.repository.AttendanceRepository;
import com.event.system.repository.EventRepository;
import com.event.system.repository.RegistrationRepository;
import com.event.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired private EventRepository eventRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private UserRepository userRepository;

    public DashboardStatsDto getAdminStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        List<com.event.system.model.Event> events = eventRepository.findAll();
        stats.setTotalEvents(events.size());
        stats.setActiveEvents(events.stream().filter(e -> e.isActive()).count());

        stats.setTotalParticipants(userRepository.count());

        // Category distribution
        Map<String, Long> dist = new HashMap<>();
        events.forEach(e -> dist.merge(e.getCategory(), 1L, Long::sum));
        stats.setCategoryDistribution(dist);

        return stats;
    }

    public Map<String, Object> getOrganizerStats(Long organizerId) {
        Map<String, Object> stats = new HashMap<>();
        List<com.event.system.model.Event> events = eventRepository.findByOrganizerId(organizerId);

        stats.put("totalEvents", (long) events.size());

        long totalParticipants = 0;
        for (com.event.system.model.Event e : events) {
            totalParticipants += registrationRepository.findByEventId(e.getId()).size();
        }
        stats.put("totalParticipants", totalParticipants);

        return stats;
    }
}
