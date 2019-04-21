package com.gmail.eugene.shchemelyov.chest.service.converter.impl;

import com.gmail.eugene.shchemelyov.chest.repository.enums.ItemStatusEnum;
import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import com.gmail.eugene.shchemelyov.chest.service.converter.ItemConverter;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.springframework.stereotype.Component;

@Component
public class ItemConverterImpl implements ItemConverter {
    @Override
    public Item fromItemDTO(ItemDTO itemDTO) {
        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                ItemStatusEnum.valueOf(itemDTO.getStatus()),
                itemDTO.getDeleted()
        );
    }

    @Override
    public ItemDTO fromItem(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getItemStatusEnum().name(),
                item.isDeleted()
        );
    }
}
