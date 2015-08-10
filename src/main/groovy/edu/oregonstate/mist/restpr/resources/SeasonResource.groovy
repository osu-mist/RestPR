package edu.oregonstate.mist.restpr.resources

import com.google.common.base.Optional

import edu.oregonstate.mist.restpr.api.ErrorPOJO
import edu.oregonstate.mist.restpr.api.Season
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.db.SeasonDAO
import edu.oregonstate.mist.restpr.db.UserDAO

import javax.validation.Valid
import javax.validation.constraints.NotNull

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

/**
 * Created by georgecrary on 7/13/15.
 */

@Path("/season")
@Produces(MediaType.APPLICATION_JSON)
class SeasonResource {
  private final SeasonDAO seasonDAO

  @Context
  UriInfo uriInfo

  SeasonResource(SeasonDAO seasonDAO) {
    this.seasonDAO = seasonDAO
  }

  def debugPrintSeason(Season season) {
    System.out.println("*** Debug "+season.getSeason_id()+" "+season.getCommunity_name()+" "+season.getCycle_format()
            +" "+season.getCycle_count()+" "+season.getElo_default_seed()+" "+season.getYear())
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: List<Season>
  public Response getSeasons(@QueryParam("community_name") Optional<String> community_name,
                             @QueryParam("cycle_format") Optional<String> cycle_format,
                             @QueryParam("cycle_count") Optional<String> cycle_count,
                             @QueryParam("year") Integer year){

    //def yearQueryString = (year == null) ? "" : year.toString()

    List<Season> returnList = seasonDAO.getSeasonMatch(community_name.or(""),cycle_format.or(""),
            cycle_count.or(""),year)


    Response.ok(returnList).build()
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postSeason(@Valid Season newSeason){
    Response returnResponse
    URI createdURI

    try {
      //seasonDAO.postSeason(newSeason.getCommunity_name(),newSeason.getCycle_format(),newSeason.getCycle_count(),newSeason.getElo_default_seed(),newSeason.getYear())

      seasonDAO.postSeason(newSeason.community_name, newSeason.cycle_format, newSeason.cycle_count,
              newSeason.elo_default_seed, newSeason.year)

      createdURI = URI.create(uriInfo.getPath()+"/"+seasonDAO.getLatestSeasonId())
      System.out.println("*** DEBUG " + createdURI.toString())

      returnResponse = Response.created(createdURI).build()

    } catch (org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException e){

      def constraintError = e.cause.toString()
      System.out.println("*** DEBUG " + constraintError + "***")

      ErrorPOJO returnError
      if(constraintError.contains("SEASON_U_COMMUNITY_NAME")){//USER LOGIN IS NOT UNIQUE

        returnError = new ErrorPOJO("Community name is not unique", Response.Status.CONFLICT.getStatusCode())

      }else{//Some other error, should be logged

        System.out.println(e.localizedMessage)
        returnError = new ErrorPOJO("Unknown error.", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
      }

      return Response.status(returnError.getErrorCode()).entity(returnError).build()
      //System.out.println(returnError.getErrorMessage())
    }

    returnResponse
  }

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: List<Season>
  public Response getAll(){
    List<Season> returnList = seasonDAO.allSeasons()

    returnList.each { debugPrintSeason(it) }

    Response returnResponse = Response.ok(returnList).build()
    returnResponse
  }

  @Path("/{season_id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: Season
  public Response getSeasonById(@NotNull Integer season_id){
    Season returnSeason = seasonDAO.getSeasonById(season_id)
    Response returnResponse

    if (returnSeason == null) {
      ErrorPOJO returnError = new ErrorPOJO("Resource not found.",Response.Status.NOT_FOUND.getStatusCode())
      returnResponse = Response.status(Response.Status.NOT_FOUND).entity(returnError).build()

    }else{

      returnResponse = Response.ok(returnSeason).build()
    }

    returnResponse
  }

  @Path("/{season_id}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response putSeasonById(@PathParam("season_id") Integer season_id , Season newSeason){
    Response returnResponse

    Season checkForSeason_id = seasonDAO.getSeasonById(season_id)
    if(checkForSeason_id == null){
      seasonDAO.postUserToUserId(season_id,newSeason.community_name,newSeason.cycle_format,
              newSeason.cycle_count,newSeason.elo_default_seed,newSeason.year)

      URI createdURI = URI.create(uriInfo.getPath()+"/"+season_id)
      returnResponse = Response.created(createdURI).build()
    }else{


      //If the user is updating just their login or their display name we can use whats already in the DB using the
      //optional class or method.

      //TODO Edit specification to mention this detail
      //TODO Check swagger spec for header responses
      //TODO Check swagger spec for detailing constraints in the database

      String    newCommunity_name     = Optional.of(newSeason.community_name).or(checkForSeason_id.community_name)
      String    newCycle_format       = Optional.of(newSeason.cycle_format).or(checkForSeason_id.cycle_format)
      String    newCycle_count        = Optional.of(newSeason.cycle_count).or(checkForSeason_id.cycle_count)
      Integer   newElo_default_seed   = Optional.of(newSeason.elo_default_seed).or(checkForSeason_id.elo_default_seed)
      Integer   newYear               = Optional.of(newSeason.year).or(checkForSeason_id.year)

      seasonDAO.putUser(season_id,newCommunity_name,newCycle_format,newCycle_count,newElo_default_seed,newYear)
      returnResponse = Response.ok().build()

    }

    returnResponse

  }

}
