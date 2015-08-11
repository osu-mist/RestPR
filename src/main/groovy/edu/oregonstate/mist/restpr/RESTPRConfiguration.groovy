package edu.oregonstate.mist.restpr

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

/**
 * Created by georgecrary on 6/29/15.
 */
public class RESTPRConfiguration extends Configuration {
    DataSourceFactory database = new DataSourceFactory()

}
