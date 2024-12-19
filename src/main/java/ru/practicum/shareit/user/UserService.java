package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        return userMapper.map(user);
    }

    public UserDto save(CreateUserDto user) {
        User toCreate = userMapper.map(user);
        User createdFilm = userRepository.save(toCreate);
        return userMapper.map(createdFilm);
    }

    public UserDto update(Long userId, UpdateUserDto userDto) {
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        User user = userMapper.map(userDto, userId, oldUser);
        User updatedUser = userRepository.save(user);
        return userMapper.map(updatedUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
