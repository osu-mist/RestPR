package edu.oregonstate.mist.restpr.resources

import edu.oregonstate.mist.restpr.api.User
import io.dropwizard.auth.Auth

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by georgecrary on 8/26/15.
 */
@Path("/AuthTest")
@Produces(MediaType.APPLICATION_JSON)
class AuthTestResource {
  @GET
  @Path('/TestMe')
  @Produces(MediaType.TEXT_PLAIN)
  public Response testMe(@Auth User user){
    Response returnResponse = Response.ok(new String("You're authed.")).build()
    returnResponse
  }

}
