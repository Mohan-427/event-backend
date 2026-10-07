package com.event.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String name;
    private String email;
    private String phone;
    private String college;
    private String department;
    private String year;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
