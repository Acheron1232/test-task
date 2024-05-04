package com.acheron.testtask.service;

import com.acheron.testtask.dto.UserCreateUpdateDto;
import com.acheron.testtask.entity.User;
import com.acheron.testtask.exception.UserNotFoundException;
import com.acheron.testtask.mapper.UserMapper;
import com.acheron.testtask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private Integer age;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.age = 18;
        ReflectionTestUtils.setField(userService, "age", this.age);
    }
    //save

    @Test
    void saveShouldReturnBadRequestWhenUnderage() {
        UserCreateUpdateDto userDto = new UserCreateUpdateDto();
        userDto.setBirthDate(LocalDate.now());
        ResponseEntity<?> response = userService.save(userDto);
        assertEquals(ResponseEntity.badRequest().body("Age must be over " + this.age), response);
    }


    @Test
    void saveShouldCreateUserWhenValidRequest() throws UserNotFoundException {
        UserCreateUpdateDto userDto = new UserCreateUpdateDto("John", "Doe", "john@example.com",
                LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.mapUserDtoToUser(any(UserCreateUpdateDto.class))).thenReturn(new User());
        ResponseEntity<?> response = userService.save(userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any(User.class));

    }

    //update

    @Test
    void updateSuccess() throws UserNotFoundException {
        UserCreateUpdateDto userDto = new UserCreateUpdateDto("John", "Doe", "john@example.com",
                LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        User user = new User();
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.update(1L, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUserNotFound() {
        UserCreateUpdateDto userDto = new UserCreateUpdateDto("John", "Doe", "john@example.com",
                LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        Long id = 0L;
        assertEquals(ResponseEntity.badRequest().body("User with Id: " + id + " not found"),
                userService.update(id, userDto));
    }

    //delete

    @Test
    void deleteUserSuccess() throws UserNotFoundException {
        User user = new User();
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        ResponseEntity<?> response = userService.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUserNotFound() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.delete(0L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    //search

    @Test
    void searchByBirthDateRangeSuccess() {
        LocalDate start = LocalDate.of(1990, 1, 1);
        LocalDate end = LocalDate.of(2000, 12, 31);
        when(userRepository.findUsersByBirthDateBetween(any(LocalDate.class), any(LocalDate.class))).
                thenReturn(Collections.emptyList());

        ResponseEntity<?> response = userService.searchByBirthDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void searchByBirthDateRangeInvalid() {
        LocalDate start = LocalDate.of(2000, 12, 20);
        LocalDate end = LocalDate.of(1990, 1, 1);

        ResponseEntity<?> response = userService.searchByBirthDateRange(start, end);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("The end date must be greater than the start date"));
    }
}