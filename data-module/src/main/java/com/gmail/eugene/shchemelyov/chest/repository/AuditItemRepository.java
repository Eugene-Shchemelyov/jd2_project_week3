package com.gmail.eugene.shchemelyov.chest.repository;

import com.gmail.eugene.shchemelyov.chest.repository.model.AuditItem;

import java.sql.Connection;

public interface AuditItemRepository {
    AuditItem add(Connection connection, AuditItem auditItem);
}
