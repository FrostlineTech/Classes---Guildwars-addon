package com.guildwars.classes;

import com.guildwars.GuildWars;
import com.guildwars.classes.commands.ClassCommand;
import com.guildwars.classes.integration.GuildIntegration;
import com.guildwars.classes.listeners.ClassAbilityListener;
import com.guildwars.classes.listeners.ClassSelectionListener;
import com.guildwars.classes.managers.ClassManager;
import com.guildwars.classes.storage.ClassStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the GuildWars Classes addon.
 * This addon adds player classes with unique abilities to the GuildWars plugin.
 */
public class GuildWarsClasses extends JavaPlugin {

    private static GuildWarsClasses instance;
    private ClassStorage classStorage;
    private ClassManager classManager;
    private GuildIntegration guildIntegration;
    private GuildWars guildWarsPlugin;

    @Override
    public void onEnable() {
        // Set instance
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Check for GuildWars plugin
        if (!setupGuildWars()) {
            getLogger().severe("GuildWars plugin not found! Disabling GuildWars Classes addon.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Initialize storage
        classStorage = new ClassStorage(this);
        
        // Initialize guild integration
        guildIntegration = new GuildIntegration(this);
        
        // Initialize class manager
        classManager = new ClassManager(this);
        
        // Register commands
        registerCommands();
        
        // Register event listeners
        registerListeners();
        
        getLogger().info("GuildWars Classes addon has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save data when plugin is disabled
        if (classStorage != null) {
            classStorage.saveData();
        }
        
        getLogger().info("GuildWars Classes addon has been disabled!");
    }
    
    /**
     * Setup the GuildWars plugin dependency.
     * 
     * @return true if GuildWars plugin is found, false otherwise
     */
    private boolean setupGuildWars() {
        if (getServer().getPluginManager().getPlugin("GuildWars") == null) {
            return false;
        }
        
        guildWarsPlugin = (GuildWars) getServer().getPluginManager().getPlugin("GuildWars");
        return true;
    }
    
    /**
     * Register all commands for the plugin.
     */
    private void registerCommands() {
        // Register class command
        getCommand("class").setExecutor(new ClassCommand(this));
        getLogger().info("Commands registered successfully.");
    }
    
    /**
     * Register all event listeners for the plugin.
     */
    private void registerListeners() {
        // Register class ability listener
        Bukkit.getPluginManager().registerEvents(new ClassAbilityListener(this), this);
        
        // Register class selection listener
        Bukkit.getPluginManager().registerEvents(new ClassSelectionListener(this), this);
        
        getLogger().info("Event listeners registered successfully.");
    }
    
    /**
     * Get the plugin instance.
     * 
     * @return The plugin instance
     */
    public static GuildWarsClasses getInstance() {
        return instance;
    }
    
    /**
     * Get the class storage.
     * 
     * @return The class storage
     */
    public ClassStorage getClassStorage() {
        return classStorage;
    }
    
    /**
     * Get the class manager.
     * 
     * @return The class manager
     */
    public ClassManager getClassManager() {
        return classManager;
    }
    
    /**
     * Get the GuildWars plugin instance.
     * 
     * @return The GuildWars plugin instance
     */
    public GuildWars getGuildWarsPlugin() {
        return guildWarsPlugin;
    }
    
    /**
     * Get the guild integration handler.
     * 
     * @return The guild integration handler
     */
    public GuildIntegration getGuildIntegration() {
        return guildIntegration;
    }
}
