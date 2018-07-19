package net.skycade.koth.utils.messages;

import net.skycade.koth.SkycadeKoth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class MessageManager {

    private SkycadeKoth plugin;
    private Map<String, String> cachedMessages;

    public MessageManager(SkycadeKoth plugin) {
        this.plugin = plugin;
        cachedMessages = new HashMap<>();

        // TODO load from database.
        plugin.getDatabase().sendPreparedStatement("CREATE TABLE IF NOT EXISTS messages (\n" +
                "    messageKey VARCHAR(40) NOT NULL,\n" +
                "    messageValue LONGTEXT NOT NULL,\n" +
                "    PRIMARY KEY (messageKey)\n" +
                ");", false, true, (statement) -> {});

        loadDatabaseMessages();
    }

    protected String getMessageFromCache(String key) {

        if (!cachedMessages.containsKey(key)) {
            plugin.getDatabase().sendPreparedStatement("INSERT INTO messages VALUES('" + key + "', 'Configure me..')", false, true, statement -> {});
            loadDatabaseMessages();
            return "Please configure the key '" + key + "' in the database..";
        }

        return cachedMessages.get(key);
    }

    public Map<String, String> getCachedMessages() {
        return cachedMessages;
    }

    private void loadDatabaseMessages() {

        cachedMessages.clear();

        plugin.getDatabase().sendPreparedStatement("SELECT * FROM messages;", false, true, statement -> {

            try {

                ResultSet set = statement.getResultSet();

                while (set.next()) {
                    String messageKey = set.getString("messageKey");
                    String messageValue = set.getString("messageValue");

                    if (messageKey != null && messageValue != null) {
                        cachedMessages.put(messageKey, messageValue);
                        System.out.println("Cached a new message.. " + messageValue + " under the key: " + messageKey);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
