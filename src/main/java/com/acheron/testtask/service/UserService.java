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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${permitted-age}")
    private Integer age;

    public ResponseEntity<String> save(UserCreateUpdateDto userDto) {
        if ((LocalDate.now().getYear() - userDto.getBirthDate().getYear()) >= age) {
            try {
                User user = findByEmail(userDto.getEmail());
                log.debug("User with Email: " + userDto.getEmail() + " already exists");
                return ResponseEntity.badRequest().body("User already exists");
            } catch (UserNotFoundException e) {
                userRepository.save(userMapper.mapToUser(userDto));
                return ResponseEntity.ok("Success");
            }
        } else return ResponseEntity.badRequest().body("Age must be over " + age);
    }

    public ResponseEntity<String> update(Long id, UserCreateUpdateDto userDto) {
        if ((LocalDate.now().getYear() - userDto.getBirthDate().getYear()) >= age) {
            try {
                User user = findById(id);
                userRepository.save(userMapper.merge(userDto, user));
                return ResponseEntity.ok("Success");
            } catch (UserNotFoundException e) {
                log.debug("User with Id: " + id + " not found");
                return ResponseEntity.badRequest().body("User with Id: " + id + " not found");
            }
        } else return ResponseEntity.badRequest().body("Age must be over " + age);
    }

    public ResponseEntity<String> delete(Long id) {
        try {
            User user = findById(id);
            userRepository.delete(user);
            return ResponseEntity.ok("Success");
        } catch (UserNotFoundException e) {
            log.debug("User with Id: " + id + " not found");
            return ResponseEntity.badRequest().body("User with Id: " + id + " not found");
        }
    }

    public ResponseEntity<?> searchByBirthDateRange(LocalDate start, LocalDate end) {
        if (end.isAfter(start)) {
            return ResponseEntity.ok(userMapper.mapToUserGetDtoList(userRepository.findUsersByBirthDateBetween(start, end)));
        } else {
            return ResponseEntity.badRequest().body("the end date must be greater than the start date");
        }
    }

    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
