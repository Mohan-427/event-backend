package com.event.system;

import com.event.system.controller.EventController;
import com.event.system.model.User;
import com.event.system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EventRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventRegistrationApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     EventController eventController) {
        return args -> {
            // 1. Create Default Admin Account
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .name("System Administrator")
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@college.edu")
                        .role(User.Role.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println(">>> Default Admin created: admin / admin123");
            }

            // 2. Create Default Organizer Account
            if (userRepository.findByUsername("organizer").isEmpty()) {
                User organizer = User.builder()
                        .name("Event Coordinator")
                        .username("organizer")
                        .password(passwordEncoder.encode("org123"))
                        .email("org@college.edu")
                        .role(User.Role.ORGANIZER)
                        .build();
                userRepository.save(organizer);
                System.out.println(">>> Default Organizer created: organizer / org123");
            }

            // 3. Automatically populate demo events
            try {
                eventController.setupDemo();
                System.out.println(">>> Demo events populated successfully!");
            } catch (Exception e) {
                System.err.println(">>> Error populating demo events: " + e.getMessage());
            }
        };
    }
}
