# Mob Arena Extension  [![Build status](https://github.com/SaitDev/MobArenaExtension/actions/workflows/snapshot.yml/badge.svg)](https://github.com/SaitDev/MobArenaExtension/actions/workflows/maven.yml)

Extend the supporting for 3rd pt plugins or additional features

## Getting Started
* Install Spigot/Paper 1.18.2, Java 17 and MobArena 0.107 or higher
* Download a release from [SpigotMC](https://www.spigotmc.org/resources/mobarenaextension.106125/) or snapshot [Github Action](https://github.com/SaitDev/MobArenaExtension/actions). Install to your server
* Enable which module you wanna in `plugins/MobArenaExtension/config.yml`


### Supported Plugins

`MythicMobs`
* Install MythicMobs, requires version 5 onward. Then create cool mobs ([docs](https://mythicmobs.net/manual/) on their offical website)
* Reload extension `/mobarenaextension reload` (restart server if using version before 0.3)
* Use mythic mob's name in arena waves config

(Check [Known issues](#Known-Issues))

**~~PlaceholderAPI~~**  No longer available in 0.3, but good news, placeholder is now officially supported [MobArenaPlaceholders](https://github.com/mobarena/MobArenaPlaceholders)

0.2 placeholders :

Key | description
------------|-------------
mobarena_prefix | global prefix 
mobarena_total_enabled | amount of arenas is enabled
mobarena_arena_name | name of arena that player is in
mobarena_arena_prefix | prefix of arena that player is in
mobarena_arena_wave | current wave of arena
mobarena_arena_final_wave | final wave of arena
mobarena_arena_mobs | amount of live mobs
mobarena_arena_killed | mobs that that player killed in this run
mobarena_arena_damage_dealt | damages that player dealt in this run
mobarena_arena_damage_received | damages that player received in this run
mobarena_arena_player_join | players joining this arena
mobarena_arena_player_live | players fighting in this arena

`DiscordSRV`

In isolated chat arena, messages wont be sent to discord.

### Known Issues
* MythicMobs allow using some non-living entity (armor stand) but MobArena only allow living entity. Which mean you can not use non-living entity mythic mob in MobArena `yet`
* Using similar mythic mob name is not compatible, like `Hero brine`, `hero-brine`, `Hero brines`
* This is a bug from MythicMob itself, when mob A spawn a minion B --> B has parent that is A. This is intended behaviour, but when using skill `Summon` without radius or with radius = 0, A wont be B's parent. Always use summon radius higher than 0 if you plan to use that in MobArena

For first issue, will need to wait for the next MobArena [major patch](https://github.com/garbagemule/MobArena/projects/5)


## License

[MIT](/LICENSE)