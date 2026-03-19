package com.gifprojects.vistak.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    @NotBlank(message = "Every task has a title.")
    private String title;

    @Column (nullable = false)
    @NotBlank (message = "What do you need to do? Provide a description.")
    private String desc;

    @Column (nullable = false)
    @NotBlank (message = "Choosing a task type keeps everything tidy.")
    private TaskType taskType;

    @Column (nullable = false)
    @NotBlank (message = "Priority is needed for productivity.")
    private TaskPriority taskPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
