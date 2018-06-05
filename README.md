# Random Steam Game Bot #
A simple bot for discord to give a user a random game to play from their Steam profile. A user can also filter by games they've played or games they haven't yet played. Another feature is the capability of checking your most played or least played games.

See [Commands](#commands) for usages.

## Important ##
Since Steam updated what users can decide to show on their profile, I have no possible way to prevent every edge case in the bot. An ideal scenario would be a level of privacy indicated in the Steam API response, but this is currently not provided.

If you want to avoid any issues just ensure you have your privacy settings set to "Public".

## Current version ## 
Few additions I would like to make in due time -

- Look into adding tag support for games, following up on the newly added genres
- Refactor the way commands are currently handled as it's awful right now
- Assessing the speeds of the bot and whether it needs paid hosting/whether I want to
- Consider adding emojis for refreshing the random game and to delete posts, this will help combat spam by the bot

## Hosting ##

The bot is now being hosted on an Amazon EC2 instance. You can invite the bot to your server using this link:
https://discordapp.com/oauth2/authorize?client_id=348109452043485185&permissions=67324928&scope=bot

Alternatively, find it on:
https://bots.discord.pw/bots/348109452043485185


## Commands ##

```
!sbhelp

!rgame <Custom URL/17 digit ID>

!rgame <Custom URL/17 digit ID> <played/unplayed>

!mostplayed <Custom URL/17 digit ID>

!leastplayed <Custom URL/17 digit ID>
```
