package com.gmail.eugene.shchemelyov.chest.service;

import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO add(Item item);
    List<ItemDTO> getItemByStatus(Long id);
    int update(String status);
}
