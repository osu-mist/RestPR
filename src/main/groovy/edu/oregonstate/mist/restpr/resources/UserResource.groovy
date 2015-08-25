package edu.oregonstate.mist.restpr.resources
/**
 * Created by georgecrary on 6/27/15.
 */

import edu.oregonstate.mist.restpr.api.ErrorPOJO
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.db.UserDAO
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException

import javax.validation.Valid
import com.google.common.base.Optional
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
import javax.ws.rs.core.UriBuilder
import javax.ws.rs.core.UriInfo

//TODO look up VALID annotation
//TODO look up NotNull annoation

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
class UserResource {
  private final UserDAO userDAO

  @Context
  UriInfo uriInfo

  public UserResource(UserDAO userDAO){
    this.userDAO = userDAO
  }

  /**
   *  Returns all RestPR Users in the table.
   * @return all Users in the RestPR User table within response body.
   */
  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: List<User>
  public Response getAll() {
    List<User> returnList = userDAO.allRESTPRUsers()

    Response returnResponse = Response.ok(returnList).build()
    returnResponse
  }

  /**
   *  Returns all of the Users that match the where like %user_login% and %display_name% clause.
   * @param user_login    The login to partially match against the Users in the table.
   * @param display_name  The display name to partially match against the Users in the table.
   * @return  all of the Users that match the where like %user_login% and %display_name% clause within response body.
   *
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity type: List<User>
  public Response getUsers(@NotNull @QueryParam("user_login") Optional<String> user_login ,
                             @NotNull @QueryParam("display_name") Optional<String> display_name) {
    List<User> returnList = userDAO.getPRUSERSmatch(user_login.or("") , display_name.or(""))

    Response returnResponse = Response.ok(returnList).build()
    returnResponse

  }

  /**
   *  Posts new user into the User table as long as it does not violate the unique key constraints on both fields.
   * @param newUser   The new user to be posted to the database.
   * @return Response code with uri of the newly created user
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: URI or ErrorPOJO
  public Response postUser(@Valid User newUser) {
    Response returnResponse
    if(newUser.getUser_login() == newUser.getDisplay_name()){
      ErrorPOJO returnError = new ErrorPOJO(errorMessage:  "user_login and display_name fields are not unique.", errorCode: Response.Status.CONFLICT.getStatusCode())
      return Response.status(returnError.getErrorCode()).entity(returnError).build()

    }else{
      try {
        userDAO.postUser(newUser.getUser_login(),newUser.getDisplay_name())
        URI createdURI = UriBuilder.fromResource(UserResource.class).path("{user_id}").build(userDAO.getLatestUserId())

        returnResponse = Response.created(createdURI).build()
        //FIXME NOTE https://github.com/dropwizard/dropwizard/issues/878
        //Created uri can't be set correctly due to a Jersey bug.
      } catch (UnableToExecuteStatementException e){
        String constraintError = e.getMessage()

        ErrorPOJO returnError
        if(constraintError.contains("PR_USER_U_USER_LOGIN")){//USER LOGIN IS NOT UNIQUE
          //e.cause will contain this
          //"java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (MISTSTU1.PR_USER_U_USER_LOGIN) violated"

          returnError = new ErrorPOJO(errorMessage: "User login is not unique.",errorCode: Response.Status.CONFLICT.getStatusCode())
        }else if(constraintError.contains("PR_USER_U_DISPLAY_NAME")){//DISPLAY NAME IS NOT UNIQUE
          //e.cause will contain this
          //"java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (MISTSTU1.PR_USER_U_DISPLAY_NAME) violated"
          
          returnError = new ErrorPOJO(errorMessage: "Display name is not unique.",errorCode: Response.Status.CONFLICT.getStatusCode())

        }else{//Some other error, should be logged
          returnError = new ErrorPOJO(errorMessage: "Unknown error.", errorCode: Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
        }

        return Response.status(returnError.getErrorCode()).entity(returnError).build()
      }
    }
    returnResponse
  }

  /**
   * Returns the user with the specified user_id
   * @param user_id   The user_id of the user that is to be returned
   * @return Returns the user with the specified user_id
   */
  @Path("/{user_id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  //Entity Type: User
  public Response findUserById(@PathParam("user_id") Integer user_id) {
    User returnUser = userDAO.getUserById(user_id)
    Response returnResponse
    if (returnUser == null) {
      ErrorPOJO returnError = new ErrorPOJO(errorMessage:  "Resource not found.",errorCode:  Response.Status.NOT_FOUND.getStatusCode())
      returnResponse = Response.status(Response.Status.NOT_FOUND).entity(returnError).build()

    }else{

      returnResponse = Response.ok(returnUser).build()
    }

    returnResponse

  }

  /**
   * Updates the user specified with the user_id within the table to be updated with the newUser representation.
   * @param user_id   User_id of the user to be updated
   * @param newUser   Representation of the new User to be updated with
   * @return An okay response on success
   */
  @Path("/{user_id}")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response putUserById(@PathParam("user_id") Integer user_id ,@Valid User newUser){ 
    Response returnResponse

    User checkForUser_id = userDAO.getUserById(user_id)
    if(checkForUser_id == null){
      userDAO.postUserToUserId(user_id, newUser.getUser_login(),newUser.getDisplay_name())
      //FIXME NOTE https://github.com/dropwizard/dropwizard/issues/878
      //Created uri can't be set correctly due to a Jersey bug.
      URI createdURI = URI.create("/"+user_id)
      returnResponse = Response.created(createdURI).build()
    }else{
      String newDisplayName = Optional.of( newUser.getDisplay_name()).or(checkForUser_id.getDisplay_name())
      String newUserLogin = Optional.of( newUser.getUser_login()).or(checkForUser_id.getUser_login())

      //If the user is updating just their login or their display name we can use whats already in the DB using the
      //optional class or method.
      userDAO.putUser(user_id ,newUserLogin,newDisplayName)
      returnResponse = Response.ok().build()
    }

    returnResponse
  }

  /**
   * Deletes the specified user with the given user_id
   * @param User_id   User_id of the user to be deleted
   * @return An okay response
   */
  @Path("/{user_id}")
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUserById(@PathParam("user_id") Integer user_id){
    //TODO add authentication for this method
    userDAO.deleteUserById(user_id)
    Response returnResponse = Response.ok().build()

    returnResponse

  }

}
