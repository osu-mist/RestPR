package edu.oregonstate.mist.webapiskeleton.resources
/**
 * Created by georgecrary on 6/27/15.
 */
import edu.oregonstate.mist.webapiskeleton.core.User;
import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed

import javax.management.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/User")
@Produces(MediaType.APPLICATION_JSON)
class UserResource {

  @GET
  public User getUsers(@QueryParam("User_Login") Optional<String> user_login,
                       @QueryParam("Display_Name") Optional<String> display_name){


  }
}




@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
  private final String template;
  private final String defaultName;
  private final AtomicLong counter;

  public HelloWorldResource(String template, String defaultName) {
    this.template = template;
    this.defaultName = defaultName;
    this.counter = new AtomicLong();
  }

  @GET
  @Timed
  public Saying sayHello(@QueryParam("name") Optional<String> name) {
    final String value = String.format(template, name.or(defaultName));
    return new Saying(counter.incrementAndGet(), value);
  }
}