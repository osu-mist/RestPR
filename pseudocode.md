Note: A lot of this pseudocode will have to change to fit the DAO access patterns.

##GET USER

Get users and filters for partial name matches.
Accept display_name and user_login as parameters used for set constraints

```
if db connection is dead
	return 500 error response
else
	select from users in the user table where like %display_name% and where like %user_login%
	return query result as json collection of users with 200 succes response
```
See how dropwizards’ dao binding handles null values

##POST USER

Authenticated action

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
//TODO Figure out how to check a permissions join table
//Check how JDBI and the other dropwizard libraries handle screening for uniqueness before creation
//"Uniqueness in POJO field for DAO"

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
		200 successful response
	else
		401 invalid authorization error
else
	500 internal server error
```
Delete is idempotent so we don’t have to check if it exists in advance

##POST TOURNAMENT

Takes in Season_ID, Challonge API Key, Challonge Subdomain string, Challonge url string
This will fall outside of the simple POJO field population creation style
//Make new pojo, set fields, insert into table...
Theres no reason to have a tournament without matches at the moment so matches
will have to

Matches should have a time_started column that handles the order in which to update elo.


```
if db connection is alive
	if challonge url string and subdomain pair are unique to the db
make http request to challonge api for
	if request is successful
		if tournament is complete
			*Query db for all players in the season
			take set difference of db_players and tournament_players by name
			make new players in db
				keep these names in a list
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
With the DAO pattern in mind we can add a function to the players DAO that gets
plays by season.

Instead of trying to match spelling variations of player names the user will
just be notified of new players that get added to the players table.
This way they can manually merge the players on their own

##Player/Merge

Accepts a source_player_id and destination_player_id that correspond to the players table

```
if source_player_id exists and destination_player_id exists in the player table
	//Change the matches
	update matches (Change the old player id to new destination_player_id)
	set player_id = destination_player_id
	where player_id = source_player_id

delete source_player_id row from player table
```
//Lookup how to do this sort of query. This isn't just an update by id query.


##PLAYERS/ELO

Accepts a season, returns the elo of all players

```
Get the season ELO seed value
Query DB for all player from the season //Use the previously mentioned player DAO list by season function
	initialize their base elo values //The DAO should handle this
		if null
			assign the season ELO seed value
Query for all matches from the season //The DAO should do this as well
	Sort them by time_started field
Stick those matches into a list
Stick the users into a hashmap //Use groovy maps
iterate through each match
	update the respective players ELO value pairs
update the user rows that were mapped to the hashmap
	//Iterate through the map, update indivdually

return a json representation of the player collection
```

Ideally this representation would be cached until a new set of tournament matches get posted or the base elo’s
In this case we use a hashmap because the player id’s we receive from the matches don’t correspond to array indexes.
They’re keys to the player objects in memory.
On top of this the players are going to be accessed out of order so it’d be nice to have a data structure that accommodates for that.
We are going to be iterating through the matches so they can just be put into an array.

DAO Design Notes

Player and Matches should have a list by seasonID function.
//TODO Determine how to update multiple rows in one call

Determine how to make a DAO interface using JDBI (or research Hibernate to see if it handles this)
that can create a hashmap from a select statement using one of the fields as the key

Goal example:
I want to select all players in a particular season. Make a hashmap that associates their
PLAYER_ID primary key to an ELO value pair.

//TODO Figure out how to handle the time from Challonge's JSON, the usage of JODA Time
for sorting and storing in the DB.

Challonge API handles time in this format
Joda Time has a format builder for it.
http://www.joda.org/joda-time/key_format.html

Example from Challonge:
```
"started_at": "2015-03-21T22:04:04.365-04:00",
```
So far I think Challonge uses this format
"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
Heres some documentation about it.
http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html

Sorting the list of matches by a time_started field would be nice.
Joda Time has a comparator for this.
http://www.joda.org/joda-time/apidocs/org/joda/time/DateTimeComparator.html

//TODO Design ranking interface around the Groovy list.each() expressions
http://www.groovy-lang.org/groovy-dev-kit.html#Collections-Lists

http://www.groovy-lang.org/groovy-dev-kit.html#Collections-Maps

JDBI DAO NOTES

Our goal is to map the query ResultSet to our POJO

