# Random Steam Game Bot #
A bot for discord to can give a user a random game to play from their Steam profile. See [Commands](#commands) for more usages.

## Current version ## 
Works well but has issues with a few commands, such as the mostplayed command. Currently looking for a solution.

Looking to add:
- General refactoring of code
- Speeding up the bots responses
- Preventing the spam of commands
- Fixing errors that occasionally appear

## Hosting ##

I am not hosting the bot publicly at this point in time but may decide to in the future. The type of bot it is makes it better to host for just a few friends whenever we're on Discord. So with that being said...

### To host the bot yourself ###
- Download the project or fork it
- Create your bot https://discordapp.com/developers/applications/me/
- Apply for a Steam API Key https://steamcommunity.com/dev/apikey
- Put the Steam API Key in the SteamCrawler.java steamAPIToken variable.

## Commands ##

```
!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]

!mostplayed [Custom URL/17 digit ID]
```
