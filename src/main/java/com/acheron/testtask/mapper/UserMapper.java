package com.acheron.testtask.mapper;

import com.acheron.testtask.dto.UserCreateUpdateDto;
import com.acheron.testtask.dto.UserGetDto;
import com.acheron.testtask.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserGetDto mapUserToUserGetDto(User user) {
        return new UserGetDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }

    public User mapUserDtoToUser(UserCreateUpdateDto userDto) {
        return new User(
                null,
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getBirthDate(),
                userDto.getAddress(),
                userDto.getPhoneNumber()
        );
    }

    public User merge(UserCreateUpdateDto userDto, User user) {
        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getBirthDate() != null) user.setBirthDate(userDto.getBirthDate());
        if (userDto.getAddress() != null) user.setAddress(userDto.getAddress());
        if (userDto.getPhoneNumber() != null) user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public List<UserGetDto> mapUserListToUserGetDtoList(List<User> users) {
        return users.stream().map(user -> new UserGetDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber()
        )).toList();
    }
}
