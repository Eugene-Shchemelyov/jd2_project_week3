package com.gmail.eugene.shchemelyov.chest.repository.impl;

import com.gmail.eugene.shchemelyov.chest.repository.AuditItemRepository;
import com.gmail.eugene.shchemelyov.chest.repository.exception.DatabaseException;
import com.gmail.eugene.shchemelyov.chest.repository.model.AuditItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import static com.gmail.eugene.shchemelyov.chest.repository.constant.ExceptionMessageConstant.DATABASE_ERROR_MESSAGE;

@Repository
public class AuditItemRepositoryImpl implements AuditItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(AuditItemRepositoryImpl.class);

    @Override
    public AuditItem add(Connection connection, AuditItem auditItem) {
        String query = "INSERT INTO T_AUDIT_ITEM (F_ACTION, F_ITEM_ID, F_DATE, F_DELETED) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, auditItem.getAction());
            ps.setLong(2, auditItem.getItemId());
            ps.setTimestamp(3, new Timestamp(auditItem.getDate().getTime()));
            ps.setBoolean(4, auditItem.isDeleted());
            int countAuditItemsAdded = ps.executeUpdate();
            logger.info("Count audit items added: {}", countAuditItemsAdded);
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    auditItem.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error(DATABASE_ERROR_MESSAGE);
            throw new DatabaseException(String.format("%s %s", DATABASE_ERROR_MESSAGE, e.getMessage()), e);
        }
        return auditItem;
    }
}
