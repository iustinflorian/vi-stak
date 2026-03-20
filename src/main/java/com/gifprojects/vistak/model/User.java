package com.gifprojects.vistak.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="users")
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Column (nullable = false, unique = true)
    @NotBlank (message = "Username is mandatory for logging in.")
    private String username;

    @Column (nullable = false)
    @NotBlank (message = "Password is mandatory for logging in.")
    private String password;

    @Column (nullable = false, unique = true)
    @NotBlank (message = "Alerts and reminders are sent to email. Please provide one!")
    @Email (message = "Email format invalid.")
    private String email;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Enumerated (EnumType.STRING)
    @Column (name = "gender")
    private GenderType genderType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> taskList;
}
