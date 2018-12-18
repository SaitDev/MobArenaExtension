# Mob Arena Extension  [![Build status](https://ci.appveyor.com/api/projects/status/npoql7iisagunifw/branch/master?svg=true)](https://ci.appveyor.com/project/SaitDev/mobarenaextension/branch/master)

Extend the supporting for 3rd pt plugins or additional features

## Getting Started
* Download a snapshot from [Build server](https://ci.appveyor.com/project/SaitDev/mobarenaextension/build/artifacts). Install to your plugins folder. Make sure you dont forget to install MobArena as well
* Enable which module you wanna in `plugins/MobArenaExtension/config.yml`


### Supported Plugins

`MythicMobs`
* Install MythicMobs. Then create cool mobs ([docs](https://mythicmobs.net/manual/) on their offical website)
* Restart your server (see [Known issues](#Known-Issues))
* Use mythic mob's name in arena waves config

`PlaceholderAPI`

Added placeholder (still updating):
Key | description
------------|-------------
mobarena_prefix | global prefix 
mobarena_total_enabled | amount of arenas is enabled
mobarena_arena_name | name of arena that player is in
mobarena_arena_prefix | prefix of arena that player is in
mobarena_arena_wave | current wave of arena
mobarena_arena_final_wave | 
mobarena_arena_mobs | amount of live mobs
mobarena_arena_killed | mobs that that player killed of this run
mobarena_arena_damage_dealt | damages that player dealt of this run
mobarena_arena_damage_received | damages that player received of this run

`DiscordSRV`



### Known Issues
* MythicMobs allow using some non-living entity (armor stand) but MobArena only allow living entity. Which mean you can not use non-living entity mythic mob in MobArena `yet`
* Adding new mob, rename or remove mob in MythicMobs wont get sync to MobArena, you should restart server
* Using similar mythic mob name is not compatible, like `Hero brine`, `hero-brine`, `Hero brines`
* This is a bug from MythicMob itself, when mob A spawn a minion B --> B has parent that is A. This is intended behaviour, but when using skill `Summon` without radius or with radius = 0, A wont be B's parent. Always use summon radius higher than 0 if you plan to use that in MobArena

3 first issues will need to wait for the next MobArena [major patch](https://github.com/garbagemule/MobArena/projects/5)


## License
The license does not apply to files inside folder /libs

[MIT](/LICENSE)