package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public final User map(CreateUserDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public final User map(UpdateUserDto dto, Long userId, User oldUser) {
        return User.builder()
                .id(userId)
                .name(dto.getName() != null ? dto.getName() : oldUser.getName())
                .email(dto.getEmail() != null ? dto.getEmail() : oldUser.getEmail())
                .build();
    }

    public final UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public final User map(UserDto user) {
        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
