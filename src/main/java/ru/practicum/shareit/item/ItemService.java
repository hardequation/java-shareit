package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongRequirementsException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    public List<ItemDto> findByOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        List<Item> items = itemRepository.findByOwner(owner);
        return items.stream()
                .map(itemMapper::map)
                .toList();
    }

    public List<ItemDto> findAll(Long userId) {
        List<Item> items;
        if (userId != null) {
            User owner = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
            items = itemRepository.findByOwner(owner);
        } else {
            items = itemRepository.findAll();
        }

        return items.stream()
                .map(itemMapper::map)
                .toList();
    }

    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        return itemMapper.map(item);
    }

    public ItemDto save(Long ownerId, CreateItemDto itemDto) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        Item item = itemMapper.map(itemDto, owner);
        Item addedItem = itemRepository.save(item);
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        return itemMapper.map(addedItem);
    }

    public ItemDto save(Long ownerId, Long itemId, UpdateItemDto itemDto) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        if (!oldItem.getOwner().getId().equals(ownerId)) {
            throw new AuthentificationException("Only owner can update item");
        }
        Item item = itemMapper.map(itemDto, itemId, oldItem);

        item.setOwner(oldItem.getOwner());
        Item addedItem = itemRepository.save(item);
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        return itemMapper.map(addedItem);
    }

    public void deleteById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        itemRepository.deleteById(itemId);
    }

    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::map)
                .toList();
    }

    public CommentDto commentItem(Long userId,
                                  Long itemId,
                                  CreateCommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .text(commentDto.getText())
                .created(LocalDate.now())
                .build();
        List<Booking> bookings = bookingRepository.findByBookerAndItemAndStatus(
                comment.getUser(),
                comment.getItem(),
                BookingStatus.APPROVED);

        if (bookings.isEmpty()) {
            throw new WrongRequirementsException("You can leave comment only if you booked this item");
        }
        Comment createdComment = commentRepository.save(comment);
        return commentMapper.map(createdComment);
    }
}
