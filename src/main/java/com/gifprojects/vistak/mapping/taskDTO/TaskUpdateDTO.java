package com.gifprojects.vistak.mapping.taskDTO;

import com.gifprojects.vistak.model.TaskPriority;
import com.gifprojects.vistak.model.TaskType;
import lombok.Data;

@Data
public class TaskUpdateDTO {
    String title;
    String desc;
    TaskType taskType;
    TaskPriority taskPriority;
}
