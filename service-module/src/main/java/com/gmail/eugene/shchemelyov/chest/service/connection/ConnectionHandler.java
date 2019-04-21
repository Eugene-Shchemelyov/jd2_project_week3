package com.gmail.eugene.shchemelyov.chest.service.connection;

import com.gmail.eugene.shchemelyov.chest.service.exception.ConnectionFailedException;
import com.gmail.eugene.shchemelyov.chest.service.exception.FileNotExistException;
import com.gmail.eugene.shchemelyov.chest.service.properties.DataProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.CONNECTION_FAILED_MESSAGE;
import static com.gmail.eugene.shchemelyov.chest.service.constant.ExceptionMessageConstant.FILE_NOT_FOUND_MESSAGE;

@Component
public class ConnectionHandler {
    private static final Logger logger = LogManager.getLogger(ConnectionHandler.class);
    private final DataProperties dataProperties;

    public ConnectionHandler(DataProperties dataProperties) {
        try {
            Class.forName(dataProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
        this.dataProperties = dataProperties;
    }

    public Connection getConnection() {
        Properties properties = new Properties();
        properties.setProperty("user", dataProperties.getUsername());
        properties.setProperty("password", dataProperties.getPassword());
        try {
            return DriverManager.getConnection(dataProperties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
    }

    @PostConstruct
    public void createCapacityTables() {
        String tablesFileName = this.getClass().getResource(dataProperties.getTablesFile()).getPath();
        List<String> listQueries = new ArrayList<>();
        readFileQueries(tablesFileName, listQueries);
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                for (String query : listQueries) {
                    statement.executeUpdate(query);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(CONNECTION_FAILED_MESSAGE);
                throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
            }
        } catch (SQLException e) {
            logger.error(CONNECTION_FAILED_MESSAGE);
            throw new ConnectionFailedException(String.format("%s %s", CONNECTION_FAILED_MESSAGE, e.getMessage()), e);
        }
    }

    private void readFileQueries(String fileName, List<String> listQueries) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                listQueries.add(line);
            }
        } catch (IOException e) {
            logger.error(FILE_NOT_FOUND_MESSAGE);
            throw new FileNotExistException(String.format("%s %s", FILE_NOT_FOUND_MESSAGE, e.getMessage()), e);
        }
    }
}
