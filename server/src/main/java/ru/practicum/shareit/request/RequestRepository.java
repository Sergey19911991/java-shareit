package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    @Query(value = "select * " +
            "from  requests as r " +
            "where r.requestor_id=?1 ", nativeQuery = true)
    List<ItemRequest> getRequestRequestor(int idRequestor);


    @Query(value = "select * " +
            "from  requests as r " +
            "where r.requestor_id<>?1 " +
            "ORDER BY r.created DESC OFFSET ?2 ROWS " +
            "FETCH NEXT ?3 ROWS ONLY", nativeQuery = true)
    List<ItemRequest> getRequestAllFromSize(int idUser, int from, int size);

    @Query(value = "select * " +
            "from  requests as r " +
            "where r.requestor_id<>?1 " +
            "ORDER BY r.created DESC ", nativeQuery = true)
    List<ItemRequest> getRequestAll(int idUser);
}
