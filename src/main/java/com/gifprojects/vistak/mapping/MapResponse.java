package com.gifprojects.vistak.mapping;

import com.gifprojects.vistak.mapping.taskDTO.TaskFetchDTO;
import com.gifprojects.vistak.mapping.userDTO.UserFetchDTO;
import com.gifprojects.vistak.model.Task;
import com.gifprojects.vistak.model.User;

import java.util.List;

public class MapResponse {
    public static UserFetchDTO mapUserResponse(User user){
        UserFetchDTO response = new UserFetchDTO();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

    public static TaskFetchDTO mapTaskResponse(Task task){
        TaskFetchDTO response = new TaskFetchDTO();
        response.setTitle(task.getTitle());
        response.setDesc(task.getDesc());
        response.setTaskType(task.getTaskType());
        response.setTaskPriority(task.getTaskPriority());

        return response;
    }

    public static List<TaskFetchDTO> mapTaskListResponse(List<Task> tasks){
        return tasks.stream()
                .map(MapResponse::mapTaskResponse)
                .toList();
    }

}
