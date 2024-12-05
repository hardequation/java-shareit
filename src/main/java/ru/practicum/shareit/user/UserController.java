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
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

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
        User createdFilm = userService.create(toCreate);
        return userMapper.map(createdFilm);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Integer userId) {
        userService.remove(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Integer userId, @Valid @RequestBody UpdateUserDto userDto) {
        User user = userMapper.map(userDto);
        User updatedUser = userService.update(userId, user);
        return userMapper.map(updatedUser);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        User user = userService.get(id);
        return userMapper.map(user);
    }

}
