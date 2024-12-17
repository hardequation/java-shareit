package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserDto user) {
        User toCreate = userMapper.map(user);
        User createdFilm = userService.save(toCreate);
        return userMapper.map(createdFilm);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @Valid @RequestBody UpdateUserDto userDto) {
        User oldUser = userService.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        User user = userMapper.map(userDto, userId, oldUser);
        User updatedUser = userService.save(user);
        return userMapper.map(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        return userMapper.map(user);
    }

}
