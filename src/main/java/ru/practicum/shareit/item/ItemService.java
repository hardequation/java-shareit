package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongRequirementsException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List<Item> findByOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        return itemRepository.findByOwner(owner);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public Item save(Item item) {
        Long ownerId = item.getOwner().getId();
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        return itemRepository.save(item);
    }

    public void deleteById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        itemRepository.deleteById(itemId);
    }

    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }

    public Comment commentItem(Comment comment) {
        List<Booking> bookings = bookingRepository.findByBookerAndItemAndStatus(
                comment.getUser(),
                comment.getItem(),
                BookingStatus.APPROVED);

        if (bookings.isEmpty()) {
            throw new WrongRequirementsException("You can leave comment only if you booked this item");
        }

        return commentRepository.save(comment);
    }
}
