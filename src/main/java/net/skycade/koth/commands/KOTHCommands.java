package net.skycade.koth.commands;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.game.GamePhase;
import net.skycade.koth.game.KOTHGame;
import net.skycade.koth.game.arena.Arena;
import net.skycade.koth.utils.commands.SkycadeCommand;
import net.skycade.koth.utils.commands.components.PermissibleCommand;
import net.skycade.koth.utils.messages.MessageUtil;
import net.skycade.koth.utils.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

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
        super("koth");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.GREEN + "/koth start <gameID> <arena> - start a KOTH game.");
            sender.sendMessage(ChatColor.GREEN + "/koth join <gameID> - join a KOTH game.");
            sender.sendMessage(ChatColor.GREEN + "/koth leave - leave a KOTH game.");
            sender.sendMessage(ChatColor.GREEN + "/koth listgames - lists all the loaded games.");
            sender.sendMessage(ChatColor.GREEN + "/koth listarenas - lists all the loaded arenas.");
            return;
        }

        if (args.length == 3) {

            switch (args[0].toLowerCase()) {

                case "start":

                    if (sender instanceof Player && !sender.hasPermission("koth.start")) {
                        MessageUtil.sendMessage((Player) sender, "noPermission");
                        return;
                    }

                    String createGameName = args[1];
                    String arenaName = args[2];

                    KOTHGame game = plugin.getGameManager().getGameById(createGameName);
                    Arena arena = plugin.getArenaManager().getArenaByName(arenaName);

                    if (game != null) {
                        sender.sendMessage(ChatColor.RED + createGameName + " is already an active game.. /koth join " + createGameName);
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
                                    new Placeholder("%gamename%", createGameName),
                                    new Placeholder("%arenaname%", arena.getArenaName())
                            )
                    ));

                    plugin.getGameManager().startGame(sender.getName(), new KOTHGame(plugin, createGameName, arena));
                    break;

            }
        } else if (args.length == 2) {

            switch (args[0].toLowerCase()) {

                case "join":

                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "Console cannot join a game.. please execute the same command from a player instance.");
                        return;
                    }

                    Player player = (Player) sender;

                    String joinGameName = args[1];
                    KOTHGame joinGame = plugin.getGameManager().getGameById(joinGameName);

                    if (joinGame == null) {
                        sender.sendMessage(ChatColor.RED + joinGameName + " is not an active game, try /koth listgames to find one!");
                        return;
                    }

                    if (joinGame.getCurrentPhase() != GamePhase.WAITING && joinGame.getCurrentPhase() != GamePhase.COUNTDOWN) {
                        sender.sendMessage(ChatColor.RED + joinGameName + " has already started.. please wait for the next game.");
                        return;
                    }

                    boolean alreadyInGame = false;

                    for (KOTHGame game : plugin.getGameManager().getActiveKOTHGames().values()) {
                        if (game.getActivePlayers().contains(player.getUniqueId())) {
                            alreadyInGame = true;
                            break;
                        }
                    }

                    if (alreadyInGame) {
                        MessageUtil.sendMessage(player, "alreadyingame");
                        return;
                    }

                    joinGame.addActivePlayer(player);
                    MessageUtil.sendMessage(player, "joingame",
                            Arrays.asList(
                                    new Placeholder("%gamename%", joinGameName),
                                    new Placeholder("%arenaname%", joinGame.getCurrentArena().getArenaName())));
                    break;
            }
        } else if (args.length == 1) {

            switch (args[0].toLowerCase()) {

                case "listarenas":
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Possible Maps:");
                    for (Arena arena : plugin.getArenaManager().getArenaCache().values()) {
                        sender.sendMessage(ChatColor.BLUE + arena.getArenaName());
                    }
                    break;

                case "listgames":
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Possible games:");
                    for (KOTHGame game : plugin.getGameManager().getActiveKOTHGames().values()) {
                        sender.sendMessage(ChatColor.BLUE + game.getGameId());
                    }
                    break;

                case "leave":

                    boolean successfullyLeft = false;

                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "Console cannot leave a game.. please execute the same command from a player instance.");
                        return;
                    }

                    Player player = (Player) sender;

                    for (KOTHGame game : plugin.getGameManager().getActiveKOTHGames().values()) {
                        if (game.getActivePlayers().contains(player.getUniqueId())) {

                            if (plugin.getScoreboardManager().getScoreboard(player) != null) {
                                plugin.getScoreboardManager().getScoreboard(player).remove();
                                plugin.getScoreboardManager().removeScoreboard(player);
                            }

                            game.removeActivePlayer(player);
                            game.handleEndCheck();
                            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                            MessageUtil.sendMessage(player, "leavegame", Collections.singletonList(new Placeholder("%gamename%", game.getGameId())));
                            successfullyLeft = true;
                        }
                    }

                    if (!successfullyLeft) {
                        MessageUtil.sendMessageToPlayer(player, "&7There was no game that you could leave..");
                    }
                    break;
            }
        }
    }
}
