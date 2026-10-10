package com.event.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;
    private LocalDateTime date;
    private String time;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private Integer maxParticipants;
    private LocalDateTime deadline;
    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    public enum EventStatus {
        UPCOMING, OPEN, CLOSED, ONGOING, COMPLETED
    }
}
