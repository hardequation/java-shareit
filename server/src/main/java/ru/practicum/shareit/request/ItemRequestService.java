package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemRequestDto create(Long userId, CreateItemRequestDto dto) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        Request request = requestMapper.map(userId, dto);
        Request newRequest = requestRepository.save(request);
        return requestMapper.map(newRequest, null);
    }

    public ItemRequestDto findById(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
        List<Item> items = itemRepository.getByRequest(request.getId(), Sort.by("id").descending());

        return requestMapper.map(request, items.stream().map(itemMapper::map).toList());
    }

    public List<ItemRequestDto> findByRequester(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<Request> requests = requestRepository.findByRequestor(userId);
        return requests.stream()
                .map(request -> {
                    List<Item> items = itemRepository.getByRequest(request.getId(), Sort.by("id").descending());
                    return requestMapper.map(request, items.stream().map(itemMapper::map).toList());
                })
                .toList();
    }

    public List<ItemRequestDto> findAll(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(request -> {
                    List<Item> items = itemRepository.getByRequest(request.getId(), Sort.by("id").descending());
                    return requestMapper.map(request, items.stream().map(itemMapper::map).toList());
                })
                .toList();
    }

    public void deleteById(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}
