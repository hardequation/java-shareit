package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dal.UserRepository;

import static ru.practicum.shareit.exception.ErrorMessages.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    ItemRequestRepository requestRepository;

    private final ItemRequestMapper requestMapper;

    UserRepository userRepository;

    public ItemRequestDto findById(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
        return requestMapper.map(request);
    }

    public ItemRequestDto save(ItemRequestDto dto) {
        Request request = requestMapper.map(dto);
        Request newRequest = requestRepository.save(request);
        return requestMapper.map(newRequest);
    }

    public void deleteById(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}
