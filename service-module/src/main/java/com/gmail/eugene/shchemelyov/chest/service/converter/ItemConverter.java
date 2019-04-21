package com.gmail.eugene.shchemelyov.chest.service.converter;

import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;

public interface ItemConverter {
    Item fromItemDTO(ItemDTO itemDTO);

    ItemDTO fromItem(Item item);
}
