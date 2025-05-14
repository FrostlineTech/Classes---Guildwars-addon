package com.guildwars.classes.commands;

import com.guildwars.classes.GuildWarsClasses;
import com.guildwars.classes.model.PlayerClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Command handler for the /class command.
 */
public class ClassCommand implements CommandExecutor {

    private final GuildWarsClasses plugin;
    
    /**
     * Creates a new class command handler.
     *
     * @param plugin The GuildWarsClasses plugin instance
     */
    public ClassCommand(GuildWarsClasses plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            // Show player's current class
            showPlayerClass(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        
        switch (subCommand) {
            case "list":
                handleList(player);
                break;
            case "select":
            case "choose":
                handleSelect(player, subArgs);
                break;
            case "info":
                handleInfo(player, subArgs);
                break;
            case "remove":
            case "reset":
                handleRemove(player);
                break;
            case "ability1":
            case "primary":
                handlePrimaryAbility(player);
                break;
            case "ability2":
            case "secondary":
                handleSecondaryAbility(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command. Use /class for help.");
                break;
        }
        
        return true;
    }
    
    /**
     * Show a player's current class.
     *
     * @param player The player
     */
    private void showPlayerClass(Player player) {
        PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
        
        player.sendMessage(ChatColor.GOLD + "=== Your Class ===");
        
        if (playerClass == null) {
            player.sendMessage(ChatColor.YELLOW + "You don't have a class selected.");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class select <class>" + ChatColor.YELLOW + " to choose a class.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Current Class: " + ChatColor.GREEN + playerClass.getName());
            player.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.WHITE + playerClass.getDescription());
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class ability1" + ChatColor.YELLOW + " to use your primary ability.");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class ability2" + ChatColor.YELLOW + " to use your secondary ability.");
        }
    }
    
    /**
     * Handle the list command.
     *
     * @param player The player
     */
    private void handleList(Player player) {
        List<PlayerClass> availableClasses = plugin.getClassManager().getAvailableClasses();
        
        player.sendMessage(ChatColor.GOLD + "=== Available Classes ===");
        
        for (PlayerClass playerClass : availableClasses) {
            player.sendMessage(ChatColor.GREEN + playerClass.getName() + ChatColor.YELLOW + " - " + ChatColor.WHITE + playerClass.getDescription());
        }
        
        player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class select <class>" + ChatColor.YELLOW + " to choose a class.");
    }
    
    /**
     * Handle the select command.
     *
     * @param player The player
     * @param args The command arguments
     */
    private void handleSelect(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify a class to select.");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
            return;
        }
        
        String classId = args[0].toLowerCase();
        
        if (plugin.getClassManager().setPlayerClass(player, classId)) {
            PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have selected the " + playerClass.getName() + " class!");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class" + ChatColor.YELLOW + " to see your class information.");
        } else {
            player.sendMessage(ChatColor.RED + "Invalid class: " + classId);
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
        }
    }
    
    /**
     * Handle the info command.
     *
     * @param player The player
     * @param args The command arguments
     */
    private void handleInfo(Player player, String[] args) {
        if (args.length == 0) {
            // Show info for player's current class
            PlayerClass playerClass = plugin.getClassManager().getPlayerClass(player.getUniqueId());
            
            if (playerClass == null) {
                player.sendMessage(ChatColor.RED + "You don't have a class selected.");
                player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
                return;
            }
            
            showClassInfo(player, playerClass);
        } else {
            // Show info for specified class
            String classId = args[0].toLowerCase();
            PlayerClass playerClass = plugin.getClassManager().getClassById(classId);
            
            if (playerClass == null) {
                player.sendMessage(ChatColor.RED + "Invalid class: " + classId);
                player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
                return;
            }
            
            showClassInfo(player, playerClass);
        }
    }
    
    /**
     * Show information about a class.
     *
     * @param player The player
     * @param playerClass The class to show information for
     */
    private void showClassInfo(Player player, PlayerClass playerClass) {
        player.sendMessage(ChatColor.GOLD + "=== " + playerClass.getName() + " Class ===");
        player.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.WHITE + playerClass.getDescription());
        
        // Show class-specific information
        if (playerClass.getTypeName().equals("orc")) {
            player.sendMessage(ChatColor.YELLOW + "Primary Ability: " + ChatColor.WHITE + "Orc Rage - Gain increased strength and resistance for a short time.");
            player.sendMessage(ChatColor.YELLOW + "Secondary Ability: " + ChatColor.WHITE + "Ground Slam - Knock back nearby enemies and apply weakness.");
            player.sendMessage(ChatColor.YELLOW + "Passive Effects: " + ChatColor.WHITE + "Increased strength, but slower movement speed.");
        } else if (playerClass.getTypeName().equals("healer")) {
            player.sendMessage(ChatColor.YELLOW + "Primary Ability: " + ChatColor.WHITE + "Healing Beam - Shoot a beam that heals allies in its path.");
            player.sendMessage(ChatColor.YELLOW + "Secondary Ability: " + ChatColor.WHITE + "Healing Aura - Heal all nearby allies and grant them absorption.");
            player.sendMessage(ChatColor.YELLOW + "Passive Effects: " + ChatColor.WHITE + "Increased health and slow regeneration.");
        }
    }
    
    /**
     * Handle the remove command.
     *
     * @param player The player
     */
    private void handleRemove(Player player) {
        if (plugin.getClassManager().removePlayerClass(player)) {
            player.sendMessage(ChatColor.GREEN + "Your class has been reset.");
            player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + "/class list" + ChatColor.YELLOW + " to see available classes.");
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a class to remove.");
        }
    }
    
    /**
     * Handle the primary ability command.
     *
     * @param player The player
     */
    private void handlePrimaryAbility(Player player) {
        if (plugin.getClassManager().performPrimaryAbility(player)) {
            // Success message is handled by the ability itself
        } else {
            // Failure message is handled by the ability or class manager
        }
    }
    
    /**
     * Handle the secondary ability command.
     *
     * @param player The player
     */
    private void handleSecondaryAbility(Player player) {
        if (plugin.getClassManager().performSecondaryAbility(player)) {
            // Success message is handled by the ability itself
        } else {
            // Failure message is handled by the ability or class manager
        }
    }
}
