package com.guildwars.classes.managers;

import com.guildwars.classes.GuildWarsClasses;
import com.guildwars.classes.integration.GuildIntegration;
import com.guildwars.classes.model.HealerClass;
import com.guildwars.classes.model.OrcClass;
import com.guildwars.classes.model.PlayerClass;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player class assignments and class-related operations.
 */
public class ClassManager {

    private final GuildWarsClasses plugin;
    private final GuildIntegration guildIntegration;
    private final Map<UUID, PlayerClass> playerClasses;
    private final List<PlayerClass> availableClasses;
    
    /**
     * Creates a new class manager.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public ClassManager(GuildWarsClasses plugin) {
        this.plugin = plugin;
        this.guildIntegration = plugin.getGuildIntegration();
        this.playerClasses = new HashMap<>();
        this.availableClasses = new ArrayList<>();
        
        // Register available classes
        registerClasses();
        
        // Load player classes from storage
        loadPlayerClasses();
        
        // Apply class effects to online players
        applyClassEffectsToOnlinePlayers();
    }
    
    /**
     * Register all available classes.
     */
    private void registerClasses() {
        // Register Orc class
        availableClasses.add(new OrcClass());
        
        // Register Healer class
        availableClasses.add(new HealerClass());
        
        plugin.getLogger().info("Registered " + availableClasses.size() + " classes.");
    }
    
    /**
     * Load player classes from storage.
     */
    private void loadPlayerClasses() {
        Map<UUID, String> storedClasses = plugin.getClassStorage().loadPlayerClasses();
        
        for (Map.Entry<UUID, String> entry : storedClasses.entrySet()) {
            UUID playerId = entry.getKey();
            String classId = entry.getValue();
            
            PlayerClass playerClass = getClassById(classId);
            if (playerClass != null) {
                playerClasses.put(playerId, playerClass);
            }
        }
        
        plugin.getLogger().info("Loaded " + playerClasses.size() + " player classes from storage.");
    }
    
    /**
     * Apply class effects to all online players.
     */
    private void applyClassEffectsToOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerClass playerClass = getPlayerClass(player.getUniqueId());
            if (playerClass != null) {
                playerClass.applyEffects(player);
            }
        }
    }
    
    /**
     * Get a class by its ID.
     *
     * @param classId The ID of the class
     * @return The class, or null if not found
     */
    public PlayerClass getClassById(String classId) {
        for (PlayerClass playerClass : availableClasses) {
            if (playerClass.getId().equalsIgnoreCase(classId)) {
                return playerClass;
            }
        }
        return null;
    }
    
    /**
     * Get a player's class.
     *
     * @param playerId The UUID of the player
     * @return The player's class, or null if they don't have one
     */
    public PlayerClass getPlayerClass(UUID playerId) {
        return playerClasses.get(playerId);
    }
    
    /**
     * Set a player's class.
     *
     * @param player The player
     * @param classId The ID of the class to set
     * @return True if the class was set successfully, false otherwise
     */
    public boolean setPlayerClass(Player player, String classId) {
        UUID playerId = player.getUniqueId();
        
        // Remove current class effects if they have one
        PlayerClass currentClass = getPlayerClass(playerId);
        if (currentClass != null) {
            currentClass.removeEffects(player);
        }
        
        // Get the new class
        PlayerClass newClass = getClassById(classId);
        if (newClass == null) {
            return false;
        }
        
        // Set the new class
        playerClasses.put(playerId, newClass);
        
        // Apply the new class effects
        newClass.applyEffects(player);
        
        // Apply guild bonuses if applicable
        if (guildIntegration != null && guildIntegration.isEnabled()) {
            guildIntegration.applyGuildBonuses(player, newClass);
        }
        
        // Save to storage
        plugin.getClassStorage().savePlayerClass(playerId, classId);
        
        return true;
    }
    
    /**
     * Remove a player's class.
     *
     * @param player The player
     * @return True if the class was removed successfully, false otherwise
     */
    public boolean removePlayerClass(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Remove current class effects if they have one
        PlayerClass currentClass = getPlayerClass(playerId);
        if (currentClass != null) {
            currentClass.removeEffects(player);
            playerClasses.remove(playerId);
            
            // Remove from storage
            plugin.getClassStorage().removePlayerClass(playerId);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Get all available classes.
     *
     * @return A list of all available classes
     */
    public List<PlayerClass> getAvailableClasses() {
        return new ArrayList<>(availableClasses);
    }
    
    /**
     * Perform the primary ability for a player's class.
     *
     * @param player The player
     * @return True if the ability was used successfully, false otherwise
     */
    public boolean performPrimaryAbility(Player player) {
        PlayerClass playerClass = getPlayerClass(player.getUniqueId());
        if (playerClass != null) {
            // Check if player is in a guild for potential bonuses
            if (guildIntegration != null && guildIntegration.isEnabled() && guildIntegration.isPlayerInGuild(player)) {
                // Apply guild-specific ability bonuses
                if (playerClass.getTypeName().equals("healer")) {
                    player.sendMessage(ChatColor.GOLD + "[Guild Bonus] " + ChatColor.GREEN + "Your healing abilities are enhanced by your guild membership!");
                } else if (playerClass.getTypeName().equals("orc")) {
                    player.sendMessage(ChatColor.GOLD + "[Guild Bonus] " + ChatColor.GREEN + "Your strength abilities are enhanced by your guild membership!");
                }
            }
            return playerClass.performPrimaryAbility(player);
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a class!");
            return false;
        }
    }
    
    /**
     * Perform the secondary ability for a player's class.
     *
     * @param player The player
     * @return True if the ability was used successfully, false otherwise
     */
    public boolean performSecondaryAbility(Player player) {
        PlayerClass playerClass = getPlayerClass(player.getUniqueId());
        if (playerClass != null) {
            // Check if player is in a guild for potential bonuses
            if (guildIntegration != null && guildIntegration.isEnabled() && guildIntegration.isPlayerInGuild(player)) {
                // Apply guild-specific ability bonuses
                if (playerClass.getTypeName().equals("healer")) {
                    player.sendMessage(ChatColor.GOLD + "[Guild Bonus] " + ChatColor.GREEN + "Your healing aura is enhanced by your guild membership!");
                } else if (playerClass.getTypeName().equals("orc")) {
                    player.sendMessage(ChatColor.GOLD + "[Guild Bonus] " + ChatColor.GREEN + "Your ground slam is enhanced by your guild membership!");
                }
            }
            return playerClass.performSecondaryAbility(player);
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a class!");
            return false;
        }
    }
}
