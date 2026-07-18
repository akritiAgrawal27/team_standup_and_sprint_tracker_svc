package com.sprinttracker.repository;

import com.sprinttracker.entity.StandupEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StandupEntryRepository extends JpaRepository<StandupEntry, Long> {

    List<StandupEntry> findByTeamIdAndEntryDateOrderByCreatedAtAsc(Long teamId, LocalDate entryDate);

    List<StandupEntry> findByUserIdAndSprintIdOrderByEntryDateDesc(Long userId, Long sprintId);

    Optional<StandupEntry> findByUserIdAndSprintIdAndEntryDate(Long userId, Long sprintId, LocalDate entryDate);
}
