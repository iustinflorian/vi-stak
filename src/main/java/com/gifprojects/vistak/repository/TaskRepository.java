package com.gifprojects.vistak.repository;

import com.gifprojects.vistak.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class TaskRepository implements JpaRepository<Task, Long> { }