package net.skycade.koth.database;

import com.zaxxer.hikari.HikariDataSource;
import net.skycade.koth.utils.Callback;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**************************************************************************************************
 *     Copyright 2018 Jake Brown                                                                  *
 *                                                                                                *
 *     Licensed under the Apache License, Version 2.0 (the "License");                            *
 *     you may not use this file except in compliance with the License.                           *
 *     You may obtain a copy of the License at                                                    *
 *                                                                                                *
 *         http://www.apache.org/licenses/LICENSE-2.0                                             *
 *                                                                                                *
 *     Unless required by applicable law or agreed to in writing, software                        *
 *     distributed under the License is distributed on an "AS IS" BASIS,                          *
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                   *
 *     See the License for the specific language governing permissions and                        *
 *     limitations under the License.                                                             *
 **************************************************************************************************/
public class SkycadeDatabase {

    /** Hikari instance for sql connections. */
    private HikariDataSource connectionPool;
    /** Hikari credentials and connection info. */
    private final String host, database, user, password;
    private final int port;

    /**
     * Connect to the database.. (MySQL)
     * @param host - the host ip address to connect with.
     * @param port - the port to connect with.
     * @param database - the database.
     * @param user - the username.
     * @param password - the password.
     */
    public SkycadeDatabase(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /**
     * Connect to the repository.. (MySQL)
     */
    public void openConnection() {

        try {

            connectionPool = new HikariDataSource();
            connectionPool.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            connectionPool.addDataSourceProperty("serverName", host);
            connectionPool.addDataSourceProperty("port", port);
            connectionPool.addDataSourceProperty("databaseName", database);
            connectionPool.addDataSourceProperty("user", user);
            connectionPool.addDataSourceProperty("password", password);
            connectionPool.setConnectionTimeout(3000);
            connectionPool.setValidationTimeout(1000);
            connectionPool.setLeakDetectionThreshold(60 * 1000);
            connectionPool.setMaximumPoolSize(20);

            System.out.println("Waiting for confirmation of connection.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an issue connecting to the database, please check your credentials and try again!");
            Bukkit.getServer().shutdown();
        }
    }

    /**
     * Send queries to the database (Prepared Statement).
     * @param query - the query.
     * @param callback - the callback to get the query after execution.
     */
    public void sendPreparedStatement(String query, boolean update, boolean async, Callback<PreparedStatement> callback) {

        if (async) {

            new Thread(() -> {

                PreparedStatement statement;

                try (Connection connection = connectionPool.getConnection()) {

                    statement = connection.prepareStatement(query);

                    if (update) {
                        statement.executeUpdate();
                    } else {
                        statement.execute();
                    }

                    callback.call(statement);
                } catch (SQLException e) {
                    e.printStackTrace();
                    callback.call(null);
                }
            }).start();
        } else {

            PreparedStatement statement;

            try (Connection connection = connectionPool.getConnection()) {

                statement = connection.prepareStatement(query);

                if (update) {
                    statement.executeUpdate();
                } else {
                    statement.execute();
                }

                callback.call(statement);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.call(null);
            }
        }
    }

    /**
     * Check if the connection pool is closed.
     * @return isClosed - if the pool is closed.
     */
    public boolean isClosed() { return connectionPool.isClosed(); }

    /**
     * Close Connection Pool
     */
    public void closePool() {
        try {
            connectionPool.evictConnection(connectionPool.getConnection());
            connectionPool.close();
        } catch (Exception e) { /* Ignored */ }
    }
}
