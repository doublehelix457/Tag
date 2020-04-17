package com.doublehelix;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tag"))
        {
            if (args.length == 0)
            {
                TagUtil.sendTagMessage(player,ChatColor.BLUE + "You are running doublehelix457's tag version 1.0-SNAPSHOT.");
                TagUtil.sendTagMessage(player,ChatColor.BLUE + "Usage of tag command: /tag <option>");
                TagUtil.sendTagMessage(player,ChatColor.BLUE + "Options: join, leave");
                TagUtil.sendTagMessage(player,ChatColor.BLUE + "Staff options: version, setlocation, start, stop, kick <player>");
                return true;
            }
            if(args[0].equalsIgnoreCase("create")){
                if(sender.hasPermission("tag.create")) {
                    if (!TagManager.doesGameExsist() && !TagManager.isGameRunning()) {
                        TagUtil.sendTagMessage(player, "");
                        TagManager.createGame(player);
                        return true;
                    } else {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is already an exsisting game of Tag!");
                        return true;
                    }
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to create a game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("start"))
            {
                if (sender.hasPermission("tag.start"))
                {
                    if(TagManager.isGameRunning())
                    {
                        TagUtil.sendTagMessage(player,ChatColor.RED + "There is already a game of tag started.");
                        return true;
                    }
                    String msg = TagManager.startGame();
                    if(msg != null){
                        TagUtil.sendTagMessage(player, msg);
                    }
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to start a game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("stop"))
            {
                if (sender.hasPermission("tag.stop"))
                {
                    TagManager.endGame();
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to stop the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("join"))
            {
                if (sender.hasPermission("tag.join"))
                {

                    TagUtil.sendTagMessage(player, ChatColor.RED + "You have successfully joined the tag game.");
                    TagManager.addPlayerToGame(player);
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to join the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("leave"))
            {
                if (sender.hasPermission("tag.leave"))
                {
                    if(TagManager.getTaggers().contains(player))
                    {
                        TagManager.quitter(player);
                        return true;
                    }
                    TagUtil.sendTagMessage(player, ChatColor.RED + "You're not in a game of tag, silly!");
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to leave the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("setspawn"))
            {
                if (sender.hasPermission("tag.settaglocation"))
                {
                    Location loc = (player).getLocation();
                    TagUtil.setTagSpawn(loc);
                    TagUtil.sendTagMessage(player, ChatColor.RED + "The tag location has been set!");
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to stop the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("kick"))
            {
                if (sender.hasPermission("tag.kick"))
                {
                    if (args.length == 1)
                    {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "Please select a player to kick");
                        TagUtil.sendTagMessage(player, ChatColor.BLUE + "Usage: /tag kick <player>");
                        TagUtil.sendTagMessage(player, ChatColor.BLUE + "Kicks a player from the tag game.");
                        return true;
                    }
                    String playerto = args[1];
                    Player kickee = TagUtil.getPlayerFromString(playerto);
                    if (kickee != null)
                    {
                        TagManager.kickPlayer(kickee);
                        return true;
                    }
                    TagUtil.sendTagMessage(player, "Can't find player.");
                    return false;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to stop the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("version"))
            {
                TagUtil.sendTagMessage(player, ChatColor.GOLD + "You are running Hotshot devs Tag Plugin Version 1.0");
                return true;
            }
            return false;
        }
        return false;
    }
}
