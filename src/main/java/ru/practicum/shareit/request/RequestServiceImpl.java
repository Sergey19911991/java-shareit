package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;
import ru.practicum.shareit.item.dto.MappingItemDtoOutletInlet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final MappingRequest mappingRequest;
    private final UserRepositoryJpa userRepositoryJpa;

    private final RequestRepository requestRepository;

    private final ItemRepositoryJpa itemRepositoryJpa;

    private final MappingItemDtoOutletInlet mappingItemDtoOutletInlet;

    @Override
    public ItemRequest createRequest(ItemRequestDto requestDto, int idUser) {
        User user = userRepositoryJpa.findById(idUser).orElse(null);
        if (user != null) {
            ItemRequest itemRequest = mappingRequest.mapItemDto(requestDto);
            itemRequest.setCreated(LocalDateTime.now());
            itemRequest.setRequestor(user);
            log.info("Создан запрос");
            return requestRepository.save(itemRequest);
        } else {
            log.error("Пользователь с таким id не найден");
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public List<ItemRequestDtoOutlet> getRequests(int idUser) {
        if (userRepositoryJpa.existsById(idUser)) {
            List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();
            List<ItemRequest> itemRequests = new ArrayList<>(requestRepository.getRequestRequestor(idUser));
            for (ItemRequest itemRequest : itemRequests) {
                List<Item> itemList = new ArrayList<>(itemRepositoryJpa.getRequest(itemRequest.getId()));
                List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
                for (Item item : itemList) {
                    itemDtoInletOutlets.add(mappingItemDtoOutletInlet.mappingItem(item));
                }
                ItemRequestDtoOutlet itemRequestDtoOutlet = mappingRequest.mapItemDtoOutlet(itemRequest);
                itemRequestDtoOutlet.setItems(itemDtoInletOutlets);
                itemRequestList.add(itemRequestDtoOutlet);
            }
            log.info("Запросы пользователя с id = {}", idUser);
            return itemRequestList;
        } else {
            log.error("Пользователь с таким id не найден");
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public List<ItemRequestDtoOutlet> getRequestsAll(int idUser, Integer from, Integer size) {
        if (size == null && from == null) {
            List<ItemRequest> itemRequests = new ArrayList<>(requestRepository.getRequestAll(idUser));
            List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();
            for (ItemRequest itemRequest : itemRequests) {
                List<Item> itemList = new ArrayList<>(itemRepositoryJpa.getRequest(itemRequest.getId()));
                List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
                for (Item item : itemList) {
                    itemDtoInletOutlets.add(mappingItemDtoOutletInlet.mappingItem(item));
                }
                ItemRequestDtoOutlet itemRequestDtoOutlet = mappingRequest.mapItemDtoOutlet(itemRequest);
                itemRequestDtoOutlet.setItems(itemDtoInletOutlets);
                itemRequestList.add(itemRequestDtoOutlet);
            }
            log.info("Запросы других пользователей, отсортированные по дате создания");
            return itemRequestList;
        }
        if (size != null && from != null && size > 0 && from >= 0) {
            List<ItemRequest> itemRequests = new ArrayList<>(requestRepository.getRequestAllFromSize(idUser, from, size));
            List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();
            for (ItemRequest itemRequest : itemRequests) {
                List<Item> itemList = new ArrayList<>(itemRepositoryJpa.getRequest(itemRequest.getId()));
                List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
                for (Item item : itemList) {
                    itemDtoInletOutlets.add(mappingItemDtoOutletInlet.mappingItem(item));
                }
                ItemRequestDtoOutlet itemRequestDtoOutlet = mappingRequest.mapItemDtoOutlet(itemRequest);
                itemRequestDtoOutlet.setItems(itemDtoInletOutlets);
                itemRequestList.add(itemRequestDtoOutlet);
            }
            log.info("Запросы других пользователей, отсортированные по дате создания");
            return itemRequestList;
        } else {
            log.error("Длина списка не может быть меньше нуля или равна нулю");
            throw new RequestException("Длина списка не может быть меньше нуля или равна нулю");
        }

    }

    @Override
    public ItemRequestDtoOutlet getRequestById(int userId, int requestId) {
        if (userRepositoryJpa.existsById(userId)) {
            ItemRequest itemRequest = requestRepository.findById(requestId).orElse(null);
            if (itemRequest != null) {
                ItemRequestDtoOutlet itemRequestDtoOutlet = mappingRequest.mapItemDtoOutlet(itemRequest);
                List<Item> itemList = new ArrayList<>(itemRepositoryJpa.getRequest(requestId));
                List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
                for (Item item : itemList) {
                    itemDtoInletOutlets.add(mappingItemDtoOutletInlet.mappingItem(item));
                }
                itemRequestDtoOutlet.setItems(itemDtoInletOutlets);
                log.info("Данные о запросе с id = {}", requestId);
                return itemRequestDtoOutlet;
            } else {
                log.error("Запрос с таким id не найден");
                throw new NotFoundException("Запрос с таким id не найден");
            }
        } else {
            log.error("Пользователь с таким id не найден");
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }


}
