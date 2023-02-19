package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Integer> {
    @Query(value = "select * " +
            "from items as i " +
            "where i.request_id=?1", nativeQuery = true)
    List<Item> getRequest(int idRequest);

}
