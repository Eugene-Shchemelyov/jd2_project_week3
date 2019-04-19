package com.gmail.eugene.shchemelyov.chest.web;

import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;

import java.util.List;

public interface ItemController {
    List<ItemDTO> findAllItems();

    ItemDTO addItem(ItemDTO itemDTO);

    int updateStatus(ItemDTO itemDTO);
}
