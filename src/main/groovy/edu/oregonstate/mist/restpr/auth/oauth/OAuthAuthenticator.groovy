package edu.oregonstate.mist.restpr.auth.oauth

import com.google.common.base.Optional
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.db.OAuthDAO
import io.dropwizard.auth.AuthenticationException

/**
 * Created by georgecrary on 9/2/15.
 */
class OAuthAuthenticator implements io.dropwizard.auth.Authenticator<String, User> {
  private final OAuthDAO ouathDAO

  OAuthAuthenticator(OAuthDAO ouathDAO) {
    this.ouathDAO = ouathDAO
  }

  Optional<User> authenticate(String credentials) throws AuthenticationException {
    if(credentials != null){
      Optional<User> checkedCredentials = Optional.fromNullable( ouathDAO.checkForUserAgainstToken(credentials) )
      if(checkedCredentials.present) {
        return checkedCredentials
      }
    }

    Optional.absent()
  }

}