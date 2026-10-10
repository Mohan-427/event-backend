package com.event.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participants", uniqueConstraints = {
    @UniqueConstraint(name = "uk_participant_event_email", columnNames = {"email", "event_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    @Builder.Default
    private boolean attendance = false;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
    }
}
