# Random Steam Game Bot
A bot implemented using Discord4J to communicate with Discord, Selenium for web crawling and Jsoup for HTML parsing that gets all of the users Steam games and returns a random recommendation for them to play, along with their Steam name and total game amount, on the chat application Discord. Can be filtered by played or unplayed games and more information such as games played, percentage of games played and time spent in the game are shown with these filters.

Current version: Works but has issues. Issues mostly noticed with huge Steam libraries (5000+ games), this is due to the Steam page being unresponsive with so many games to load.

Looking to add:
- More efficient use of the WebDriver's used for web crawling (Potential multi-threading)
- General refactoring of code
- Speeding up the bots responses
- Preventing the spam of commands

To host the bot yourself:
- Create your bot https://discordapp.com/developers/applications/me/
- Use your client ID for your bot in BotMain
- Download the ChromeDriver https://sites.google.com/a/chromium.org/chromedriver/downloads
- Add the path to the ChromeDriver in BotMain's main method
- The latest list of DLC for filtering can be found here https://github.com/JackJohnsonsProjects/Steam-game-dlc-crawler/tree/master/src/output

## Commands:

!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]
