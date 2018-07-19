package net.skycade.koth.commands;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.game.KOTHGame;
import net.skycade.koth.game.arena.Arena;
import net.skycade.koth.utils.commands.SkycadeCommand;
import net.skycade.koth.utils.commands.components.PermissibleCommand;
import net.skycade.koth.utils.messages.MessageUtil;
import net.skycade.koth.utils.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

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
public class KOTHCommands extends SkycadeCommand {

    private SkycadeKoth plugin;

    public KOTHCommands(SkycadeKoth plugin) {
        super(plugin, "koth");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.GREEN + "/koth start <game> <arena> - start a KOTH game.");
            sender.sendMessage(ChatColor.GREEN + "/koth listgames - lists all the loaded games.");
            sender.sendMessage(ChatColor.GREEN + "/koth listarenas - lists all the loaded arenas.");
            return;
        }

        if (args.length == 3) {

            switch (args[0].toLowerCase()) {
                case "start":
                    String gameName = args[1];
                    String arenaName = args[2];

                    KOTHGame game = plugin.getGameManager().getGameById(gameName);
                    Arena arena = plugin.getArenaManager().getArenaByName(arenaName);

                    if (game != null) {
                        sender.sendMessage(ChatColor.RED + arenaName + " is already an active game.. /koth join " + gameName);
                        return;
                    }

                    if (arena == null) {
                        sender.sendMessage(ChatColor.RED + arenaName + " is not a valid arena..");
                        return;
                    }

                    sender.sendMessage(ChatColor.GREEN + "Starting the gamemode with the arena " + arena.getArenaName());

                    Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendMessage(
                            player,
                            "joinnewgame",
                            Arrays.asList(
                                    new Placeholder("%gamename%", gameName),
                                    new Placeholder("%arenaname%", arena.getArenaName())
                            )
                    ));

                    plugin.getGameManager().startGame(sender.getName(), new KOTHGame(plugin, gameName, arena));
                    break;
            }
        } else if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "listarenas":
                    for (Arena arena : plugin.getArenaManager().getArenaCache().values()) {
                        sender.sendMessage(ChatColor.BLUE + arena.getArenaName());
                    }
                    break;
                case "listgames":
                    for (KOTHGame game : plugin.getGameManager().getActiveKOTHGames().values()) {
                        sender.sendMessage(ChatColor.BLUE + game.getGameId());
                    }
                    break;
            }
        }
    }
}
