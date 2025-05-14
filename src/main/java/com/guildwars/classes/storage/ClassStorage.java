package com.guildwars.classes.storage;

import com.guildwars.classes.GuildWarsClasses;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Handles saving and loading player class data.
 */
public class ClassStorage {

    private final GuildWarsClasses plugin;
    private final File classesFile;
    private FileConfiguration classesConfig;
    
    /**
     * Creates a new class storage.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public ClassStorage(GuildWarsClasses plugin) {
        this.plugin = plugin;
        
        // Create data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Initialize files
        classesFile = new File(plugin.getDataFolder(), "classes.yml");
        
        // Load configuration
        loadConfiguration();
    }
    
    /**
     * Load the configuration file.
     */
    private void loadConfiguration() {
        try {
            // Create file if it doesn't exist
            if (!classesFile.exists()) {
                classesFile.createNewFile();
            }
            
            // Load configuration
            classesConfig = YamlConfiguration.loadConfiguration(classesFile);
            
            plugin.getLogger().info("YAML configuration loaded successfully.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create YAML file", e);
        }
    }
    
    /**
     * Save the configuration file.
     */
    private void saveConfiguration() {
        try {
            classesConfig.save(classesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save YAML file", e);
        }
    }
    
    /**
     * Load all player classes from storage.
     *
     * @return A map of player UUIDs to class IDs
     */
    public Map<UUID, String> loadPlayerClasses() {
        Map<UUID, String> playerClasses = new HashMap<>();
        
        if (classesConfig.contains("players")) {
            for (String uuidStr : classesConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(uuidStr);
                    String classId = classesConfig.getString("players." + uuidStr);
                    
                    if (classId != null && !classId.isEmpty()) {
                        playerClasses.put(playerId, classId);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in classes.yml: " + uuidStr);
                }
            }
        }
        
        return playerClasses;
    }
    
    /**
     * Save a player's class to storage.
     *
     * @param playerId The UUID of the player
     * @param classId The ID of the class
     */
    public void savePlayerClass(UUID playerId, String classId) {
        classesConfig.set("players." + playerId.toString(), classId);
        saveConfiguration();
    }
    
    /**
     * Remove a player's class from storage.
     *
     * @param playerId The UUID of the player
     */
    public void removePlayerClass(UUID playerId) {
        classesConfig.set("players." + playerId.toString(), null);
        saveConfiguration();
    }
    
    /**
     * Save all data to storage.
     */
    public void saveData() {
        saveConfiguration();
        plugin.getLogger().info("All class data saved to YAML file.");
    }
}
