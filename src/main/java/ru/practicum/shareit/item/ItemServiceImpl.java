package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    private final MappingComment mappingComment;

    private final BookingsRepository bookingsRepository;
    private final CommenRepository commenRepository;
    private final MappingItem mappingItem;

    private final MappingNextBooking mappingNextBooking;

    private final MappingLastBooking mappingLastBooking;
    private final MappingItemDtoOutletInlet mappingItemDtoOutletInlet;

    private final RequestRepository requestRepository;

    @Override
    public ItemDtoInletOutlet creatItem(int id, ItemDtoInletOutlet itemDtoInletOutlet) {
        Item item = mappingItemDtoOutletInlet.mapping(itemDtoInletOutlet);
        if (itemDtoInletOutlet.getRequestId() != null) {
            int i = Integer.parseInt((String.valueOf(itemDtoInletOutlet.getRequestId())));
            ItemRequest itemRequest = requestRepository.findById(i).get();
            item.setRequest(itemRequest);
        }
        validationItem(item);
        if (userRepositoryJpa.existsById(id)) {
            item.setOwner(id);
            log.info("Создана вещь");
            itemDtoInletOutlet.setId(itemRepositoryJpa.save(item).getId());
            return itemDtoInletOutlet;
        } else {
            log.error("Такой пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден!");
        }
    }

    @Override
    public Item updateItem(int idUser, Item item, int id) {
        if (idUser == itemRepositoryJpa.findById(id).get().getOwner() && userRepositoryJpa.existsById(idUser)) {
            if (item.getName() == null) {
                item.setName(itemRepositoryJpa.findById(id).get().getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemRepositoryJpa.findById(id).get().getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(itemRepositoryJpa.findById(id).get().getAvailable());
            }
            item.setId(id);
            item.setOwner(idUser);
            log.info("Перезаписана вещь");
            return itemRepositoryJpa.save(item);
        } else {
            log.error("Данная вещь не принадлежит пользователю или такого пользователя нет");
            throw new NotFoundException("Данная вещь не принадлежит пользователю или такого пользователя нет");
        }

    }

    @Override
    public ItemDto getItemById(int id, int idUser) {
        if (itemRepositoryJpa.existsById(id)) {
            ItemDto itemDto = new ItemDto();
            itemDto = mappingItem.mapItemDto(itemRepositoryJpa.findById(id).get());
            if (idUser == itemRepositoryJpa.findById(id).get().getOwner()) {
                Booking next = new Booking();
                next.setStart(LocalDateTime.MAX);
                if (!bookingsRepository.getBookingItemOwner((itemRepositoryJpa.findById(id).get().getOwner()), itemRepositoryJpa.findById(id).get().getId()).isEmpty()) {
                    for (Booking booking : bookingsRepository.getBookingItemOwner(itemRepositoryJpa.findById(id).get().getOwner(), itemRepositoryJpa.findById(id).get().getId())) {
                        if (booking.getStart().isAfter(LocalDateTime.now()) && booking.getStart().isBefore(next.getStart())) {
                            next = booking;
                        }
                    }
                    itemDto.setNextBooking(mappingNextBooking.nextBooking(next));
                }
                Booking last = new Booking();
                last.setEnd(LocalDateTime.MIN);
                if (!bookingsRepository.getBookingItemOwner(itemRepositoryJpa.findById(id).get().getOwner(), itemRepositoryJpa.findById(id).get().getId()).isEmpty()) {
                    for (Booking booking : bookingsRepository.getBookingItemOwner(itemRepositoryJpa.findById(id).get().getOwner(), itemRepositoryJpa.findById(id).get().getId())) {
                        if (booking.getEnd().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(last.getEnd())) {
                            last = booking;
                        }
                    }
                    if (last.getStart() != null) {
                        itemDto.setLastBooking(mappingLastBooking.lastBooking(last));
                    }
                }
            }
            log.info("Данные о вещи с id = {}", id);
            return itemDto;
        } else {
            log.error("Такой вещи нет");
            throw new NotFoundException("Такой вещи нет");
        }
    }

    @Override
    public Set<ItemDto> getItemAll(int idUser) {
        Set<ItemDto> itemsList = new TreeSet<>(Comparator.comparingInt(ItemDto::getId));
        for (Item itemId : itemRepositoryJpa.findAll()) {
            if (itemId.getOwner() == idUser) {
                ItemDto itemDto = new ItemDto();
                itemDto = mappingItem.mapItemDto(itemId);
                if (idUser == itemId.getOwner()) {
                    Booking next = new Booking();
                    next.setStart(LocalDateTime.MAX);
                    if (!bookingsRepository.getBookingItemOwner((itemId.getOwner()), itemId.getId()).isEmpty()) {
                        for (Booking booking : bookingsRepository.getBookingItemOwner(itemId.getOwner(), itemId.getId())) {
                            if (booking.getStart().isAfter(LocalDateTime.now()) && booking.getStart().isBefore(next.getStart())) {
                                next = booking;
                            }
                        }
                        itemDto.setNextBooking(mappingNextBooking.nextBooking(next));
                    }
                    Booking last = new Booking();
                    last.setEnd(LocalDateTime.MIN);
                    if (!bookingsRepository.getBookingItemOwner(itemId.getOwner(), itemId.getId()).isEmpty()) {
                        for (Booking booking : bookingsRepository.getBookingItemOwner(itemId.getOwner(), itemId.getId())) {
                            if (booking.getEnd().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(last.getEnd())) {
                                last = booking;
                            }
                        }
                        itemDto.setLastBooking(mappingLastBooking.lastBooking(last));
                    }
                }
                itemsList.add(itemDto);
            }
        }
        log.info("Данные о всех вещах пользователя с id = {}", idUser);
        return itemsList;
    }

    @Override
    public List<ItemDto> getSearch(String text, int idUser) {
        log.error("Пользователю {} выведена информация о вещах, содержащих в названии и/или в описании текст {}", idUser, text);
        List<ItemDto> itemsList = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item itemId : itemRepositoryJpa.findAll()) {
                if ((itemId.getName().toLowerCase().contains(text.toLowerCase()) || itemId.getDescription().toLowerCase().contains(text.toLowerCase())) && itemId.getAvailable() == true) {
                    itemsList.add(mappingItem.mapItemDto(itemId));
                }
            }
        }
        return itemsList;
    }

    public void validationItem(Item item) {
        if (item.getAvailable() == null) {
            log.error("Статус не может отсутствовать");
            throw new RequestException("Статус не может отсутствовать");
        }
        if (item.getName().isEmpty()) {
            log.error("Название вещи не может быть пустым");
            throw new RequestException("Название вещи не может быть пустым");
        }
        if (item.getDescription() == null) {
            log.error("Описание вещи не может быть пустым");
            throw new RequestException("Описание вещи не может быть пустым");
        }

    }

    public CommentDto createComment(Comments comment, int booker, int itemId) {
        Boolean endBooking = false;
        if (bookingsRepository.getBookingItemBooker(booker, itemId).size() != 0 && !comment.getText().isEmpty()) {
            for (Booking booking : bookingsRepository.getBookingItemBooker(booker, itemId)) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    endBooking = true;
                    break;
                }
            }
            if (endBooking == false) {
                throw new RequestException("Срок бронирования данной вещи не закончился");
            }
            comment.setItem(itemRepositoryJpa.findById(itemId).get());
            comment.setAuthor(userRepositoryJpa.findById(booker).get());
            comment.setCreated(LocalDateTime.now());
            commenRepository.save(comment);
            log.info("Создан коммпентарий пользователем с id = {} для вещи с id =  {}", booker, itemId);
            return mappingComment.mapCommentDto(comment);
        } else {
            log.error("Данный пользователь не бронировал данную вещь");
            throw new RequestException("Данный пользователь не бронировал данную вещь");
        }
    }



}




