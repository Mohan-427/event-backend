package com.event.system.dto;

import com.event.system.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private User.Role role;
}
