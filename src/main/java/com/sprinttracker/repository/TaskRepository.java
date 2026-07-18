package com.sprinttracker.repository;

import com.sprinttracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findBySprintIdOrderByCreatedAtAsc(Long sprintId);

    @Query("select coalesce(sum(t.storyPoints), 0) from Task t where t.sprintId = :sprintId")
    int sumPlannedPoints(@Param("sprintId") Long sprintId);

    @Query("select coalesce(sum(t.storyPoints), 0) from Task t where t.sprintId = :sprintId and t.status = 'DONE'")
    int sumCompletedPoints(@Param("sprintId") Long sprintId);
}
