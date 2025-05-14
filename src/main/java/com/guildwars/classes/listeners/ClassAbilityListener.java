package com.guildwars.classes.listeners;

import com.guildwars.classes.GuildWarsClasses;
import com.guildwars.classes.model.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener for class ability triggers.
 */
public class ClassAbilityListener implements Listener {

    private final GuildWarsClasses plugin;
    
    /**
     * Creates a new class ability listener.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public ClassAbilityListener(GuildWarsClasses plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Handle player interaction events to trigger class abilities.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        
        // Check if the player has a class
        PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
        if (playerClass == null) {
            return;
        }
        
        // Handle right-click abilities
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            // Healer's primary ability - triggered by right-clicking with a stick
            if (playerClass.getTypeName().equals("healer") && item != null && item.getType() == Material.STICK) {
                event.setCancelled(true);
                playerClass.performPrimaryAbility(player);
            }
            
            // Orc's primary ability - triggered by right-clicking with an axe
            if (playerClass.getTypeName().equals("orc") && item != null && 
                (item.getType() == Material.WOODEN_AXE || 
                 item.getType() == Material.STONE_AXE || 
                 item.getType() == Material.IRON_AXE || 
                 item.getType() == Material.GOLDEN_AXE || 
                 item.getType() == Material.DIAMOND_AXE || 
                 item.getType() == Material.NETHERITE_AXE)) {
                
                // Don't cancel the event for Orc's ability to allow normal axe usage
                playerClass.performPrimaryAbility(player);
            }
        }
        
        // Handle left-click abilities (for future expansion)
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            // Currently no left-click abilities implemented
        }
    }
}
