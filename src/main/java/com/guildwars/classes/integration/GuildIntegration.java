package com.guildwars.classes.integration;

import com.guildwars.GuildWars;
import com.guildwars.classes.GuildWarsClasses;
import com.guildwars.classes.model.PlayerClass;
import com.guildwars.model.Guild;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



/**
 * Handles integration between the GuildWars Classes addon and the GuildWars plugin.
 * Provides guild-specific bonuses for class abilities and effects.
 */
public class GuildIntegration {

    private final GuildWarsClasses plugin;
    private final GuildWars guildWarsPlugin;
    private final boolean enabled;
    private final boolean guildBonusesEnabled;
    
    /**
     * Creates a new guild integration handler.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public GuildIntegration(GuildWarsClasses plugin) {
        this.plugin = plugin;
        this.guildWarsPlugin = plugin.getGuildWarsPlugin();
        this.enabled = plugin.getConfig().getBoolean("guild-integration.enabled", true);
        this.guildBonusesEnabled = plugin.getConfig().getBoolean("guild-integration.guild-bonuses.enabled", true);
    }
    
    /**
     * Check if a player is in a guild.
     *
     * @param player The player to check
     * @return True if the player is in a guild, false otherwise
     */
    public boolean isPlayerInGuild(Player player) {
        if (!enabled || guildWarsPlugin == null) {
            return false;
        }
        
        try {
            Guild guild = guildWarsPlugin.getGuildService().getGuildByPlayer(player.getUniqueId());
            return guild != null;
        } catch (Exception e) {
            plugin.getLogger().warning("Error checking if player is in guild: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a player's guild.
     *
     * @param player The player
     * @return The player's guild, or null if they are not in a guild
     */
    public Guild getPlayerGuild(Player player) {
        if (!enabled || guildWarsPlugin == null) {
            return null;
        }
        
        try {
            return guildWarsPlugin.getGuildService().getGuildByPlayer(player.getUniqueId());
        } catch (Exception e) {
            plugin.getLogger().warning("Error getting player guild: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if two players are in the same guild.
     *
     * @param player1 The first player
     * @param player2 The second player
     * @return True if both players are in the same guild, false otherwise
     */
    public boolean arePlayersInSameGuild(Player player1, Player player2) {
        if (!enabled || guildWarsPlugin == null) {
            return false;
        }
        
        try {
            Guild guild1 = guildWarsPlugin.getGuildService().getGuildByPlayer(player1.getUniqueId());
            Guild guild2 = guildWarsPlugin.getGuildService().getGuildByPlayer(player2.getUniqueId());
            
            if (guild1 == null || guild2 == null) {
                return false;
            }
            
            return guild1.getId().equals(guild2.getId());
        } catch (Exception e) {
            plugin.getLogger().warning("Error checking if players are in same guild: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Apply guild bonuses to a player based on their class.
     *
     * @param player The player
     * @param playerClass The player's class
     */
    public void applyGuildBonuses(Player player, PlayerClass playerClass) {
        if (!enabled || !guildBonusesEnabled || guildWarsPlugin == null) {
            return;
        }
        
        if (!isPlayerInGuild(player)) {
            return;
        }
        
        String classType = playerClass.getTypeName();
        
        switch (classType) {
            case "orc":
                applyOrcGuildBonuses(player);
                break;
            case "healer":
                applyHealerGuildBonuses(player);
                break;
            default:
                // No bonuses for unknown class types
                break;
        }
    }
    
    /**
     * Apply guild bonuses for the Orc class.
     *
     * @param player The player
     */
    private void applyOrcGuildBonuses(Player player) {
        int strengthBonus = plugin.getConfig().getInt("guild-integration.guild-bonuses.orc.strength-bonus", 1);
        
        if (strengthBonus > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthBonus, true, false));
            player.sendMessage("§6[Guild Bonus] §aYour guild membership grants you additional strength!");
        }
    }
    
    /**
     * Apply guild bonuses for the Healer class.
     *
     * @param player The player
     */
    private void applyHealerGuildBonuses(Player player) {
        int healthBonus = plugin.getConfig().getInt("guild-integration.guild-bonuses.healer.health-bonus", 1);
        
        if (healthBonus > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, healthBonus, true, false));
            player.setHealth(Math.min(player.getHealth() + 4, player.getAttribute(Attribute.MAX_HEALTH).getValue()));
            player.sendMessage("§6[Guild Bonus] §aYour guild membership grants you additional health!");
        }
    }
    
    /**
     * Get the healing bonus for a healer based on their guild membership.
     *
     * @param player The player
     * @return The healing bonus amount
     */
    public int getHealerGuildHealingBonus(Player player) {
        if (!enabled || !guildBonusesEnabled || guildWarsPlugin == null) {
            return 0;
        }
        
        if (!isPlayerInGuild(player)) {
            return 0;
        }
        
        return plugin.getConfig().getInt("guild-integration.guild-bonuses.healer.healing-bonus", 2);
    }
    
    /**
     * Get the strength bonus for an orc based on their guild membership.
     *
     * @param player The player
     * @return The strength bonus level
     */
    public int getOrcGuildStrengthBonus(Player player) {
        if (!enabled || !guildBonusesEnabled || guildWarsPlugin == null) {
            return 0;
        }
        
        if (!isPlayerInGuild(player)) {
            return 0;
        }
        
        return plugin.getConfig().getInt("guild-integration.guild-bonuses.orc.strength-bonus", 1);
    }
    
    /**
     * Check if guild integration is enabled.
     *
     * @return True if guild integration is enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled && guildWarsPlugin != null;
    }
    
    /**
     * Check if guild bonuses are enabled.
     *
     * @return True if guild bonuses are enabled, false otherwise
     */
    public boolean areGuildBonusesEnabled() {
        return enabled && guildBonusesEnabled && guildWarsPlugin != null;
    }
}
