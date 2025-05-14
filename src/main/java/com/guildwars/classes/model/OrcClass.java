package com.guildwars.classes.model;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the Orc class in the GuildWars Classes addon.
 * Orcs have extra strength but slower movement speed.
 */
public class OrcClass extends PlayerClass {

    private static final Map<UUID, Long> primaryAbilityCooldowns = new HashMap<>();
    private static final Map<UUID, Long> secondaryAbilityCooldowns = new HashMap<>();
    
    private static final int PRIMARY_COOLDOWN_SECONDS = 60;
    private static final int SECONDARY_COOLDOWN_SECONDS = 180;
    
    /**
     * Creates a new Orc class.
     */
    public OrcClass() {
        super("orc", "Orc", "A powerful warrior with extra strength but slower movement speed.");
    }

    @Override
    public void applyEffects(Player player) {
        // Apply strength effect (level 1, infinite duration)
        applyPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false);
        
        // Apply slowness effect (level 0, infinite duration)
        applyPotionEffect(player, PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false);
        
        player.sendMessage(ChatColor.GREEN + "You feel the strength of an Orc flowing through your veins!");
    }

    @Override
    public void removeEffects(Player player) {
        // Remove strength effect
        removePotionEffect(player, PotionEffectType.INCREASE_DAMAGE);
        
        // Remove slowness effect
        removePotionEffect(player, PotionEffectType.SLOW);
    }

    @Override
    public boolean performPrimaryAbility(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (hasCooldown(player, "primary")) {
            int remainingSeconds = getRemainingCooldown(player, "primary");
            player.sendMessage(ChatColor.RED + "Orc Rage is on cooldown! " + remainingSeconds + " seconds remaining.");
            return false;
        }
        
        // Apply temporary strength boost (level 2, 15 seconds)
        applyPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 15 * 20, 1, true, true);
        
        // Apply temporary resistance (level 1, 15 seconds)
        applyPotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 15 * 20, 0, true, true);
        
        // Play sound effect
        player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1.0f, 0.8f);
        
        // Send message
        player.sendMessage(ChatColor.RED + "You unleash your Orc Rage, gaining increased strength and resistance!");
        
        // Set cooldown
        primaryAbilityCooldowns.put(playerId, System.currentTimeMillis() + (PRIMARY_COOLDOWN_SECONDS * 1000));
        
        return true;
    }

    @Override
    public boolean performSecondaryAbility(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (hasCooldown(player, "secondary")) {
            int remainingSeconds = getRemainingCooldown(player, "secondary");
            player.sendMessage(ChatColor.RED + "Ground Slam is on cooldown! " + remainingSeconds + " seconds remaining.");
            return false;
        }
        
        // Get nearby players within 5 blocks
        player.getNearbyEntities(5, 5, 5).forEach(entity -> {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                
                // Apply knockback effect
                target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5).setY(0.5));
                
                // Apply weakness effect (level 1, 10 seconds)
                applyPotionEffect(target, PotionEffectType.WEAKNESS, 10 * 20, 0, true, true);
                
                // Send message to target
                target.sendMessage(ChatColor.RED + "You were hit by " + player.getName() + "'s Ground Slam!");
            }
        });
        
        // Play sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
        
        // Send message
        player.sendMessage(ChatColor.RED + "You slam the ground with your mighty strength, knocking back nearby enemies!");
        
        // Set cooldown
        secondaryAbilityCooldowns.put(playerId, System.currentTimeMillis() + (SECONDARY_COOLDOWN_SECONDS * 1000));
        
        return true;
    }

    @Override
    public boolean hasCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();
        
        if ("primary".equalsIgnoreCase(abilityName)) {
            return primaryAbilityCooldowns.containsKey(playerId) && 
                   primaryAbilityCooldowns.get(playerId) > System.currentTimeMillis();
        } else if ("secondary".equalsIgnoreCase(abilityName)) {
            return secondaryAbilityCooldowns.containsKey(playerId) && 
                   secondaryAbilityCooldowns.get(playerId) > System.currentTimeMillis();
        }
        
        return false;
    }

    @Override
    public int getRemainingCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();
        
        if ("primary".equalsIgnoreCase(abilityName) && primaryAbilityCooldowns.containsKey(playerId)) {
            long remainingMillis = primaryAbilityCooldowns.get(playerId) - System.currentTimeMillis();
            return remainingMillis > 0 ? (int) (remainingMillis / 1000) : 0;
        } else if ("secondary".equalsIgnoreCase(abilityName) && secondaryAbilityCooldowns.containsKey(playerId)) {
            long remainingMillis = secondaryAbilityCooldowns.get(playerId) - System.currentTimeMillis();
            return remainingMillis > 0 ? (int) (remainingMillis / 1000) : 0;
        }
        
        return 0;
    }

    @Override
    public String getTypeName() {
        return "orc";
    }
    
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("type", getTypeName());
        return map;
    }
}
