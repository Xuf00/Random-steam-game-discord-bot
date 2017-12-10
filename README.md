# Random Steam Game Bot #
A bot for discord to can give a user a random game to play from their Steam profile. See [Commands](#commands)

## Current version ## 
Works but has issues. Issues mostly noticed with huge Steam libraries (5000+ games), this is due to the Steam page being unresponsive with so many games to load and I currently don't see a workaround.

Looking to add:
- More efficient use of the WebDriver's used for web crawling (Potential multi-threading)
- General refactoring of code
- Speeding up the bots responses
- Preventing the spam of commands
- Fixing errors that occasionally appear
- Create a jar for the app

## Hosting ##

I am not hosting the bot publically at this point in time but may decide to in the future. The type of bot it is makes it better to host for just a few friends whenever we're on Discord. So with that said...

### To host the bot yourself ###
- Download the project or fork it
- Create your bot https://discordapp.com/developers/applications/me/
- Use your client ID for your bot in the programs arguments
- The latest list of DLC for filtering can be found here, right-click and save-as  "ListOfDLC" https://raw.githubusercontent.com/JackJohnsonsProjects/Steam-game-dlc-crawler/master/src/output/ListOfDLC.txt

## Commands ##

```
!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]

!mostplayed [Custom URL/17 digit ID]
```
