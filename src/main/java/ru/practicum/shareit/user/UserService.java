package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private static int idCounter = 0;
    private final UserRepository userRepository;

    public User get(int id) {
        if (Boolean.FALSE.equals(userRepository.contains(id))) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        return userRepository.find(id);
    }

    public User create(User user) {
        if (Boolean.TRUE.equals(userRepository.containsByEmail(user.getEmail()))) {
            throw new ValidationException("User with such email already exists");
        }
        user.setId(getCounter());
        return userRepository.add(user);
    }

    public void remove(Integer id) {
        if (Boolean.FALSE.equals(userRepository.contains(id))) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        userRepository.remove(id);
    }

    public User update(Integer userId, User newUser) {
        if (Boolean.FALSE.equals(userRepository.contains(userId))) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        if (Boolean.TRUE.equals(userRepository.containsByEmail(newUser.getEmail()))) {
            throw new ValidationException("User with such email already exists");
        }
        User updatedUser = userRepository.find(userId);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            updatedUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            updatedUser.setEmail(newUser.getEmail());
        }
        return updatedUser;
    }

    private synchronized int getCounter() {
        return ++idCounter;
    }
}
