package com.event.system.dto;

import com.event.system.model.Event;
import com.event.system.model.Participant;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsDto {
    private long totalEvents;
    private long activeEvents;
    private long totalRegistrations;
    private long totalParticipants;
    private long totalPresent;
    private long totalAbsent;
    private List<Event> upcomingEvents;
    private List<Participant> recentRegistrations;
    private Map<String, Long> categoryDistribution;
}
