package edu.oregonstate.mist.restpr.resources
/**
 * Created by georgecrary on 6/27/15.
 */
import com.google.common.base.Optional

import edu.oregonstate.mist.restpr.api.ErrorPOJO
import edu.oregonstate.mist.restpr.api.User;

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

@Path("/User")
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
  private final UserDAO userDAO;

  public UserResource(UserDAO userDAO){
    this.userDAO = userDAO;
  }

  def debugPrintUser(User user) {
    System.out.println("*** DEBUG USERLOGIN:\""+ user.getUserLogin() + "\" DISPLAYNAME:\"" + user.getDisplayName() + "\"***")
  }

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: List<User>
  public Response getAll() {
    List<User> returnList = userDAO.allRESTPRUsers()

    returnList.each { debugPrintUser(it) }

    def returnResponse = Response.ok(returnList).build()
    return returnResponse;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity type: List<User>
  public Response getUsers(@NotNull @QueryParam("user_login") Optional<String> user_login ,
                             @NotNull @QueryParam("display_name") Optional<String> display_name) {
    //System.out.println("DEBUGGING DISPLAY_NAME VARIABLE USER GET %"+ display_name.or("") +"%");
    List<User> returnList = userDAO.getPRUSERSmatch(user_login.or("") , display_name.or(""))
    returnList.each { debugPrintUser(it) }

    def returnResponse = Response.ok(returnList).build()
    return returnResponse

  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postUser(@NotNull User newUser) {
    Response returnResponse;
    def createdURI;

    System.out.println("*** DEBUG "+ newUser.getUserLogin() + " " + newUser.getDisplayName() + "***")

    try {
      userDAO.postUser(newUser.getUserLogin() , newUser.getDisplayName())
      //createdURI = URI.create("/"+userDAO)

      //201 CREATED
      //TODO add in the URI of newly created resource
      returnResponse = Response.created().build()

    } catch (org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException e){

      def constraintError = e.cause.toString()
      //System.out.println("*** DEBUG " + constraintError + "***")
      def returnError;
      if(constraintError.contains("PR_USER_U_USER_LOGIN")){//USER LOGIN IS NOT UNIQUE

        returnError = new ErrorPOJO("User login is not unique", Response.Status.CONFLICT.getStatusCode())
      }else if(constraintError.contains("PR_USER_U_DISPLAY_NAME")){//DISPLAY NAME IS NOT UNIQUE

        returnError = new ErrorPOJO("Display name is not unique", Response.Status.CONFLICT.getStatusCode())

      }else{//Some other error, should be logged
        //System.out.println(e.localizedMessage)
        returnError = new ErrorPOJO("Unknown error.", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
      }

      return Response.status(Response.Status.CONFLICT).entity(returnError).build()

      //System.out.println(returnError.getErrorMessage())
    }

    return returnResponse;
  }

  @Path("/{userid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public User findUserById(@PathParam("userid") Integer userid){
    User returnUser = userDAO.getUserById(userid);

    if(returnUser == null){
      throw new WebApplicationException(404)
    }

    return returnUser;

  }
}