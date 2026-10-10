package com.event.system.service;

import com.event.system.model.Event;
import com.event.system.repository.EventRepository;
import com.event.system.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    public Event createEvent(Event event) {
        if (event.getStatus() == null) {
            event.setStatus(Event.EventStatus.OPEN);
        }
        event.setActive(true);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);
        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setCategory(eventDetails.getCategory());
        event.setDate(eventDetails.getDate());
        event.setTime(eventDetails.getTime());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        event.setVenue(eventDetails.getVenue());
        event.setMaxParticipants(eventDetails.getMaxParticipants());
        event.setDeadline(eventDetails.getDeadline());
        event.setBannerUrl(eventDetails.getBannerUrl());
        if (eventDetails.getStatus() != null) {
            event.setStatus(eventDetails.getStatus());
        }
        event.setActive(eventDetails.isActive());
        if (eventDetails.getOrganizer() != null) {
            event.setOrganizer(eventDetails.getOrganizer());
        }
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        eventRepository.deleteById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public Map<String, Object> getEventStats(Long eventId) {
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
