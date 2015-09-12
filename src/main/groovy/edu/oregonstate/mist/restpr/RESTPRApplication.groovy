package edu.oregonstate.mist.restpr

import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.auth.oauth.OAuthAuthenticator
import edu.oregonstate.mist.restpr.db.OAuthDAO
import edu.oregonstate.mist.restpr.db.SeasonDAO
import edu.oregonstate.mist.restpr.db.UserDAO
import edu.oregonstate.mist.restpr.resources.AuthTestResource
import edu.oregonstate.mist.restpr.resources.SeasonResource
import edu.oregonstate.mist.restpr.resources.UserResource
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.auth.AuthFactory
import io.dropwizard.auth.oauth.OAuthFactory
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
        final SeasonDAO mySeasonDao = jdbi.onDemand(SeasonDAO.class)
        final OAuthDAO myOAuthDao = jdbi.onDemand(OAuthDAO.class)

        environment.jersey().register(new UserResource(myUserDao))
        environment.jersey().register(new SeasonResource(mySeasonDao))

        environment.jersey().register(new AuthTestResource())
        environment.jersey().register(AuthFactory.binder(new OAuthFactory<User>(new OAuthAuthenticator(myOAuthDao), 'RESTPRApplication', User.class)))
        //TODO MAKE UNIT TESTS FOR AUTHENTICATOR
        //TODO ADD OAUTH RESOURCE WITH /oauth/auth and /oauth/token paths for token acquisition
    }

    public static void main(String[] arguments) throws Exception {
        new RESTPRApplication().run(arguments)
    }
}
