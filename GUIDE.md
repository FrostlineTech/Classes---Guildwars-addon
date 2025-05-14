# GuildWars Classes Addon - User Guide

## Introduction

Welcome to the GuildWars Classes Addon! This guide will help you understand how to use the class system and integrate it with your existing GuildWars plugin.

## Getting Started

### Installation

1. Make sure you have the GuildWars plugin installed on your server
2. Place the GuildWarsClasses.jar file in your server's plugins folder
3. Start or restart your server
4. The plugin will generate default configuration files

### First Steps

1. Choose a class using `/class select <classname>`
2. View your class information with `/class`
3. Explore available abilities with `/class info`

## Available Classes

### Orc Class

The Orc class is a powerful warrior with enhanced strength but reduced movement speed.

**Passive Effects:**
- Increased strength (Strength I effect)
- Slower movement speed (Slowness I effect)

**Primary Ability: Orc Rage**
- Activated by right-clicking with any axe
- Grants increased strength (Strength II) and resistance for 15 seconds
- 60-second cooldown
- Command: `/class ability1`

**Secondary Ability: Ground Slam**
- Knocks back nearby enemies and applies weakness
- 180-second cooldown
- Command: `/class ability2`

**Guild Bonuses:**
- Guild members receive additional strength when using abilities
- Enhanced ground slam effect

### Healer Class

The Healer class is a supportive class with extra health and healing abilities.

**Passive Effects:**
- Increased health (Health Boost II effect)
- Slow regeneration (Regeneration I effect)

**Primary Ability: Healing Beam**
- Activated by right-clicking with a stick
- Shoots a beam that heals allies in its path
- Restores 12 health points (6 hearts) and applies Regeneration II for 10 seconds
- 30-second cooldown
- Command: `/class ability1`

**Secondary Ability: Healing Aura**
- Heals all nearby allies and grants them absorption
- Restores 8 health points (4 hearts), applies Regeneration II for 15 seconds, and Absorption II for 30 seconds
- 120-second cooldown
- Command: `/class ability2`

**Guild Bonuses:**
- Guild members receive additional healing from abilities
- Enhanced healing aura effect

## Commands

| Command | Description |
|---------|-------------|
| `/class` | Shows your current class information |
| `/class list` | Lists all available classes |
| `/class select <class>` | Selects a class |
| `/class info [class]` | Shows detailed information about a class |
| `/class remove` | Removes your current class |
| `/class ability1` | Uses your primary ability |
| `/class ability2` | Uses your secondary ability |

## Guild Integration

The GuildWars Classes addon integrates seamlessly with the GuildWars plugin, providing additional benefits for guild members:

1. **Enhanced Abilities:** Guild members receive bonuses to their class abilities
2. **Guild-Specific Bonuses:** Each class receives different bonuses based on guild membership
3. **Team Synergy:** Guild members can coordinate their classes for maximum effectiveness

## Tips & Tricks

1. **Class Selection:** Choose a class that complements your playstyle and guild role
2. **Ability Timing:** Use your abilities strategically, as they have cooldowns
3. **Guild Coordination:** Coordinate with guild members to have a balanced team of classes
4. **Orc Combat:** As an Orc, use your Ground Slam to create space, then engage with Orc Rage
5. **Healer Support:** As a Healer, stay behind your teammates and use your Healing Beam to support from a distance

## Configuration

Server administrators can customize the plugin's behavior in the `config.yml` file:

- Adjust ability cooldowns
- Modify effect strengths and durations
- Enable/disable guild integration features
- Customize class-specific settings

## Troubleshooting

**Issue:** Class abilities don't work
**Solution:** Make sure you're using the correct item (axe for Orc, stick for Healer)

**Issue:** Guild bonuses aren't applying
**Solution:** Verify that guild integration is enabled in the config.yml

**Issue:** Cooldowns seem incorrect
**Solution:** Check the config.yml for custom cooldown settings

## Extending the Plugin

Developers can extend the plugin by:

1. Creating new classes that extend the `PlayerClass` abstract class
2. Implementing required methods for abilities and effects
3. Registering the new class in the `ClassManager`

## Support

If you encounter any issues or have questions about the GuildWars Classes addon, please:

1. Check the configuration file for potential solutions
2. Verify that you're using the latest version of both GuildWars and the Classes addon
3. Contact the plugin developer for support

Enjoy your enhanced GuildWars experience with the Classes addon!
