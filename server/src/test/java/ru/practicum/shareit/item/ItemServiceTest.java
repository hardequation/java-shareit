package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@Rollback(false)
@SpringBootTest
@TestPropertySource(properties = {"jdbc:h2:mem:shareit"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    @Autowired
    private final EntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Test
    void findByOwner() {
        CreateUserDto userDto1 = CreateUserDto.builder()
                .name("Some Name1")
                .email("abc1@g.com")
                .build();

        CreateUserDto userDto2 = CreateUserDto.builder()
                .name("Some Name2")
                .email("abc2@g.com")
                .build();

        UserDto user1 = userService.save(userDto1);
        UserDto user2 = userService.save(userDto2);

        CreateItemDto itemDto1 = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        ItemDto createdItem1 = itemService.create(user1.getId(), itemDto1);

        List<ItemDto> foundItems = itemService.findByOwner(user1.getId());
        List<ItemDto> foundItemsEmpty = itemService.findByOwner(2L);

        assertNotNull(foundItems);
        assertEquals(0, foundItemsEmpty.size());
        assertEquals(1, foundItems.size());
        ItemDto foundItem = foundItems.getFirst();
        assertEquals(createdItem1.getName(), foundItem.getName());
        assertEquals(createdItem1.getAvailable(), foundItem.getAvailable());
        assertEquals(createdItem1.getDescription(), foundItem.getDescription());
    }
}