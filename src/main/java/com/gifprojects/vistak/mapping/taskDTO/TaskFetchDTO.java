package com.gifprojects.vistak.mapping.taskDTO;

import com.gifprojects.vistak.model.TaskPriority;
import com.gifprojects.vistak.model.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskFetchDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String desc;

    @NotNull
    private TaskType taskType;

    @NotNull
    private TaskPriority taskPriority;
}
