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

    public User getUser(int id) {
        if (Boolean.FALSE.equals(userRepository.contains(id))) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        return userRepository.find(id);
    }

    public User create(User user) {
        if (Boolean.TRUE.equals(userRepository.containsUserWithEmail(user.getEmail()))) {
            throw new ValidationException("User with such email already exists");
        }
        user.setId(getCounter());
        return userRepository.add(user);
    }

    public void removeUser(Integer id) {
        if (Boolean.FALSE.equals(userRepository.contains(id))) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        userRepository.remove(id);
    }

    public User updateUser(Integer userId, User newUser) {
        if (Boolean.FALSE.equals(userRepository.contains(userId))) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        if (newUser.getEmail() != null && userRepository.containsUserWithEmail(newUser.getEmail())) {
            throw new ValidationException("User with such email already exists");
        }
        User updatedUser = userRepository.find(userId);
        if (newUser.getName() != null) {
            updatedUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null) {
            updatedUser.setEmail(newUser.getEmail());
        }
        userRepository.update(updatedUser);
        return updatedUser;
    }

    private synchronized int getCounter() {
        return ++idCounter;
    }
}
