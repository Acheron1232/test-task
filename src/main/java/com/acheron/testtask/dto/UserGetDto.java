package com.acheron.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * In this test task userDto has no role,
 * but for further extension of the program
 * or in other cases userDto is an important component
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
