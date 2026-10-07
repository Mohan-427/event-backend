package com.event.system.repository;

import com.event.system.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByRegistrationId(String registrationId);
    boolean existsByParticipantIdAndEventId(Long participantId, Long eventId);
}
