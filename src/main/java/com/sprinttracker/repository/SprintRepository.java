package com.sprinttracker.repository;

import com.sprinttracker.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByTeamIdOrderByStartDateDesc(Long teamId);
}
