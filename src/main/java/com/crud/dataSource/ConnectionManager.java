package com.crud.dataSource;

import com.crud.exceptions.ConnectionException;
import com.crud.exceptions.DriverException;
import com.crud.util.PropertiesUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionManager {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";
    private static final String POOL_SIZE = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 25;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        LOGGER.info("Loading JDBC driver");
        loadDriver();
        LOGGER.info("Initializing connection pool");
        initConnectionPool();
    }

    private static void initConnectionPool() {
        String poolSize = PropertiesUtil.getProperty(POOL_SIZE);
        Integer size = (poolSize == null) ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);

        LOGGER.info("Initializing connection pool with size: {}", size);

        for (int i = 0; i < size; i++) {
            Connection connection = openConnection();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> {
                        if ("close".equals(method.getName())) {
                            LOGGER.debug("Returning connection to pool.");
                            return pool.add((Connection) proxy);
                        } else {
                            LOGGER.debug("Invoking method: {}", method.getName());
                            return method.invoke(connection, args);
                        }
                    });
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
        LOGGER.info("Connection pool initialized successfully.");
    }

    public static Connection getConnection() {
        try {
            LOGGER.debug("Getting connection from pool...");
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Error getting connection from pool", e);
            throw new ConnectionException("Error getting connection", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.info("PostgreSQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.error("PostgreSQL JDBC driver not found", e);
            throw new DriverException("PostgreSql JDBC driver not found", e);
        }
    }

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperty(URL),
                    PropertiesUtil.getProperty(USERNAME),
                    PropertiesUtil.getProperty(PASSWORD)
            );
        } catch (SQLException e) {
            LOGGER.error("Error opening connection", e);
            throw new ConnectionException("Failed to open connection", e);
        }
    }

    public static void closeConnection() {
        for (Connection connection : sourceConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Error closing connection", e);
                throw new ConnectionException("Failed to close connection", e);
            }
        }
    }
}
