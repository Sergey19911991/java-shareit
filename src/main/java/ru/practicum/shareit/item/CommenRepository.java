package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comments;

import java.util.List;

public interface CommenRepository extends JpaRepository<Comments, Integer> {

    @Query(value = "select * " +
            "from comments as c " +
            "where c.item_id=?1", nativeQuery = true)
    List<Comments> getCommentsItem(int itemId);
}
