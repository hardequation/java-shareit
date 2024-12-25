package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {"jdbc:h2:mem:shareit"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    @Autowired
    private final EntityManager em;
    UserDto user1;
    UserDto user2;
    UserDto user3;
    ItemDto item1;
    ItemDto item2;
    ItemDto item3;
    TypedQuery<Item> queryFindById;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        CreateUserDto userDto1 = CreateUserDto.builder()
                .name("Some Name1")
                .email("abc1@g.com")
                .build();

        CreateUserDto userDto2 = CreateUserDto.builder()
                .name("Some Name2")
                .email("abc2@g.com")
                .build();

        CreateUserDto userDto3 = CreateUserDto.builder()
                .name("Some Name3")
                .email("abc3@g.com")
                .build();

        user1 = userService.save(userDto1);
        user2 = userService.save(userDto2);
        user3 = userService.save(userDto3);

        CreateItemDto itemDto1 = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        CreateItemDto itemDto2 = CreateItemDto.builder()
                .name("Item2")
                .description("Some description2")
                .available(true)
                .build();

        CreateItemDto itemDto3 = CreateItemDto.builder()
                .name("Item3")
                .description("Some description3")
                .available(true)
                .build();

        item1 = itemService.create(user1.getId(), itemDto1);
        item2 = itemService.create(user1.getId(), itemDto2);
        item3 = itemService.create(user2.getId(), itemDto3);

        queryFindById = em.createQuery("Select i from Item i where i.id = :id", Item.class);
    }

    @Test
    void create() {
        CreateItemDto toCreate = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        ItemDto itemDto = itemService.create(user1.getId(), toCreate);
        Item item = queryFindById.setParameter("id", itemDto.getId()).getSingleResult();
        assertNotNull(item);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void update() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        UpdateItemDto toUpdate = UpdateItemDto.builder()
                .name("new Name")
                .description("new description")
                .available(true)
                .build();

        ItemDto createdItemDto = itemService.create(user1.getId(), itemDto);
        ItemDto updated = itemService.update(user1.getId(), createdItemDto.getId(), toUpdate);
        Item item = queryFindById.setParameter("id", updated.getId()).getSingleResult();
        assertNotNull(createdItemDto);
        assertEquals(createdItemDto.getId(), item.getId());
        assertEquals(updated.getName(), item.getName());
        assertEquals(updated.getAvailable(), item.getAvailable());
        assertEquals(updated.getDescription(), item.getDescription());
    }

    @Test
    void findByOwner() {
        List<ItemDto> foundItems = itemService.findByOwner(user1.getId());
        List<ItemDto> foundItemsEmpty = itemService.findByOwner(user3.getId());

        assertNotNull(foundItems);
        assertEquals(0, foundItemsEmpty.size());
        assertEquals(2, foundItems.size());
        ItemDto foundItem1 = foundItems.get(0);
        ItemDto foundItem2 = foundItems.get(1);
        assertEquals(item1.getName(), foundItem1.getName());
        assertEquals(item1.getAvailable(), foundItem1.getAvailable());
        assertEquals(item1.getDescription(), foundItem1.getDescription());
        assertEquals(item2.getName(), foundItem2.getName());
        assertEquals(item2.getAvailable(), foundItem2.getAvailable());
        assertEquals(item2.getDescription(), foundItem2.getDescription());
    }

    @Test
    void findAll() {
        List<ItemDto> foundItems = itemService.findAll(user1.getId());
        List<ItemDto> foundItemsAll = itemService.findAll(null);

        assertNotNull(foundItems);
        assertEquals(2, foundItems.size());
        assertEquals(3, foundItemsAll.size());
        ItemDto foundItem1 = foundItemsAll.get(0);
        ItemDto foundItem2 = foundItemsAll.get(1);
        ItemDto foundItem3 = foundItemsAll.get(2);
        assertEquals(item1.getName(), foundItem1.getName());
        assertEquals(item1.getAvailable(), foundItem1.getAvailable());
        assertEquals(item1.getDescription(), foundItem1.getDescription());
        assertEquals(item2.getName(), foundItem2.getName());
        assertEquals(item2.getAvailable(), foundItem2.getAvailable());
        assertEquals(item2.getDescription(), foundItem2.getDescription());
        assertEquals(item3.getName(), foundItem3.getName());
        assertEquals(item3.getAvailable(), foundItem3.getAvailable());
        assertEquals(item3.getDescription(), foundItem3.getDescription());
    }

    @Test
    void findById() {
        ItemDto foundItem = itemService.findById(item1.getId());

        assertNotNull(foundItem);
        assertEquals(item1.getName(), foundItem.getName());
        assertEquals(item1.getAvailable(), foundItem.getAvailable());
        assertEquals(item1.getDescription(), foundItem.getDescription());
        assertThrows(NotFoundException.class, () -> itemService.findById(999L));
    }
}