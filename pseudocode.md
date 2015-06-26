Note: A lot of this pseudocode will have to change to fit the DAO access patterns.

##GET USER //get users

Accept display_name and user_login as parameters used for set constraints

```
if db connection is dead
return 500 error response
else
	//See how dropwizards’ dao binding handles null values

	select from users in the user table where like %display_name% and where like %user_login%
return query result as json collection of users
```

##POST USER // Authenticated action

POST USER // Authenticated action
Accept api_key in header as parameter
Accept new user object in the request body

```
if db connection is live
	if api_key parameter exists in user permissions table
if display_name parameter and user_login parameter are unique (both do not exist in table)
	create new user row in users table with parameters as values
	return 200 success response
else
	return 405 bad input response
	else
		return 401 invalid authentication response
else
	return 500 internal server error response
```

##GET USER/{ID}

```
if db connection is live
	if id exists in the users table
		return 200 success response with it in body
	else
		return 404 not found error
else
	return 500 internal server error response
```



##PUT USER/{ID}

PUT USER/{ID}

```
if db connection is live
	if api_key parameter exists in user permissions table
if display_name parameter and user_login parameter are unique (both do not exist in table)
	create new user row in users table with parameters as values
	return 200 success response
else
	return 405 bad input response
	else
		return 401 invalid authentication response
else
	return 500 internal server error response
```

##DELETE USER/{ID}

```
if db connection is live
	if api_key parameter exists in the user permissions table
		delete specified user from the table
		//Delete is idempotent so we don’t have to check if it exists in advance
		200 successful response
	else
		401 invalid authorization error
else
	500 internal server error
```

##POST TOURNAMENT

Takes in Season_ID, Challonge API Key, Challonge Subdomain string, Challonge url string

```
if db connection is alive
	if challonge url string and subdomain pair are unique to the db
make http request to challonge api for
	if request is successful
		if tournament is complete
			Query db for all player in the season
			take set difference of db_players and tournament_players by name
			make new players in db
				keep these names in a list
				//this gets returned later as a way to handle merging players
				//Instead of trying to match spelling variations the user will just be notified of new players that get added to the players table.
				//This way they can manually merge the players on their own

map tournament_player id’s to db_player id’s
			for every tournament_match
add new match rows with internal db playerid’s
		else
			405 invalid input
	else
		500 internal server error //challonge’s api is down
	else
		405 invalid input
else //db is down
	500 internal server error
```

##Player/Merge

Accepts a source_player_id and destination_player_id that correspond to the players table

```
if source_player_id exists and destination_player_id exists in the player table
	//Change the matches
	update matches
	set player_id = destination_player_id
	where player_id = source_player_id

delete source_player_id row from player table
```

##PLAYERS/ELO

Accepts a season, returns the elo of all players

Query DB for all player from the season
	initialize their base elo values
		if null
use the season seed value
Query for all matches from the season
Stick those matches into an arraylist
Stick the users into a hashmap
iterate through each match
	update the respective players

return a json representation of the player collection

Ideally this representation would be cached until a new set of tournament matches get posted or the base elo’s

In this case we use a hashmap because the player id’s we receive from the matches don’t correspond to array indexes. They’re keys to the player objects in memory. On top of this the players are going to be accessed out of order so it’d be nice to have a data structure that accommodates for that. We are going to be iterating through the matches so they can just be put into an array.
```
