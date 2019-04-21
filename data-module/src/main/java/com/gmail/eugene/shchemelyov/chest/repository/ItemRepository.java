package com.gmail.eugene.shchemelyov.chest.repository;

import com.gmail.eugene.shchemelyov.chest.repository.model.Item;

import java.sql.Connection;
import java.util.List;

public interface ItemRepository {
    Item add(Connection connection, Item item);

    List<Item> getItems(Connection connection);

    int update(Connection connection, Item item);
}
