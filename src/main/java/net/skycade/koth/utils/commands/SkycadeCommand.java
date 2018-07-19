package net.skycade.koth.utils.commands;

import net.skycade.koth.SkycadeKoth;
import net.skycade.koth.utils.commands.components.PermissibleCommand;
import net.skycade.koth.utils.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
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
public abstract class SkycadeCommand extends BukkitCommand {

    private SkycadeKoth plugin;

    public SkycadeCommand(SkycadeKoth plugin, String name) {
        super(name);
        this.plugin = plugin;

        registerCommand();
    }

    public SkycadeCommand(SkycadeKoth plugin, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;

        registerCommand();
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            onCommand(sender, args);
            return false;
        }

        Player player = (Player) sender;

        if (this.getClass().isAnnotationPresent(PermissibleCommand.class) && !player.hasPermission(this.getClass().getAnnotation(PermissibleCommand.class).value())) {
            MessageUtil.sendMessage(player, "noPermission");
            return false;
        }

        onCommand(sender, args);
        return false;
    }

    private void registerCommand() {

        try {

            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap)field.get(Bukkit.getServer());

            if (this.getClass().isAnnotationPresent(PermissibleCommand.class)) {
                setPermission(this.getClass().getAnnotation(PermissibleCommand.class).value());
            }

            commandMap.register(this.getName(), this);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }
}
