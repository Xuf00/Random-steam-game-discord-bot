# Random Steam Game Bot
A bot implemented using Discord4J to communicate with Discord, Selenium for web crawling and Jsoup for HTML parsing that gets all of the users Steam games and returns a random recommendation for them to play, along with their Steam name and total game amount, on the chat application Discord. Can be filtered by played or unplayed games and more information such as games played, percentage of games played and time spent in the game are shown with these filters.

Another feature is being able to view your top 5 most played games on Steam. This is nicely formatted using an EmbedBuilder.

## Current version 
Works but has issues. Issues mostly noticed with huge Steam libraries (5000+ games), this is due to the Steam page being unresponsive with so many games to load and I currently don't see a workaround.

Looking to add:
- More efficient use of the WebDriver's used for web crawling (Potential multi-threading)
- General refactoring of code
- Speeding up the bots responses
- Preventing the spam of commands
- Fixing errors that occasionally appear

## Hosting

I am not hosting the bot publically at this point in time but may decide to in the future. The type of bot it is makes it better to host for just a few friends whenever we're on Discord. So with that said...

### To host the bot yourself
- Download the project, fork it or check the releases page
- Create your bot https://discordapp.com/developers/applications/me/
- Use your client ID for your bot in the config/botconfig.txt file
- The latest list of DLC for filtering can be found here, right-click and save-as https://raw.githubusercontent.com/JackJohnsonsProjects/Steam-game-dlc-crawler/master/src/output/ListOfDLC.txt

## Commands:

!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]

!mostplayed [Custom URL/17 digit ID]
