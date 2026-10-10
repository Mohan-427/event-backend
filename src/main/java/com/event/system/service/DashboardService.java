package com.event.system.service;

import com.event.system.dto.DashboardStatsDto;
import com.event.system.model.Event;
import com.event.system.repository.EventRepository;
import com.event.system.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    public DashboardStatsDto getAdminStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        List<Event> events = eventRepository.findAll();
        stats.setTotalEvents(events.size());
        stats.setActiveEvents(events.stream().filter(Event::isActive).count());

        long totalRegistrations = participantRepository.count();
        stats.setTotalRegistrations(totalRegistrations);
        stats.setTotalParticipants(totalRegistrations);

        long totalPresent = participantRepository.countByAttendance(true);
        long totalAbsent = participantRepository.countByAttendance(false);

        stats.setTotalPresent(totalPresent);
        stats.setTotalAbsent(totalAbsent);

        // Upcoming events (up to 5)
        stats.setUpcomingEvents(events.stream().limit(5).collect(Collectors.toList()));

        // Recent registrations (up to 5)
        stats.setRecentRegistrations(participantRepository.findTop5ByOrderByRegistrationDateDesc());

        // Category distribution
        Map<String, Long> dist = new HashMap<>();
        events.forEach(e -> {
            if (e.getCategory() != null) {
                dist.merge(e.getCategory(), 1L, Long::sum);
            }
        });
        stats.setCategoryDistribution(dist);

        return stats;
    }

    public Map<String, Object> getOrganizerStats(Long organizerId) {
        Map<String, Object> stats = new HashMap<>();
        List<Event> events = eventRepository.findByOrganizerId(organizerId);

        stats.put("totalEvents", (long) events.size());

        long totalParticipants = 0;
        long present = 0;
        long absent = 0;

        List<Map<String, Object>> managedEvents = new ArrayList<>();

        for (Event e : events) {
            long regCount = participantRepository.countByEventId(e.getId());
            long presCount = participantRepository.countByEventIdAndAttendance(e.getId(), true);
            long absCount = participantRepository.countByEventIdAndAttendance(e.getId(), false);

            totalParticipants += regCount;
            present += presCount;
            absent += absCount;

            Map<String, Object> eventMap = new HashMap<>();
            eventMap.put("id", e.getId());
            eventMap.put("name", e.getName());
            eventMap.put("date", e.getDate());
            eventMap.put("venue", e.getVenue());
            eventMap.put("participants", regCount);
            eventMap.put("present", presCount);
            eventMap.put("absent", absCount);
            eventMap.put("attendancePercentage", regCount == 0 ? 0 : (presCount * 100 / regCount));
            managedEvents.add(eventMap);
        }

        stats.put("totalParticipants", totalParticipants);
        stats.put("present", present);
        stats.put("absent", absent);
        stats.put("attendancePercentage", totalParticipants == 0 ? 0 : (present * 100 / totalParticipants));
        stats.put("managedEvents", managedEvents);

        return stats;
    }
}
