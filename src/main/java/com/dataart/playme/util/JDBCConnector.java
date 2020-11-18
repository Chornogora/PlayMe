package com.dataart.playme.util;

import com.dataart.playme.exception.DatabaseConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCConnector.class);

    private static DataSource dataSource;

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Can't establish connection with database");
            throw new DatabaseConnectionException("Can't establish connection with database", e);
        }
    }

    public static void setDataSource(DataSource dataSource) {
        JDBCConnector.dataSource = dataSource;
    }
}
