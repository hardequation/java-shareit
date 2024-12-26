package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.NOT_AUTHENTICATED_USER;
import static ru.practicum.shareit.exception.ErrorMessages.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository requestRepository;

    private final ItemRequestMapper requestMapper;

//    private final ItemMapper itemMapper;

    private final UserRepository userRepository;

    public ItemRequestDto create(Long userId, CreateItemRequestDto dto) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        Request request = requestMapper.map(userId, dto);
        Request newRequest = requestRepository.save(request);
        return requestMapper.map(newRequest);
    }

    public ItemRequestDto findById(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
//        List<ItemDto> items = request.getItems().stream()
//                .map(itemMapper::map)
//                .toList();
        return requestMapper.map(request);
    }

    public List<ItemRequestDto> findByRequester(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<Request> requests = requestRepository.findByRequestor(userId);
        return requests.stream()
                .map(requestMapper::map)
                .toList();
    }

    public List<ItemRequestDto> findAll(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(requestMapper::map)
                .toList();
    }

    public void deleteById(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}
