# GuildWars Classes Addon

> *Enhance your GuildWars experience with unique player classes, each with special abilities and attributes.*

## Overview

GuildWars Classes is an addon for the GuildWars plugin that adds a class system to your server. Players can choose from different classes, each with unique passive abilities and active skills that can be triggered in combat or other situations.

## Features

### Class System
- **Multiple Classes**: Choose from different classes with unique abilities
- **Passive Effects**: Each class has permanent passive effects
- **Active Abilities**: Use special abilities with cooldowns
- **Class Selection**: Players can select and change their class

### Available Classes

#### Orc
- **Passive Effects**: Increased strength, slower movement speed
- **Primary Ability**: Orc Rage - Gain increased strength and resistance for a short time
- **Secondary Ability**: Ground Slam - Knock back nearby enemies and apply weakness
- **Trigger**: Right-click with any axe to activate Orc Rage

#### Healer
- **Passive Effects**: Increased health and slow regeneration
- **Primary Ability**: Healing Beam - Shoot a beam that heals allies in its path
- **Secondary Ability**: Healing Aura - Heal all nearby allies and grant them absorption
- **Trigger**: Right-click with a stick to activate Healing Beam

### GuildWars Integration
- Seamlessly integrates with the GuildWars plugin
- Guild-based class bonuses (configurable)
- Compatible with all GuildWars features

## Commands

- `/class` - Display your current class information
- `/class list` - List all available classes
- `/class select <class>` - Select a class
- `/class info [class]` - View detailed information about a class
- `/class remove` - Remove your current class
- `/class ability1` - Use your primary ability
- `/class ability2` - Use your secondary ability

## Installation

1. Make sure you have the GuildWars plugin installed
2. Place the GuildWarsClasses.jar file in your server's plugins folder
3. Start/restart your server
4. Configure the plugin in the config.yml file

## Configuration

The plugin is highly configurable. You can adjust:
- Class abilities and their effects
- Cooldown times
- Effect durations and strengths
- Guild integration features

See the `config.yml` file for all configuration options.

## Permissions

- `guildwarsclasses.use` - Allows use of the class system (default: true)
- `guildwarsclasses.admin` - Gives access to all GuildWarsClasses commands (default: op)

## Development

This addon is designed to be extensible. You can easily add new classes by:
1. Creating a new class that extends `PlayerClass`
2. Implementing the required methods
3. Registering the class in the `ClassManager`

## Requirements

- Java 21 or higher
- GuildWars plugin
- Paper/Spigot server (1.20.4+)

## License

This project is available for use and modification. Please credit the original authors when using or adapting this code.
