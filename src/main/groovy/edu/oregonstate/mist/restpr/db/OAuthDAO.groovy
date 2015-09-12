package edu.oregonstate.mist.restpr.db

import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.mapper.UserMapper
import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

/**
 * Created by georgecrary on 9/10/15.
 */

@RegisterMapper(UserMapper)
public interface OAuthDAO extends Closeable  {
  @SqlQuery("""
            select A.USER_ID, USER_LOGIN, DISPLAY_NAME
            from OAUTH_USER A
            INNER JOIN PR_USER B
            ON A.USER_ID = B.USER_ID
            where A.TOKEN = :TOKEN
            """)
  User checkForUserAgainstToken(@Bind("TOKEN") String token)

  //TODO MAKE TOKEN GENERATION
}