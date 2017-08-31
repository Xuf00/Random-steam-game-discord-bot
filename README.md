# Random Steam Game Bot
A bot implemented using Discord4J to communicate with Discord, Selenium for web crawling and Jsoup for HTML parsing that gets all of the users Steam games and returns a random recommendation for them to play, along with their Steam name and total game amount, on the chat application Discord. Can be filtered by played or unplayed games and more information such as games played, percentage of games played and time spent in the game are shown with these filters.

Current version: Works but has issues. Issues mostly noticed with huge Steam libraries, this is due to the Steam page being unresponsive (5000+ games).

Looking to add:
- Filtering out DLC (Awkward as the Steam game page doesn't differ)
- More efficient use of the WebDriver's used for web crawling
- Speeding up the bots responses
- Create a game class to hold the game data

## Commands:

!rgame [Custom URL/17 digit ID]

!rgame [Custom URL/17 digit ID] [played/unplayed]
