package com.gifprojects.vistak.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Column (nullable = false)
    @NotBlank (message = "Username is mandatory for logging in.")
    private String username;

    @Column (nullable = false)
    @NotBlank (message = "Password is mandatory for logging in.")
    private String password;

    @Column (nullable = false)
    @NotBlank (message = "Alerts and reminders are sent to email. Please provide one!")
    @Email (message = "Email format invalid.")
    private String email;

    @Column (nullable = false)
    private LocalDateTime createdAt;

    @Column
    private GenderType genderType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> taskList;
}
