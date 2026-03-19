package com.gifprojects.vistak.controller;


import com.gifprojects.vistak.mapping.MapResponse;
import com.gifprojects.vistak.mapping.taskDTO.TaskCreateDTO;
import com.gifprojects.vistak.mapping.taskDTO.TaskFetchDTO;
import com.gifprojects.vistak.mapping.taskDTO.TaskUpdateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserCreateDTO;
import com.gifprojects.vistak.mapping.userDTO.UserFetchDTO;
import com.gifprojects.vistak.mapping.userDTO.UserUpdateDTO;
import com.gifprojects.vistak.model.Task;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.service.TaskService;
import com.gifprojects.vistak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/newtask")
    public ResponseEntity<Void> createUser(
            @RequestBody TaskCreateDTO data,
            @RequestHeader("X-Auth-User-Id") Long userId){
        taskService.createTask(data, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskFetchDTO>> fetchAllTasks(@RequestHeader("X-Auth-User-Id") Long userId){
        return new ResponseEntity<>(
                MapResponse.mapTaskListResponse(taskService.fetchAllTasks(userId)),
                HttpStatus.OK);
    }

    @GetMapping("/my-tasks/{taskId}")
    public ResponseEntity<TaskFetchDTO> fetchTask(
            @PathVariable Long taskId,
            @RequestHeader("X-Auth-User-Id") Long userId){
        return new ResponseEntity<>(
                MapResponse.mapTaskResponse(taskService.fetchTask(taskId, userId)),
                HttpStatus.OK);
    }

    @PatchMapping("/my-tasks/{taskId}/update")
    public ResponseEntity<Void> updateUser(
            @RequestBody TaskUpdateDTO data,
            @PathVariable Long taskId,
            @RequestHeader("X-Auth-User-Id") Long userId){
        taskService.updateTask(data, taskId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/my-tasks/{taskId}/delete")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long taskId,
            @RequestHeader("X-Auth-User-Id") Long userId){
        taskService.deleteTask(taskId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}