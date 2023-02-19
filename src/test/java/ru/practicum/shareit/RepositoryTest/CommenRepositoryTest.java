package ru.practicum.shareit.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.JpaH2Runner;
import ru.practicum.shareit.item.CommenRepository;

import org.springframework.test.context.jdbc.Sql;

@SpringJUnitConfig
@ContextConfiguration(classes = {
        CommenRepository.class

})
public class CommenRepositoryTest extends JpaH2Runner {

    @Autowired
    CommenRepository commenRepository;


    @Test
    @Sql(value = {
            "classpath:Sql/add_user.sql",
            "classpath:Sql/add_request.sql",
            "classpath:Sql/add_item.sql",
            "classpath:Sql/add_comments.sql"
    })
    public void getCommentsItemTest() {
        System.out.println(commenRepository.getCommentsItem(1));
    }
}
