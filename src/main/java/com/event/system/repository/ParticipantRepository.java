package com.event.system.repository;

import com.event.system.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}
