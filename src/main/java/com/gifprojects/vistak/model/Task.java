package com.gifprojects.vistak.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "task_id", updatable = false, nullable = false)
    private Long id;

    @Column (nullable = false)
    @NotBlank(message = "Every task has a title.")
    private String title;

    @Column (nullable = false, length = 1000)
    @NotBlank (message = "What do you need to do? Provide a description.")
    private String desc;

    @Enumerated (EnumType.STRING)
    @Column (name = "type", nullable = false)
    @NotNull(message = "Choosing a task type keeps everything tidy.")
    private TaskType taskType;

    @Enumerated (EnumType.STRING)
    @Column (name = "priority", nullable = false)
    @NotNull (message = "Priority is needed for productivity.")
    private TaskPriority taskPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull (message = "A task must be associated with an user.")
    private User user;

    @Column (name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
