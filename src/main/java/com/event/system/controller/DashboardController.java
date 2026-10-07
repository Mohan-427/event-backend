package com.event.system.controller;

import com.event.system.dto.DashboardStatsDto;
import com.event.system.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    @Autowired private DashboardService dashboardService;

    @GetMapping("/admin")
    public ResponseEntity<DashboardStatsDto> getAdminStats() {
        return ResponseEntity.ok(dashboardService.getAdminStats());
    }

    @GetMapping("/organizer/{id}")
    public ResponseEntity<Map<String, Object>> getOrganizerStats(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getOrganizerStats(id));
    }
}
