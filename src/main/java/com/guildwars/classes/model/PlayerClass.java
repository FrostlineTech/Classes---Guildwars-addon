package com.guildwars.classes.model;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all player classes in the GuildWars Classes addon.
 */
public abstract class PlayerClass {
    
    private final String id;
    private final String name;
    private final String description;
    
    /**
     * Creates a new player class.
     *
     * @param id The unique identifier for this class
     * @param name The display name of the class
     * @param description The description of the class
     */
    public PlayerClass(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    /**
     * Gets the unique identifier for this class.
     *
     * @return The class ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the display name of the class.
     *
     * @return The class name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description of the class.
     *
     * @return The class description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Apply the class effects to a player.
     * This should be called when a player joins or when their class is set.
     *
     * @param player The player to apply effects to
     */
    public abstract void applyEffects(Player player);
    
    /**
     * Remove the class effects from a player.
     * This should be called when a player leaves or changes class.
     *
     * @param player The player to remove effects from
     */
    public abstract void removeEffects(Player player);
    
    /**
     * Perform the primary ability of this class.
     * This is typically bound to right-click with a specific item.
     *
     * @param player The player performing the ability
     * @return True if the ability was used successfully, false otherwise
     */
    public abstract boolean performPrimaryAbility(Player player);
    
    /**
     * Perform the secondary ability of this class.
     * This is typically bound to a specific command or action.
     *
     * @param player The player performing the ability
     * @return True if the ability was used successfully, false otherwise
     */
    public abstract boolean performSecondaryAbility(Player player);
    
    /**
     * Check if this class has a cooldown for the specified player.
     *
     * @param player The player to check
     * @param abilityName The name of the ability
     * @return True if the ability is on cooldown, false otherwise
     */
    public abstract boolean hasCooldown(Player player, String abilityName);
    
    /**
     * Get the remaining cooldown time for the specified player and ability.
     *
     * @param player The player to check
     * @param abilityName The name of the ability
     * @return The remaining cooldown time in seconds, or 0 if not on cooldown
     */
    public abstract int getRemainingCooldown(Player player, String abilityName);
    
    /**
     * Apply a potion effect to a player with the specified duration and amplifier.
     *
     * @param player The player to apply the effect to
     * @param effectType The type of potion effect
     * @param durationTicks The duration of the effect in ticks
     * @param amplifier The amplifier of the effect (0 = level 1)
     * @param ambient Whether the effect is ambient
     * @param particles Whether to show particles
     */
    protected void applyPotionEffect(Player player, PotionEffectType effectType, int durationTicks, int amplifier, boolean ambient, boolean particles) {
        player.addPotionEffect(new PotionEffect(effectType, durationTicks, amplifier, ambient, particles));
    }
    
    /**
     * Remove a potion effect from a player.
     *
     * @param player The player to remove the effect from
     * @param effectType The type of potion effect
     */
    protected void removePotionEffect(Player player, PotionEffectType effectType) {
        player.removePotionEffect(effectType);
    }
    
    /**
     * Convert this class to a map for storage.
     *
     * @return A map representation of this class
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        return map;
    }
    
    /**
     * Get the class type name for storage and identification.
     *
     * @return The class type name
     */
    public abstract String getTypeName();
}
