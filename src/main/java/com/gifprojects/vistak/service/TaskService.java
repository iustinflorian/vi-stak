package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.taskDTO.TaskCreateDTO;
import com.gifprojects.vistak.mapping.taskDTO.TaskUpdateDTO;
import com.gifprojects.vistak.model.Task;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.TaskRepository;
import com.gifprojects.vistak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public void createTask(TaskCreateDTO data, Long userId){
        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found"));

        validateDeadline(data.getDeadline());

        Task newTask = Task.builder()
                .title(data.getTitle())
                .desc(data.getDesc())
                .taskType(data.getTaskType())
                .taskPriority(data.getTaskPriority())
                .deadline(data.getDeadline())
                .user(newUser)
                .build();
        taskRepository.save(newTask);

        newUser.getTaskList().add(newTask);
    }

    public List<Task> fetchAllTasks(Long userId){
        return taskRepository.getAllByUserId(userId);
    }

    public Task fetchTask(Long taskId, Long userId){
        Task newTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("not found"));

        if (!newTask.getUser().getId().equals(userId)){
            throw new RuntimeException("unauthorized");
        }
        return newTask;
    }

    public void updateTask(TaskUpdateDTO data, Long taskId, Long userId){
        Task currTask = fetchTask(taskId, userId);

        if(data.getTitle() != null && !data.getTitle().isBlank()){
            currTask.setTitle(data.getTitle());
        }
        if(data.getDesc() != null && !data.getDesc().isBlank()){
            currTask.setDesc(data.getDesc());
        }
        if(data.getTaskType() != null){
            currTask.setTaskType(data.getTaskType());
        }
        if(data.getTaskPriority() != null){
            currTask.setTaskPriority(data.getTaskPriority());
        }
    }

    public void deleteTask(Long taskId, Long userId){
        Task currTask = fetchTask(taskId, userId);
        taskRepository.delete(currTask);
    }

    // deadline business logic; soon to be decoupled from TaskService
    public void validateDeadline(LocalDateTime deadline){
        if (deadline != null && deadline.isBefore(LocalDateTime.now())){
            throw new RuntimeException("invalid deadline");
        }
    }

}
