# RestPR

TEST LINE FOR MERGE 

RestPR is a RESTful web api that enables competitive communities to create player leaderboard seasons using tournament match data from services like Challonge.com

Players are ranked and sorted into a leaderboard by their rating score (ELO, Glicko, TrueSkill, etc).

After creation the season official can authorize specific users as tournament organizers as tournament officials.
These authorized users can then submit tournament brackets to populate rating data for the season.

The match history for these games are then stored locally in a DB.

Players can be given an initial seed from a previous season or seeded directly by an authorized official.

Users can visualize the match data with different types of sorting and inspect indivisual players.
Player match data can also be filtered to show victories/losses and upsets.

The season official can also create divisions within the leaderboard by score. These divisions can be used by tournament officials to incentivise competition. Additionally divisions within a leaderboard can be used for round robin side events or general seeding.

At the end of the season it can be marked as finished and a subjective power rankings can be submitted.
Panelists involved in the subjective power rankings can utilize the upset filtering to support their decisions.

#Resources

Seasons
Tournaments
Matches
Players

#Response Codes
200 Ok
400 Invalid Challonge Reference, could not validate.
401 User isn't authorized to submit tournament.
404 Resource does not exist.
409 Duplicate tournament submission.
500 Internal Error.
