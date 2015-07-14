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

@Path("/user")
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

  @Path("/{user_id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: User
  public Response findUserById(@PathParam("user_id") Integer user_id) {
    User returnUser = userDAO.getUserById(user_id);
    def returnResponse;
    if (returnUser == null) {
      def returnError = new ErrorPOJO("Resource not found.",Response.Status.NOT_FOUND.getStatusCode())
      returnResponse = Response.status(Response.Status.NOT_FOUND).entity(returnError).build()

    }else{

      returnResponse = Response.ok(returnUser).build()
    }

    return returnResponse;

  }

  @Path("/{user_id}")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response putUserById(@PathParam("user_id") Integer user_id , User newUser){

    //TODO TEST RESPONSE CODES

    def returnResponse;

    User checkForUser_id = userDAO.getUserById(user_id);
    if(checkForUser_id == null){
      userDAO.postUserToUserId(user_id, newUser.getUserLogin(),newUser.getDisplayName())
      returnResponse = Response.created().build()
    }else{
      Optional<String> newDisplayName = Optional.of( newUser.getDisplayName() )
      Optional<String> newUserLogin = Optional.of( newUser.getUserLogin())

      //If the user is updating just their login or their display name we can use whats already in the DB using the
      //optional class or method.
      userDAO.putUser(user_id ,newUserLogin.or(checkForUser_id.getUserLogin()) ,newDisplayName.or(checkForUser_id.getDisplayName()));
      returnResponse = Response.ok().build()
    }

    return returnResponse
  }

  @Path("/{user_id}")
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUserById(@PathParam("user_id") Integer user_id){
    //TODO add authentication for this method
    userDAO.deleteUserById(user_id)
    def returnResponse = Response.ok().build()

    return returnResponse;

  }

}