# GuildWars Classes Addon - Patch Notes

## Version 1.2 (May 15, 2025)

### Compatibility Updates
- **Extended Version Support**: Added compatibility for Minecraft versions 1.17 through 1.21.5
- **API Updates**: Updated Paper and Spigot API dependencies to 1.21.5
- **Core Changes**: Modified plugin to work with the latest Minecraft API standards

### Technical Improvements
- Updated attribute references to match newer Minecraft versions
  - Changed `GENERIC_MAX_HEALTH` to `MAX_HEALTH` for better compatibility
- Updated potion effect type references
  - Changed `INCREASE_DAMAGE` to `STRENGTH`
  - Changed `SLOW` to `SLOWNESS`
  - Changed `DAMAGE_RESISTANCE` to `RESISTANCE`
- Fixed particle effect implementations to work with newer Minecraft versions
- Removed unused imports for cleaner code

### Notes for Server Admins
- This update is fully backwards compatible with existing player data
- No configuration changes are required when upgrading from previous versions
- Recommended for all servers running Minecraft 1.17 or newer

---

## Previous Versions

### Version 1.0.0 (Initial Release)
- Added class system with unique abilities
- Implemented Healer class with healing abilities
- Implemented Orc class with strength bonuses
- Added guild integration for class-specific bonuses
- Added class selection and management commands
