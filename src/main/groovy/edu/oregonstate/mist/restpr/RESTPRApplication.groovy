package edu.oregonstate.mist.restpr

import edu.oregonstate.mist.restpr.db.UserDAO
import edu.oregonstate.mist.restpr.resources.UserResource
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI

class RESTPRApplication extends Application<RESTPRConfiguration>{

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
      //bootstrap.getObjectMapper().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Override
    public void run(RESTPRConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory()
        final DBI jdbi = factory.build(environment, configuration.getDatabase(),"jdbi")
        final UserDAO myUserDao = jdbi.onDemand(UserDAO.class)

        environment.jersey().register(new UserResource(myUserDao))
    }

    public static void main(String[] arguments) throws Exception {
        new RESTPRApplication().run(arguments)
    }
}
