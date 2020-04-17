package com.doublehelix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class TagManager {
    private static Map<Player, Location> last_locations = new HashMap();
    private static boolean gameRunning = false;
    private static boolean gameCreated = false;
    private static Location spawn;
    private static  ArrayList<Player> taggers = new ArrayList<>();
    private static Player IT;
    private static Player host;

    public static boolean isGameRunning() { return gameRunning; }
    public static boolean doesGameExsist() { return gameCreated; }
    public static Location getTagSpawn() { return spawn; }
    public static ArrayList<Player> getTaggers(){return taggers;}
    public static Player getIt(){return IT;}
    public static boolean isIt(Player tagger){return tagger == IT;}

    public static void createGame(Player creator){
        host = creator;
        spawn = TagUtil.getTagSpawn();
        gameCreated = true;
        host.sendMessage("Tag Lobby Started, get some players to join!");
        TagUtil.sendTagMessageToAll(host.getName() + " just started a game of Tag! Use /tag join or the join tag sign to play!");
    }

    public static String startGame(){
        if(taggers.size() < 2){
            return ChatColor.RED + "Not Enough Players in this game!";
        }
        IT = TagUtil.getRandomIt();
        for(Player p : taggers) {
            if(p != IT){
                last_locations.put(p, p.getLocation());
                p.teleport(spawn);
            }

        }
        TagUtil.sendMessageToPlayers(IT.getName() + "is it! You have 5 seconds. Start running!!!");
        Runnable delayTP = () -> {
            synchronized (last_locations) {
                last_locations.put(IT, IT.getLocation());
            }
            IT.teleport(spawn);
            TagUtil.sendMessageToPlayers(IT.getName() + "is on the hunt!");
        };
        Bukkit.getScheduler().runTaskLater(TagPlugin.inst(), delayTP, 6000);
        return null;
    }

    public static void endGame(){
        TagUtil.sendTagMessageToAll(ChatColor.RED + "The game has ended! Thanks for playing!");
        if(TagManager.doesGameExsist()){
            gameCreated = false;
        }
        if(TagManager.isGameRunning()) {
            for (Player p : taggers) {
                p.teleport(last_locations.get(p));
            }
            last_locations.clear();
        }
        taggers.clear();
        IT = null;
        gameRunning = false;
    }

    public static void kickPlayer(Player p){
        taggers.remove(p);
        p.teleport(last_locations.get(p));
        last_locations.remove(p);
        TagUtil.sendTagMessage(p, ChatColor.RED + "You have been kicked from the game.");
        if(taggers.size() < 2){
            endGame();
            return;
        }
        if(p == IT){
            IT = TagUtil.getRandomIt();
            TagUtil.sendMessageToPlayers(p.getName() + " has left the game! " + IT.getName() + " is now IT!");
        }
    }

    public static void quitter(Player p){
        taggers.remove(p);
        p.teleport(last_locations.get(p));
        last_locations.remove(p);
        TagUtil.sendTagMessage(p, ChatColor.RED + "You have left the game.");
        if(taggers.size() < 2){
            endGame();
            return;
        }
        if(p == IT){
            IT = TagUtil.getRandomIt();
            TagUtil.sendMessageToPlayers(p.getName() + " has left the game! " + IT.getName() + " is now IT!");
        }
    }

    public static void addPlayerToGame(Player p){
        if(gameCreated){
            taggers.add(p);
            if(gameRunning){
                last_locations.put(p, p.getLocation());
                p.teleport(spawn);
                TagUtil.sendMessageToPlayers(p.getName() + " has joined Tag! Better late than never I guess...");
                return;
            }
            TagUtil.sendMessageToPlayers(p.getName() + " has joined Tag!");
        }else{
            TagUtil.sendTagMessage(p, ChatColor.RED + "There is no running game of tag. Ask a staff member to create a game.");
        }
    }

}
