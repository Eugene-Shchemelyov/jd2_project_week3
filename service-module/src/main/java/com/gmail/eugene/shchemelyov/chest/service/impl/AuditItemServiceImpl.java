package com.gmail.eugene.shchemelyov.chest.service.impl;

import com.gmail.eugene.shchemelyov.chest.repository.AuditItemRepository;
import com.gmail.eugene.shchemelyov.chest.repository.model.AuditItem;
import com.gmail.eugene.shchemelyov.chest.service.AuditItemService;
import com.gmail.eugene.shchemelyov.chest.service.connection.ConnectionHandler;
import com.gmail.eugene.shchemelyov.chest.service.exception.ConnectionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.CONNECTION_FAILED_MESSAGE;
import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.TRANSACTION_FAILED_MESSAGE;

@Service
public class AuditItemServiceImpl implements AuditItemService {
    private static final Logger logger = LoggerFactory.getLogger(AuditItemServiceImpl.class);
    private final ConnectionHandler connectionHandler;
    private final AuditItemRepository auditItemRepository;

    public AuditItemServiceImpl(ConnectionHandler connectionHandler,
                                AuditItemRepository auditItemRepository) {
        this.connectionHandler = connectionHandler;
        this.auditItemRepository = auditItemRepository;
    }

    @Override
    public AuditItem save(AuditItem auditItem) {
        try (Connection connection = connectionHandler.getConnection()) {
            try{
            connection.setAutoCommit(false);
            AuditItem savedAuditItem = auditItemRepository.add(connection, auditItem);
            connection.commit();
            return savedAuditItem;
            }  catch (Exception e){
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
