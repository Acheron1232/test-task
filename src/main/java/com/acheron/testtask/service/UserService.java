package com.acheron.testtask.service;

import com.acheron.testtask.dto.UserCreateUpdateDto;
import com.acheron.testtask.entity.User;
import com.acheron.testtask.exception.UserNotFoundException;
import com.acheron.testtask.mapper.UserMapper;
import com.acheron.testtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${permitted-age}")
    private Integer age;

    public ResponseEntity<?> save(UserCreateUpdateDto userDto) {
        if (userAgeVerification(userDto)) {
            User user = findByEmail(userDto.getEmail()).orElse(null);
            if (user == null) {
                User savedUser = userRepository.save(userMapper.mapUserDtoToUser(userDto));
                return ResponseEntity.ok(userMapper.mapUserToUserGetDto(savedUser));
            } else {
                log.debug("User with Email: " + userDto.getEmail() + " already exists");
                return ResponseEntity.badRequest().body("User already exists");
            }
        } else return ResponseEntity.badRequest().body("Age must be over " + age);
    }

    public ResponseEntity<?> update(Long id, UserCreateUpdateDto userDto) {
        if (userAgeVerification(userDto)) {
            User user = findById(id).orElse(null);
            if (user != null) {
                User savedUser = userRepository.save(userMapper.merge(userDto, user));
                return ResponseEntity.ok(userMapper.mapUserToUserGetDto(savedUser));
            } else {
                log.debug("User with Id: " + id + " not found");
                return ResponseEntity.badRequest().body("User with Id: " + id + " not found");
            }
        } else return ResponseEntity.badRequest().body("Age must be over " + age);
    }

    public ResponseEntity<?> delete(Long id) {
        User user = findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok(userMapper.mapUserToUserGetDto(user));
        } else {
            log.debug("User with Id: " + id + " not found");
            return ResponseEntity.badRequest().body("User with Id: " + id + " not found");
        }
    }

    public ResponseEntity<?> searchByBirthDateRange(LocalDate start, LocalDate end) {
        if (end.isAfter(start)) {
            return ResponseEntity.ok(userMapper.mapUserListToUserGetDtoList(userRepository.findUsersByBirthDateBetween(start, end)));
        } else {
            return ResponseEntity.badRequest().body("The end date must be greater than the start date");
        }
    }

    public boolean userAgeVerification(UserCreateUpdateDto userDto) {
        return userDto.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= age;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findUserById(id);
    }
}
