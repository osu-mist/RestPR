package edu.oregonstate.mist.restpr.resources

import com.google.common.base.Optional

import edu.oregonstate.mist.restpr.api.ErrorPOJO
import edu.oregonstate.mist.restpr.api.Season
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.db.SeasonDAO;
import edu.oregonstate.mist.restpr.db.UserDAO

import javax.validation.constraints.NotNull

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE;
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT;
import javax.ws.rs.Path
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response;

/**
 * Created by georgecrary on 7/13/15.
 */

@Path("/Season")
@Produces(MediaType.APPLICATION_JSON)
class SeasonResource {
  private final SeasonDAO seasonDAO;

  SeasonResource(SeasonDAO seasonDAO) {
    this.seasonDAO = seasonDAO
  }

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: List<Season>
  public Response getAll(){
    List<Season> returnList = seasonDAO.allSeasons()

    //TODO make debug print season method
    //returnList.each { debugPrintSeason(it) }

    def returnResponse = Response.ok(returnList).build()
    return returnResponse;
  }

  @Path("/{season_id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: Season
  public Response getSeasonById(@NotNull Integer season_id){
    Season returnSeason = seasonDAO.getSeasonById(season_id)
    def returnResponse

    if (returnSeason == null) {
      def returnError = new ErrorPOJO("Resource not found.",Response.Status.NOT_FOUND.getStatusCode())
      returnResponse = Response.status(Response.Status.NOT_FOUND).entity(returnError).build()

    }else{

      returnResponse = Response.ok(returnSeason).build()
    }

    return returnResponse
  }

}