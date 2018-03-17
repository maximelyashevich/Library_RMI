package com.elyashevich.library.pool;

import com.elyashevich.library.exception.ConnectionTechnicalException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static AtomicBoolean flag = new AtomicBoolean();
    private static ConnectionPool instance;
    private static Lock lock = new ReentrantLock();
    private BlockingQueue<ProxyConnection> connectionQueue;
    private final int POOL_SIZE = 5;

    private ConnectionPool() throws ConnectionTechnicalException {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
        connectionQueue = new ArrayBlockingQueue<>(POOL_SIZE);
        try {
            ProxyConnection connection;
            for (int i = 0; i < POOL_SIZE; i++) {
                connection = new ProxyConnection(ConnectionDB.createConnection());
                connectionQueue.offer(connection);
            }
        } catch (SQLException e) {
            throw new ConnectionTechnicalException(e.getCause());
        }
    }

    public static ConnectionPool getInstance() {
        if (!flag.get()) {
            try {
                lock.lock();
                if (!flag.get()) {
                    instance = new ConnectionPool();
                    flag.set(true);
                }
            } catch (ConnectionTechnicalException e) {
                System.err.println();
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ProxyConnection defineConnection() {
        ProxyConnection connection = null;
        try {
            connection = connectionQueue.take();
        } catch (InterruptedException e) {
            System.err.println();
        }
        return connection;
    }

    public void releaseConnection(ProxyConnection connection) throws ConnectionTechnicalException {
        try {
            if (connection.getAutoCommit()) {
                try {
                    connectionQueue.put(connection);
                } catch (InterruptedException e) {
                    System.err.println();
                }
            }
        } catch (SQLException e) {
            throw new ConnectionTechnicalException(e.getCause());
        }
    }

    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            ProxyConnection connection;
            try {
                connection = connectionQueue.take();
                connection.closeConnection();
            } catch (InterruptedException | SQLException e) {
                System.err.println(e);
            }
        }
        deregisterDriver();
    }

    private void deregisterDriver() {
        Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
        try {
            while (enumDrivers.hasMoreElements()) {
                Driver driver = enumDrivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (SQLException e) {
            System.err.println();
        }
    }
}
