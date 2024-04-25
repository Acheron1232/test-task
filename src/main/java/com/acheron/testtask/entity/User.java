package com.acheron.testtask.entity;

import com.acheron.testtask.validation.BirthDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "first name cannot be empty")
    private String firstName;
    @NotBlank(message = "last name cannot be empty")
    private String lastName;
    @Email
    @Column(unique = true)
    @NotBlank(message = "email cannot be empty")
    private String email;
    @BirthDate(message = "set a valid date of birth")
    @NotNull
    private LocalDate birthDate;
    private String address;
    @Column(unique = true)
    private String phoneNumber;
}
