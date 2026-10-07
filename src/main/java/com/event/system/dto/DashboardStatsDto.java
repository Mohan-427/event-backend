package com.event.system.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardStatsDto {
    private long totalEvents;
    private long activeEvents;
    private long totalParticipants;
    private long todayRegistrations;
    private long totalPresent;
    private long totalAbsent;
    private Map<String, Long> categoryDistribution;
}
