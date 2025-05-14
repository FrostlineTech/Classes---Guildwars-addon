package com.guildwars.classes.model;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the Healer class in the GuildWars Classes addon.
 * Healers have extra health and can shoot healing with right click.
 */
public class HealerClass extends PlayerClass {

    private static final Map<UUID, Long> primaryAbilityCooldowns = new HashMap<>();
    private static final Map<UUID, Long> secondaryAbilityCooldowns = new HashMap<>();
    
    private static final int PRIMARY_COOLDOWN_SECONDS = 30;
    private static final int SECONDARY_COOLDOWN_SECONDS = 120;
    
    /**
     * Creates a new Healer class.
     */
    public HealerClass() {
        super("healer", "Healer", "A supportive class with extra health and healing abilities.");
    }

    @Override
    public void applyEffects(Player player) {
        // Apply health boost effect (level 1, infinite duration)
        applyPotionEffect(player, PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 1, true, false);
        
        // Apply regeneration effect (level 0, infinite duration)
        applyPotionEffect(player, PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false);
        
        // Heal the player to their new maximum health
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        
        player.sendMessage(ChatColor.GREEN + "You feel the healing energy flowing through your body!");
    }

    @Override
    public void removeEffects(Player player) {
        // Remove health boost effect
        removePotionEffect(player, PotionEffectType.HEALTH_BOOST);
        
        // Remove regeneration effect
        removePotionEffect(player, PotionEffectType.REGENERATION);
    }

    @Override
    public boolean performPrimaryAbility(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        if (hasCooldown(player, "primary")) {
            int remainingSeconds = getRemainingCooldown(player, "primary");
            player.sendMessage(ChatColor.RED + "Healing Beam is on cooldown! " + remainingSeconds + " seconds remaining.");
            return false;
        }
        
        // Get the direction the player is looking
        Vector direction = player.getLocation().getDirection();
        Location startLocation = player.getEyeLocation();
        
        // Create a healing beam effect
        for (int i = 0; i < 40; i++) {
            Location particleLocation = startLocation.clone().add(direction.clone().multiply(0.5 * i));
            
            // Display healing particles
            player.getWorld().spawnParticle(
                Particle.REDSTONE, 
                particleLocation, 
                5, 
                0.1, 0.1, 0.1, 
                0, 
                new Particle.DustOptions(Color.GREEN, 1)
            );
            
            // Check for players to heal
            for (Entity entity : player.getWorld().getNearbyEntities(particleLocation, 1, 1, 1)) {
                if (entity instanceof Player) {
                    Player target = (Player) entity;
                    
                    // Don't heal if already at max health
                    if (target.getHealth() >= target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                        continue;
                    }
                    
                    // Heal the target for 6 hearts (12 health points)
                    double newHealth = Math.min(target.getHealth() + 12, target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    target.setHealth(newHealth);
                    
                    // Apply regeneration effect (level 1, 10 seconds)
                    applyPotionEffect(target, PotionEffectType.REGENERATION, 10 * 20, 1, true, true);
                    
                    // Play healing sound
                    target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
                    
                    // Send message to target
                    target.sendMessage(ChatColor.GREEN + "You were healed by " + player.getName() + "!");
                }
            }
        }
        
        // Play sound effect
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.5f);
        
        // Send message
        player.sendMessage(ChatColor.GREEN + "You cast a healing beam, restoring health to allies in its path!");
        
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
            player.sendMessage(ChatColor.RED + "Healing Aura is on cooldown! " + remainingSeconds + " seconds remaining.");
            return false;
        }
        
        // Get nearby players within 10 blocks
        player.getNearbyEntities(10, 10, 10).forEach(entity -> {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                
                // Heal the target for 4 hearts (8 health points)
                double newHealth = Math.min(target.getHealth() + 8, target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                target.setHealth(newHealth);
                
                // Apply regeneration effect (level 1, 15 seconds)
                applyPotionEffect(target, PotionEffectType.REGENERATION, 15 * 20, 1, true, true);
                
                // Apply absorption effect (level 1, 30 seconds)
                applyPotionEffect(target, PotionEffectType.ABSORPTION, 30 * 20, 1, true, true);
                
                // Send message to target
                target.sendMessage(ChatColor.GREEN + "You were healed by " + player.getName() + "'s Healing Aura!");
                
                // Play healing sound
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
            }
        });
        
        // Create particle effect
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double x = Math.cos(angle) * 10;
            double z = Math.sin(angle) * 10;
            
            Location particleLocation = player.getLocation().clone().add(x, 0.5, z);
            
            player.getWorld().spawnParticle(
                Particle.REDSTONE, 
                particleLocation, 
                5, 
                0.1, 0.1, 0.1, 
                0, 
                new Particle.DustOptions(Color.GREEN, 1)
            );
        }
        
        // Play sound effect
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.2f);
        
        // Send message
        player.sendMessage(ChatColor.GREEN + "You create a healing aura, restoring health to all nearby allies!");
        
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
        return "healer";
    }
    
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("type", getTypeName());
        return map;
    }
}
