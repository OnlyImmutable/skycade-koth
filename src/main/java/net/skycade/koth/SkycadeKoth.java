package net.skycade.koth;

import net.skycade.koth.commands.KOTHCommands;
import net.skycade.koth.game.arena.ArenaManager;
import net.skycade.koth.game.countdown.CountdownManager;
import net.skycade.koth.database.SkycadeDatabase;
import net.skycade.koth.database.SkycadeFlatfileDatabase;
import net.skycade.koth.game.GameManager;
import net.skycade.koth.utils.messages.MessageManager;
import net.skycade.koth.utils.messages.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

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
public class SkycadeKoth extends JavaPlugin {

    /** Sql database instance */
    private SkycadeDatabase database;

    /** Config files */
    private SkycadeFlatfileDatabase databaseConfig;

    /** Instances */
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private CountdownManager countdownManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {

        // Load database.yml
        databaseConfig = new SkycadeFlatfileDatabase(this, "database.yml");

        // String host, int port, String database, String user, String password
        database = new SkycadeDatabase(databaseConfig.getFileConfiguration().getString("database.host"), databaseConfig.getFileConfiguration().getInt("database.port"), databaseConfig.getFileConfiguration().getString("database.database"), databaseConfig.getFileConfiguration().getString("database.username"), databaseConfig.getFileConfiguration().getString("database.password"));
        database.openConnection();

        // Creation of instances..
        gameManager = new GameManager(this);
        arenaManager = new ArenaManager(this);
        countdownManager = new CountdownManager(this);
        messageManager = new MessageManager(this);

        new MessageUtil(this);

        // Register new commands
        new KOTHCommands(this);

        System.out.println("Skycade King of the Hill enabled.");
    }

    @Override
    public void onDisable() {

        // Closes database connection correctly.
        if (!database.isClosed()) {
            database.closePool();
        }

        System.out.println("Skycade King of the Hill disabled.");
    }

    /**
     * {@link GameManager} instance.
     * @return GameManager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * {@link ArenaManager} instance.
     * @return ArenaManager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Countdown handler instance.
     * @return CountdownManager
     */
    public CountdownManager getCountdownManager() {
        return countdownManager;
    }

    /**
     * Message handler instance.
     * @return MessageManager
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * Instance for the sql database ran by Hikari.
     * @return SkycadeDatabase
     */
    public SkycadeDatabase getDatabase() {
        return database;
    }
}
