package ru.practicum.shareit.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.JpaH2Runner;
import ru.practicum.shareit.item.ItemRepositoryJpa;

@SpringJUnitConfig
@ContextConfiguration(classes = {
        ItemRepositoryJpa.class

})
public class ItemRepositoryTest extends JpaH2Runner {
    @Autowired
    ItemRepositoryJpa itemRepositoryJpa;

    @Test
    @Sql(value = {
            "classpath:Sql/add_user.sql",
            "classpath:Sql/add_request.sql",
            "classpath:Sql/add_item.sql"

    })
    public void getCommentsItemTest() {
        System.out.println(itemRepositoryJpa.getRequest(1));
    }

}
