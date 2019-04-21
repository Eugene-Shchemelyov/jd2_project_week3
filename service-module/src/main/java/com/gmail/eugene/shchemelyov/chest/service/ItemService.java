package com.gmail.eugene.shchemelyov.chest.service;

import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO add(ItemDTO itemDTO);

    List<ItemDTO> getItems();

    int update(Long id, String status);
}
