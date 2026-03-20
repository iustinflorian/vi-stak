package com.gifprojects.vistak.mapping.taskDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gifprojects.vistak.model.Task;
import com.gifprojects.vistak.model.TaskPriority;
import com.gifprojects.vistak.model.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String desc;

    @NotNull
    private TaskType taskType;

    @NotNull
    private TaskPriority taskPriority;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;

    @NotNull
    private Long userId;
}
