package com.guildwars.classes.listeners;

import com.guildwars.classes.GuildWarsClasses;
import com.guildwars.classes.model.PlayerClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for player join/leave events to apply class effects.
 */
public class ClassSelectionListener implements Listener {

    private final GuildWarsClasses plugin;
    
    /**
     * Creates a new class selection listener.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public ClassSelectionListener(GuildWarsClasses plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Handle player join events to apply class effects.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Apply class effects if the player has a class
        PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
        if (playerClass != null) {
            // Schedule the effect application for the next tick to ensure the player is fully loaded
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                playerClass.applyEffects(player);
            }, 1L);
        }
    }
    
    /**
     * Handle player quit events to remove class effects.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Remove class effects if the player has a class
        PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
        if (playerClass != null) {
            playerClass.removeEffects(player);
        }
    }
}
