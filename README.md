# RestPR
 

RestPR is a RESTful web api that enables competitive communities to create player leaderboard seasons using tournament match data from services like Challonge.com

Players are ranked and sorted into a season leaderboard by their rating score (ELO, Glicko, TrueSkill, etc).

After creation the season official can authorize specific users as tournament officials.
Season cycle format can be specified upon creation along with a start date and end date pair.
Users can request for approval to become tournament officials.

These authorized users can then submit tournament brackets to populate rating data for the season.
Batch addition of tournaments (for pools) is supported.
Users can submit indvidiual matches for approval to play for ELO. Authorized users can approve these matches.

The match history for these games are then stored locally in a DB.

Players can be given an initial seed from a previous season,  by an authorized official or by a default value.

Users can view match data with different types of sorting and inspect indivisual players.
Player match data can also be filtered to show victories/losses and upsets.
Users can get a list of matches played between two players.

Users can see the top N players in the season or all the players in the season.

The season official can also create divisions within the leaderboard by score.
These divisions can be used by tournament officials to incentivise competition.
Additionally divisions within a leaderboard can be used to produce round robin side events or general seeding.

At the end of the season it can be marked as finished and a subjective power rankings can be submitted.
Users can see how many days left 

Panelists involved in the subjective power rankings can utilize the upset filtering to support their decisions.

#Resources

- User

#Response Codes

- 200 Ok
- 400 Invalid Challonge Reference, could not validate.
- 401 User isn't authorized to submit tournament.
- 404 Resource does not exist.
- 409 Duplicate tournament submission.
- 500 Internal Error.

#Swagger
See [RestPRApi.yaml](RestPRApi.yaml) for Swagger specification.

#Configure
Define a database connection in configuration.yaml using the database: key.

You'll need to use ojdbc6_g.jar from Oracle in your /bin folder.

#Build
Run the build gradle task:
	 gradle build

#Run
To run the application you must execute this command:
	 java -classpath bin/ojdbc6_g.jar:build/libs/RESTPR-all.jar edu.oregonstate.mist.restpr.RESTPRApplication server configuration.yaml

or just run manualjavacall.sh:
	 ./manualjavacall.sh

#Connection Examples
The following examples can be excuted with netcat using heredocs like this:
	nc localhost 8008 << HERE
	...
	...
	HERE

#GET
Get Users and partial match user_login and display_name query parameters

	GET /User?user_login=LOGIN&display_name=DISPLAY HTTP/1.0

	HTTP/1.1 200 OK
	Date: Sat, 08 Aug 2015 18:56:14 GMT
	Content-Type: application/json
	Content-Length: 241

	[{"user_id":16,"user_login":"16 SER LOGIN","display_name":"16 DISPLAY NAME"},{"user_id":1,"user_login":"NEAT USER LOGIN","display_name":"NEAT DISPLAY NAME"},{"user_id":14,"user_login":"UNIQUE SER LOGIN","display_name":"UNIQUE DISPLAY NAME"}]%

Get all Users

	GET /User/all HTTP/1.0

	HTTP/1.1 200 OK
	Date: Sat, 08 Aug 2015 18:58:30 GMT
	Content-Type: application/json
	Vary: Accept-Encoding
	Content-Length: 1346

	[{"user_id":15,"user_login":"testnew","display_name":"brandnew"},{"user_id":1,"user_login":"NEAT USER LOGIN","display_name":"NEAT DISPLAY NAME"},{"user_id":21,"user_login":"pokjhnb","display_name":"983"},{"user_id":22,"user_login":"wed","display_name":"ffg"},{"user_id":23,"user_login":"ff","display_name":"uh"},{"user_id":25,"user_login":"fb","display_name":"ygv"},{"user_id":27,"user_login":"fgbh","display_name":"jhg"},{"user_id":29,"user_login":"fbbn","display_name":"uhb"},{"user_id":37,"user_login":"ffff","display_name":"qwer"},{"user_id":14,"user_login":"UNIQUE SER LOGIN","display_name":"UNIQUE DISPLAY NAME"},{"user_id":16,"user_login":"16 SER LOGIN","display_name":"16 DISPLAY NAME"},{"user_id":18,"user_login":"tesfffftnew","display_name":"ddfg"},{"user_id":19,"user_login":"rthj","display_name":"wdf"},{"user_id":20,"user_login":"tfgh","display_name":"lkjhg"},{"user_id":24,"user_login":"67890","display_name":"wertyu"},{"user_id":31,"user_login":"ytgv","display_name":"tf"},{"user_id":32,"user_login":"ikj","display_name":"yhj"},{"user_id":34,"user_login":"wef","display_name":"wwrwhwrh"},{"user_id":36,"user_login":"aef","display_name":"qqwwd"},{"user_id":40,"user_login":"qqazzz","display_name":"eeeee"},{"user_id":41,"user_login":"qqazsszz","display_name":"eesseee"},{"user_id":42,"user_login":"qqaz33sszz","display_name":"333"}]

Get User by ID

	GET /User/16 HTTP/1.0

	HTTP/1.1 200 OK
	Date: Sat, 08 Aug 2015 19:02:37 GMT
	Content-Type: application/json
	Content-Length: 75

	{"user_id":16,"user_login":"16 SER LOGIN","display_name":"16 DISPLAY NAME"}

#POST
Post user

	POST /User/ HTTP/1.0
	Content-Type: application/json
	Content-Length:56

	{
	  "display_name": "TEST#5",
	  "user_login": "TEST#6"
	}

#PUT
Update user by id

	PUT /User/52 HTTP/1.0
	Content-Type: application/json
	Content-Length:56

	{
	  "display_name": "TEST44",
	  "user_login": "TESTAA"
	}

#DELETE

Delete user by id
	DELETE /User/32 HTTP/1.0

	HTTP/1.1 200 OK
	Date: Sat, 08 Aug 2015 19:04:59 GMT
	Content-Length: 0
