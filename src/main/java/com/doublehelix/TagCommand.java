package com.doublehelix;

import org.bukkit.Bukkit;
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
                TagUtil.sendTagMessage(player,ChatColor.GOLD + "You are running doublehelix457's tag version 1.0-SNAPSHOT.");
                TagUtil.sendTagMessage(player,ChatColor.GOLD + "Usage of tag command: /tag <option>");
                return true;
            }
            if(args[0].equalsIgnoreCase("create")){
                if(sender.hasPermission("tag.create")) {
                    if (!TagManager.doesGameExsist() && !TagManager.isGameRunning()) {
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
                    if(!TagManager.doesGameExsist() || !TagManager.isGameRunning()) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no running game to stop.");
                        return true;
                    }
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
                    if(!TagManager.doesGameExsist()) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no running game of tag. Ask a staff member to create a game.");
                        return true;
                    }
                    if(!TagManager.getTaggers().contains(player)) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "You have successfully joined the tag game.");
                        TagManager.addPlayerToGame(player);
                        return true;
                    }else{
                        TagUtil.sendTagMessage(player, ChatColor.RED + "You are already in this game of tag!");
                        return true;
                    }
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
                    TagUtil.sendTagMessage(player, ChatColor.RED + "You're not in a game of tag.");
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to leave the game!");
                return true;
            }
            if (args[0].equalsIgnoreCase("setspawn"))
            {
                if (sender.hasPermission("tag.setspawn"))
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
                    if(!TagManager.doesGameExsist() || !TagManager.isGameRunning()){
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no game of tag running!");
                    }
                    if (args.length == 1)
                    {
                        TagUtil.sendTagMessage(player, ChatColor.GREEN + "Usage: /tag kick <player>");
                        return true;
                    }
                    Player kickee = Bukkit.getServer().getPlayer(args[1]);
                    if (kickee != null)
                    {
                        if(!TagManager.getTaggers().contains(kickee)){
                            TagUtil.sendTagMessage(player,ChatColor.RED + "That player is not playing tag!");
                            return true;
                        }
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
                TagUtil.sendTagMessage(player, ChatColor.GOLD + "Tag Version: 1.0-SNAPSHOT by doublehelix457");
                return true;
            }
            return false;
        }
        return false;
    }
}
