package ru.sharepos;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class SharePosCommand implements CommandExecutor, TabCompleter {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TIME = 15000; // 15 seconds in milliseconds

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }

        Player player = (Player) sender;
        
        if (!player.hasPermission("sharepos.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        if (!player.isOp() && cooldowns.containsKey(playerId)) {
            long timeLeft = cooldowns.get(playerId) + COOLDOWN_TIME - currentTime;
            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "Please wait " + (timeLeft / 1000 + 1) + " seconds before using this command again!");
                return true;
            }
        }

        Location loc = player.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();
        
        String coordinates = x + ", " + y + ", " + z;

        TextComponent message = new TextComponent(ChatColor.YELLOW + "Player " + ChatColor.GOLD + player.getName() + 
                                                ChatColor.YELLOW + " shared location: ");
        
        TextComponent coordsComponent = new TextComponent(ChatColor.GREEN + "[" + coordinates + "]");
        coordsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coordinates));
        coordsComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder(ChatColor.AQUA + "Click to copy coordinates\n" +
                               ChatColor.GRAY + "World: " + world).create()));

        message.addExtra(coordsComponent);

        if (args.length == 0) {
            if (!player.hasPermission("sharepos.all")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to share coordinates with all players!");
                player.sendMessage(ChatColor.YELLOW + "Use: /sharepos <player> or /sharepos help");
                return true;
            }
            
            if (!player.isOp()) {
                cooldowns.put(playerId, currentTime);
            }
            
            for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    onlinePlayer.spigot().sendMessage(message);
                }
            }
            player.sendMessage(ChatColor.GREEN + "Coordinates sent to all players!");
            
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GOLD + "======== SharePos Help (OP) ========");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos" + ChatColor.WHITE + " - share your coordinates with all players");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos <player>" + ChatColor.WHITE + " - share your coordinates with specific player");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos all" + ChatColor.WHITE + " - share your coordinates with all players");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos <target> all" + ChatColor.WHITE + " - share target's coordinates with all players");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos help" + ChatColor.WHITE + " - show this help");
                    player.sendMessage(ChatColor.GRAY + "Aliases: /sp, /pos");
                    player.sendMessage(ChatColor.AQUA + "Click on coordinates to copy them to clipboard!");
                    player.sendMessage(ChatColor.GREEN + "OP Privileges: No cooldown, can share other players' coordinates");
                    player.sendMessage(ChatColor.GRAY + "Plugin made by: SHTUKA");
                } else {
                    player.sendMessage(ChatColor.GOLD + "========== SharePos Help ==========");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos <player>" + ChatColor.WHITE + " - share your coordinates with specific player");
                    player.sendMessage(ChatColor.YELLOW + "/sharepos help" + ChatColor.WHITE + " - show this help");
                    if (player.hasPermission("sharepos.all")) {
                        player.sendMessage(ChatColor.YELLOW + "/sharepos" + ChatColor.WHITE + " - share your coordinates with all players");
                        player.sendMessage(ChatColor.YELLOW + "/sharepos all" + ChatColor.WHITE + " - share your coordinates with all players");
                    }
                    player.sendMessage(ChatColor.GRAY + "Aliases: /sp, /pos");
                    player.sendMessage(ChatColor.AQUA + "Click on coordinates to copy them to clipboard!");
                    player.sendMessage(ChatColor.RED + "Cooldown: 15 seconds between uses");
                    player.sendMessage(ChatColor.GRAY + "Plugin made by: SHTUKA");
                }
                return true;
                
            } else if (args[0].equalsIgnoreCase("all")) {
                if (!player.hasPermission("sharepos.all")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to share coordinates with all players!");
                    return true;
                }
                
                if (!player.isOp()) {
                    cooldowns.put(playerId, currentTime);
                }
                
                for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                    if (!onlinePlayer.equals(player)) {
                        onlinePlayer.spigot().sendMessage(message);
                    }
                }
                player.sendMessage(ChatColor.GREEN + "Coordinates sent to all players!");
                
            } else {
                Player target = player.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player " + args[0] + " not found!");
                    return true;
                }
                
                if (target.equals(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot send coordinates to yourself!");
                    return true;
                }
                
                if (!player.isOp()) {
                    cooldowns.put(playerId, currentTime);
                }
                
                target.spigot().sendMessage(message);
                player.sendMessage(ChatColor.GREEN + "Coordinates sent to player " + target.getName() + "!");
            }
        } else if (args.length == 2 && player.isOp()) {
            if (args[1].equalsIgnoreCase("all")) {
                Player targetPlayer = player.getServer().getPlayer(args[0]);
                if (targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "Player " + args[0] + " not found!");
                    return true;
                }
                
                Location targetLoc = targetPlayer.getLocation();
                int targetX = targetLoc.getBlockX();
                int targetY = targetLoc.getBlockY();
                int targetZ = targetLoc.getBlockZ();
                String targetWorld = targetLoc.getWorld().getName();
                
                String targetCoordinates = targetX + ", " + targetY + ", " + targetZ;
                
                TextComponent targetMessage = new TextComponent(ChatColor.YELLOW + "Player " + ChatColor.GOLD + targetPlayer.getName() + 
                                                        ChatColor.YELLOW + " location shared by " + ChatColor.GOLD + player.getName() + 
                                                        ChatColor.YELLOW + ": ");
                
                TextComponent targetCoordsComponent = new TextComponent(ChatColor.GREEN + "[" + targetCoordinates + "]");
                targetCoordsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, targetCoordinates));
                targetCoordsComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                    new ComponentBuilder(ChatColor.AQUA + "Click to copy coordinates\n" +
                                       ChatColor.GRAY + "World: " + targetWorld).create()));
                
                targetMessage.addExtra(targetCoordsComponent);
                
                for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                    if (!onlinePlayer.equals(player)) {
                        onlinePlayer.spigot().sendMessage(targetMessage);
                    }
                }
                
                player.sendMessage(ChatColor.GREEN + "Shared " + targetPlayer.getName() + "'s coordinates with all players!");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /sharepos [player] or /sharepos all or /sharepos help");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }

        Player player = (Player) sender;
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            
            if ("help".startsWith(input)) {
                completions.add("help");
            }
            if ("all".startsWith(input)) {
                completions.add("all");
            }
            
            for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                if (!onlinePlayer.equals(sender) && onlinePlayer.getName().toLowerCase().startsWith(input)) {
                    completions.add(onlinePlayer.getName());
                }
            }
        } else if (args.length == 2 && player.isOp()) {
            String input = args[1].toLowerCase();
            if ("all".startsWith(input)) {
                completions.add("all");
            }
        }
        
        return completions;
    }
}