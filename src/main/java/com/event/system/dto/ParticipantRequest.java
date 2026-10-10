package com.event.system.dto;

import lombok.Data;

@Data
public class ParticipantRequest {
    private String name;
    private String email;
    private String phone;
    private String college;
    private String department;
    private String year;
    private Long eventId;
}
