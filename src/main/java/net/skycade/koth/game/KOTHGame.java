package net.skycade.koth.game;

import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.events.phase.PhaseChangeEvent;
import net.skycade.koth.events.zone.EnterCaptureZoneEvent;
import net.skycade.koth.events.zone.ExitCaptureZoneEvent;
import net.skycade.koth.game.arena.Arena;
import net.skycade.koth.game.countdown.Countdown;
import net.skycade.koth.utils.LocationUtil;
import net.skycade.koth.utils.messages.MessageUtil;
import net.skycade.koth.utils.placeholder.Placeholder;
import net.skycade.koth.utils.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
public class KOTHGame implements Listener {

    private SkycadeKoth plugin;

    /** ID for the game.. typically a name. */
    private String gameId;

    /** List of players in the KOTH gamemode */
    private List<UUID> activePlayers;

    /** Current arena being used in the game. */
    private Arena currentArena;

    /** Current phase of the game */
    private GamePhase currentPhase;

    /** Queue of players capturing the area. */
    private Queue<UUID> currentlyWithinBoundaries;

    /** Current duration for capturing at the current time. */
    private int currentCaptureTime;

    public KOTHGame(SkycadeKoth plugin, String gameId, Arena currentArena) {
        this.plugin = plugin;
        this.gameId = gameId;
        this.currentArena = currentArena;

        this.activePlayers = new ArrayList<>();

        currentlyWithinBoundaries = new LinkedList<>();

        currentCaptureTime = (60 * 15);

        setCurrentPhase(GamePhase.WAITING);
    }

    public void startCountdown() {

        setCurrentPhase(GamePhase.COUNTDOWN);

        // Phase countdown

        plugin.getCountdownManager().startCountdown(
                new Countdown(plugin, "start_game", 60, new int[] { 1, 2, 3, 4, 5, 10, 15, 30, 60 }),
                currentValue -> getActivePlayers().forEach(uuid -> MessageUtil.sendMessage(Bukkit.getPlayer(uuid), "countdown", Collections.singletonList(new Placeholder("%seconds%", currentValue)))), finished -> {

                    // Start the game and set it to in progress.
                    setCurrentPhase(GamePhase.IN_PROGRESS);

                    getActivePlayers().forEach(uuid -> {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            player.teleport(currentArena.getSpawnLocation());
                            MessageUtil.sendMessage(player, "gamestarted", Arrays.asList(
                                    new Placeholder("%gamename%", gameId),
                                    new Placeholder("%arenaname%", currentArena.getArenaName())
                            ));

                            plugin.getScoreboardManager().addScoreboard(new Scoreboard(player, "&b&lSky&f&lcade &3&lKOTH",
                                    new String[] {
                                            "",
                                            "&7Goodluck &b" + player.getName(),
                                            " ",
                                            ChatColor.GREEN.toString(), // Capturing identifier
                                            ChatColor.DARK_PURPLE.toString(), // Capturing Faction identifier
                                            "  ",
                                            ChatColor.RED.toString(), // Remaining time identifier
                                            "   ",
                                    }));

                            plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturing", ChatColor.GREEN.toString(), "&7Capturing: ", "&aNONE");
                            plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturingfac", ChatColor.DARK_PURPLE.toString(), "&7Faction: ", "&aNONE");
                            plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("remainingtime", ChatColor.RED.toString(), "&7Time Left: ", "&aLoading..");
                        }
                    });
                }
        );
    }

    private void resetCountdown() {

        if (plugin.getCountdownManager().getCountdown("capture_" + gameId) != null) {
            Countdown countdown = plugin.getCountdownManager().getCountdown("capture_" + gameId);
            countdown.stop();
            plugin.getCountdownManager().removeCountdown("capture_" + gameId);
        }

        plugin.getCountdownManager().startCountdown(new Countdown(plugin, "capture_" + gameId, currentCaptureTime + 1, true), intervals -> {
            getActivePlayers().forEach(uuid -> {

                int minutes = (intervals % 3600) / 60;
                int seconds = intervals % 60;

                Player capturing = Bukkit.getPlayer(currentlyWithinBoundaries.peek());
                String faction = MPlayer.get(capturing.getUniqueId()).getFactionName();

                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturing", ChatColor.GREEN.toString(), "&7Capturing: ", capturing.getName());
                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturingfac", ChatColor.DARK_PURPLE.toString(), "&7Faction: ", "&a" + (faction.length() < 1 ? "NONE" : faction));
                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("remainingtime", ChatColor.RED.toString(), "&7Time Left: ", "&c" + minutes + ":" + seconds);
            });

            if (intervals % 60 == 0) {
                getActivePlayers().forEach(uuid -> {
                    Player player = Bukkit.getPlayer(uuid);
                    MessageUtil.sendMessageToPlayer(player, (currentlyWithinBoundaries.peek() == null ? "No one" : Bukkit.getPlayer(currentlyWithinBoundaries.peek()).getName()) + " is capturing the zone.. " + (intervals / 60) + " minutes left..");
                });
            }
        }, finished -> {
            Player winner = Bukkit.getPlayer(currentlyWithinBoundaries.peek());
            getActivePlayers().forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                MessageUtil.sendMessageToPlayer(player, winner.getName() + " successfully captured the zone.. loot inbound..");
                plugin.getScoreboardManager().getScoreboard(player).remove();
                plugin.getScoreboardManager().removeScoreboard(player);
            });

            // TODO make a KOTH key..
            winner.getInventory().addItem(new ItemStack(Material.TRIPWIRE_HOOK));
            plugin.getGameManager().endGame(this);
        });
    }

    public String getGameId() {
        return gameId;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void addActivePlayer(Player player) {
        if (activePlayers.contains(player.getUniqueId())) return;
        activePlayers.add(player.getUniqueId());
    }

    public void removeActivePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
    }

    public List<UUID> getActivePlayers() {
        return activePlayers;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentPhase(GamePhase newPhease) {
        Bukkit.getPluginManager().callEvent(new PhaseChangeEvent(this, currentPhase, newPhease));
        currentPhase = newPhease;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        // Stops checking for players not in this game..
        if (!getActivePlayers().contains(player.getUniqueId())) return;

        // Stops on the block checking..
        if (from == to) return;

        if (getCurrentPhase() != GamePhase.IN_PROGRESS) return;

        if (!LocationUtil.isWithinLocation(to, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2()) && LocationUtil.isWithinLocation(from, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2())) {
            Bukkit.getPluginManager().callEvent(new ExitCaptureZoneEvent(player, this));
        } else if (!LocationUtil.isWithinLocation(from, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2()) && LocationUtil.isWithinLocation(to, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2())) {
            Bukkit.getPluginManager().callEvent(new EnterCaptureZoneEvent(player, this));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        // Stops checking for players not in this game..
        if (!getActivePlayers().contains(player.getUniqueId())) return;

        // Stops on the block checking..
        if (from == to) return;

        if (getCurrentPhase() != GamePhase.IN_PROGRESS) return;

        if (!LocationUtil.isWithinLocation(to, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2()) && LocationUtil.isWithinLocation(from, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2())) {
            Bukkit.getPluginManager().callEvent(new ExitCaptureZoneEvent(player, this));
        } else if (!LocationUtil.isWithinLocation(from, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2()) && LocationUtil.isWithinLocation(to, getCurrentArena().getArenaBoundaryPoint1(), getCurrentArena().getArenaBoundaryPoint2())) {
            Bukkit.getPluginManager().callEvent(new EnterCaptureZoneEvent(player, this));
        }
    }

    @EventHandler
    public void onEnterCapture(EnterCaptureZoneEvent event) {

        Player player = event.getPlayer();

        if (!currentlyWithinBoundaries.contains(player.getUniqueId())) {

            if (currentlyWithinBoundaries.peek() == null) {
                resetCountdown();
            }

            currentlyWithinBoundaries.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onExitCapture(ExitCaptureZoneEvent event) {
        Player player = event.getPlayer();

        if (currentlyWithinBoundaries.size() > 1 && currentlyWithinBoundaries.peek() != null && currentlyWithinBoundaries.peek().equals(player.getUniqueId())) {
            currentlyWithinBoundaries.poll();
            Bukkit.broadcastMessage("There is now a new person collecting points.. " + Bukkit.getPlayer(currentlyWithinBoundaries.peek()).getName());
            resetCountdown();
            return;
        }

        currentlyWithinBoundaries.remove(player.getUniqueId());

        if (currentlyWithinBoundaries.size() == 0) {
            plugin.getCountdownManager().getCountdown("capture_" + gameId).stop();
            plugin.getCountdownManager().getCountdown("capture_" + gameId).setCurrentTime(currentCaptureTime);

            getActivePlayers().forEach(uuid -> {

                int minutes = (currentCaptureTime % 3600) / 60;
                int seconds = currentCaptureTime % 60;

                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturing", ChatColor.GREEN.toString(), "&7Capturing: ", "&aNONE");
                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("capturingfac", ChatColor.DARK_PURPLE.toString(), "&7Faction: ", "&aNONE");
                plugin.getScoreboardManager().getScoreboard(Bukkit.getPlayer(uuid)).updateTeam("remainingtime", ChatColor.RED.toString(), "&7Time Left: ", "&c" + minutes + ":" + seconds);
            });
        }
    }

    @EventHandler
    public void onPhaseChange(PhaseChangeEvent event) {
        Bukkit.broadcastMessage("New phase: " + event.getNewPhase().name());

        if (event.getNewPhase() == GamePhase.IN_PROGRESS) {
            // Decrease time over time (shorter as time goes on..)
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getScoreboardManager().getScoreboard(player).remove();
        plugin.getScoreboardManager().removeScoreboard(player);
    }
}
