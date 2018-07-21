package net.skycade.koth.utils.messages;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.utils.placeholder.Placeholder;
import net.skycade.koth.utils.placeholder.PlaceholderManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

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
public class MessageUtil {

    /** {@link SkycadeKoth} plugin instance. */
    private static SkycadeKoth plugin;

    public MessageUtil(SkycadeKoth plugin) {
        MessageUtil.plugin = plugin;
    }

    /**
     * Send a normal message to a player.
     * @param player - player.
     * @param message - message.
     */
    public static void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().getMessageFromCache("prefix") + " &7" + message));
    }

    /**
     * Send a message from the database that has been configured there.
     * @param player - player.
     * @param key - message key.
     */
    public static void sendMessage(Player player, String key) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().getMessageFromCache("prefix")  + " " + plugin.getMessageManager().getMessageFromCache(key)));
    }

    /**
     * Send a message from the database that has been configured there. With custom placeholders.
     * @param player - player.
     * @param key - message key.
     * @param placeholders - placeholders.
     */
    public static void sendMessage(Player player, String key, List<Placeholder> placeholders) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().getMessageFromCache("prefix")  + " " + PlaceholderManager.replaceCustomPlaceholders(plugin.getMessageManager().getMessageFromCache(key), placeholders)));
    }
}
