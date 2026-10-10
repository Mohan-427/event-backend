package com.event.system.repository;

import com.event.system.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByEventId(Long eventId);

    List<Participant> findByEventIdAndNameContainingIgnoreCaseOrEventIdAndEmailContainingIgnoreCase(
        Long eventId1, String name, Long eventId2, String email);

    List<Participant> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    Optional<Participant> findByEmailAndEventId(String email, Long eventId);

    boolean existsByEmailAndEventId(String email, Long eventId);

    long countByEventId(Long eventId);

    long countByEventIdAndAttendance(Long eventId, boolean attendance);

    long countByAttendance(boolean attendance);

    List<Participant> findTop5ByOrderByRegistrationDateDesc();
}
