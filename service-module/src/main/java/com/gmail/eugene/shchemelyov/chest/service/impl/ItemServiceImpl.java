package com.gmail.eugene.shchemelyov.chest.service.impl;

import com.gmail.eugene.shchemelyov.chest.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.chest.repository.enums.ItemStatusEnum;
import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import com.gmail.eugene.shchemelyov.chest.service.ItemService;
import com.gmail.eugene.shchemelyov.chest.service.connection.ConnectionHandler;
import com.gmail.eugene.shchemelyov.chest.service.converter.ItemConverter;
import com.gmail.eugene.shchemelyov.chest.service.exception.ConnectionFailedException;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.CONNECTION_FAILED_MESSAGE;
import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.TRANSACTION_FAILED_MESSAGE;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ConnectionHandler connectionHandler;
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    @Autowired
    public ItemServiceImpl(ConnectionHandler connectionHandler,
                           ItemRepository itemRepository,
                           ItemConverter itemConverter) {
        this.connectionHandler = connectionHandler;
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = itemConverter.fromItemDTO(itemDTO);
                item.setDeleted(false);
                Item savedItem = itemRepository.add(connection, item);
                connection.commit();
                return itemConverter.fromItem(savedItem);
            } catch (Exception e) {
                connection.rollback();
                logger.error(TRANSACTION_FAILED_MESSAGE);
                throw new ConnectionFailedException(String.format("%s %s", TRANSACTION_FAILED_MESSAGE, e.getMessage()), e);
            }
        } catch (SQLException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
    }

    @Override
    public List<ItemDTO> getItems() {
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Item> items = itemRepository.getItems(connection);
                List<ItemDTO> convertedItems = new ArrayList<>();
                items.forEach(item -> convertedItems.add(itemConverter.fromItem(item)));
                connection.commit();
                return convertedItems;
            } catch (Exception e) {
                connection.rollback();
                logger.error(TRANSACTION_FAILED_MESSAGE);
                throw new ConnectionFailedException(String.format("%s %s", TRANSACTION_FAILED_MESSAGE, e.getMessage()), e);
            }
        } catch (SQLException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
    }

    @Override
    public int update(Long id, String status) {
        if (status == null) {
            return 0;
        }
        try (Connection connection = connectionHandler.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = new Item(id, null, ItemStatusEnum.valueOf(status), null);
                int countUpdatedItems = itemRepository.update(connection, item);
                connection.commit();
                return countUpdatedItems;
            } catch (Exception e) {
                connection.rollback();
                logger.error(TRANSACTION_FAILED_MESSAGE);
                throw new ConnectionFailedException(String.format("%s %s", TRANSACTION_FAILED_MESSAGE, e.getMessage()), e);
            }
        } catch (SQLException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
    }
}
