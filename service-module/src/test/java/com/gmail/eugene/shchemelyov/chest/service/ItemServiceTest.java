package com.gmail.eugene.shchemelyov.chest.service;

import com.gmail.eugene.shchemelyov.chest.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.chest.repository.enums.ItemStatusEnum;
import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import com.gmail.eugene.shchemelyov.chest.service.connection.ConnectionHandler;
import com.gmail.eugene.shchemelyov.chest.service.converter.ItemConverter;
import com.gmail.eugene.shchemelyov.chest.service.impl.ItemServiceImpl;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ConnectionHandler connectionHandler;
    @Mock
    private ItemConverter itemConverter;
    @Mock
    private Connection connection;

    @Test
    public void shouldAddItemDTO() {
        when(connectionHandler.getConnection()).thenReturn(connection);
        Item item = new Item(1L, "itemOne", ItemStatusEnum.READY, false);
        when(itemRepository.add(connection, item)).thenReturn(item);
        ItemDTO itemDTO = new ItemDTO(null, "itemOne", "READY", false);
        when(itemConverter.fromItemDTO(itemDTO)).thenReturn(item);
        ItemDTO addedItemDTO = new ItemDTO(1L, "itemOne", "READY", false);
        when(itemConverter.fromItem(item)).thenReturn(addedItemDTO);
        itemService = new ItemServiceImpl(connectionHandler, itemRepository, itemConverter);
        ItemDTO savedItemDTO = itemService.add(itemDTO);
        Assert.assertEquals(addedItemDTO, savedItemDTO);
    }

    @Test
    public void shouldGetListItems() {
        Item item = new Item(1L, "itemOne", ItemStatusEnum.READY, false);
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        when(itemRepository.getItems(connection)).thenReturn(listItems);
        ItemDTO itemDTO = new ItemDTO(1L, "itemOne", "READY", false);
        when(itemConverter.fromItem(item)).thenReturn(itemDTO);
        when(connectionHandler.getConnection()).thenReturn(connection);
        itemService = new ItemServiceImpl(connectionHandler, itemRepository, itemConverter);
        List<ItemDTO> returnedItems = itemService.getItems();
        Assert.assertEquals(itemDTO, returnedItems.get(0));
    }
}