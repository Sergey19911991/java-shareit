package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(int userId, ItemDtoInletOutlet itemDtoInletOutlet) {
        return post("", userId, itemDtoInletOutlet);
    }

    public ResponseEntity<Object> patchItem(ItemDtoInletOutlet itemDtoInletOutlet, int id, int userId) {
        return patch("/" + id, userId, itemDtoInletOutlet);
    }

    public ResponseEntity<Object> getItem(int id, int userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getItems(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemsSearch(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(int userId, Comment comment, int itemId) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
