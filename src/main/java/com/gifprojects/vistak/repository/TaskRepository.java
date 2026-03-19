package com.gifprojects.vistak.repository;

import com.gifprojects.vistak.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getAllByUserId(Long userId);
}