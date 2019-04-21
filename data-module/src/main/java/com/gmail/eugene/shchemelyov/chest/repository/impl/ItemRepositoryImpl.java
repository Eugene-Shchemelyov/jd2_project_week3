package com.gmail.eugene.shchemelyov.chest.repository.impl;

import com.gmail.eugene.shchemelyov.chest.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.chest.repository.enums.ItemStatusEnum;
import com.gmail.eugene.shchemelyov.chest.repository.exception.DatabaseException;
import com.gmail.eugene.shchemelyov.chest.repository.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.chest.repository.constant.ExceptionMessageConstant.DATABASE_ERROR_MESSAGE;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    public Item add(Connection connection, Item item) {
        String query = "INSERT INTO T_ITEM (F_NAME, F_STATUS, F_DELETED) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getItemStatusEnum().name());
            ps.setBoolean(3, item.isDeleted());
            int countItemsAdded = ps.executeUpdate();
            logger.info("Count items added: {}", countItemsAdded);
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    item.setId(resultSet.getLong(1));
                }
                return item;
            }
        } catch (SQLException e) {
            logger.error(DATABASE_ERROR_MESSAGE);
            throw new DatabaseException(String.format("%s %s", DATABASE_ERROR_MESSAGE, e.getMessage()), e);
        }
    }

    @Override
    public List<Item> getItems(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM T_ITEM";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                List<Item> items = new ArrayList<>();
                while (resultSet.next()) {
                    items.add(getItem(resultSet));
                }
                return items;
            }
        } catch (SQLException e) {
            logger.error(DATABASE_ERROR_MESSAGE);
            throw new DatabaseException(String.format("%s %s", DATABASE_ERROR_MESSAGE, e.getMessage()), e);
        }
    }

    @Override
    public int update(Connection connection, Item item) {
        String query = "UPDATE T_ITEM SET F_STATUS = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, item.getItemStatusEnum().name());
            ps.setLong(2, item.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(DATABASE_ERROR_MESSAGE);
            throw new DatabaseException(String.format("%s %s", DATABASE_ERROR_MESSAGE, e.getMessage()), e);
        }
    }

    private Item getItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getLong("F_ID"),
                resultSet.getString("F_NAME"),
                ItemStatusEnum.valueOf(resultSet.getString("F_STATUS")),
                resultSet.getBoolean("F_DELETED")
        );
    }
}
