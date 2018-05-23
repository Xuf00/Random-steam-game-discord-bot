# Random Steam Game Bot #
A simple bot for discord to give a user a random game to play from their Steam profile. A user can also filter by games they've played or games they haven't yet played. Another feature is the capability of checking your most played or least played games.

See [Commands](#commands) for usages.

## Important ##
Since Steam updated what users can decide to show on their profile, I have no possible way to prevent every edge case in the bot. An ideal scenario would be a level of privacy indicated in the Steam API response, but this is currently not provided.

If you want to avoid any issues just ensure you have your privacy settings set to "Public".

## Current version ## 
Works well, no more major tweaks unless Steam update their API to allow me to check a users privacy level.

## Hosting ##

The bot is now being hosted on an Amazon EC2 instance. You can invite the bot to your server using this link:
https://discordapp.com/oauth2/authorize?client_id=348109452043485185&permissions=67324928&scope=bot

Alternatively, find it on:
https://bots.discord.pw/bots/348109452043485185


## Commands ##

```
!sbhelp

!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]

!mostplayed [Custom URL/17 digit ID]

!leastplayed [Custom URL/17 digit ID]
```
