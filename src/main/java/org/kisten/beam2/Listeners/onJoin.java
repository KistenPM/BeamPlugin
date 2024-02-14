package org.kisten.beam2.Listeners;

import org.kisten.beam2.TimeGetter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {
    public static Boolean done = false;
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (!done) {
            new TimeGetter();
            done = true;
        }
    }

}
