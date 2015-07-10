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

  @Path("/all")
  @GET
  @Produces (MediaType.APPLICATION_JSON)
  public List<User> getAll(){
    return userDAO.allRESTPRUsers()
  }

  @GET
  @Produces (MediaType.APPLICATION_JSON)
  public List<User> getUsers(@NotNull @QueryParam("display_name") Optional<String> display_name){
    //System.out.println("DEBUGGING DISPLAY_NAME VARIABLE USER GET %"+ display_name.or("") +"%");
    return userDAO.getPRUSERSmatch(display_name.or(""))

  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void postUser(@NotNull User newUser){
    userDAO.postUser(newUser.getUserLogin(),newUser.getDisplayName())

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