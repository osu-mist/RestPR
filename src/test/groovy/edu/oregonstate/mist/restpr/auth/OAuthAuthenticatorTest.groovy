package edu.oregonstate.mist.restpr.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Optional
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.auth.oauth.OAuthAuthenticator
import edu.oregonstate.mist.restpr.db.OAuthDAO
import io.dropwizard.jackson.Jackson
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

/**
 * Created by georgecrary on 9/17/15.
 */
class OAuthAuthenticatorTest {
  private final OAuthDAO dao = mock(OAuthDAO.class)
  OAuthAuthenticator testAuthenticator = new OAuthAuthenticator(dao)
  User user = new User(user_id: 1,  display_name: "DISPLAY_NAME_TEST", user_login: "USER_LOGIN_TEST")

  @BeforeClass
  public static void setUpClass() throws Exception {
    // Code executed before the first test method

  }

  @Before
  public void setUp() throws Exception {
    // Code executed before each test
  }

  @After
  public void tearDown() throws Exception {
    // Code executed after each test
    reset(dao)
  }

  @Test
  public void testAuthenticateNullCredentials() throws Exception {
    assertEquals(Optional.absent(),testAuthenticator.authenticate(null))
  }

  @Test
  public void testAuthenticateInvalidToken() throws Exception {
    when(dao.checkForUserAgainstToken(anyString())).thenReturn(null)

    Optional<User> expected = Optional.absent()
    Optional<User> actual   = testAuthenticator.authenticate("Invalid Token")

    verify(dao).checkForUserAgainstToken("Invalid Token")
    assertEquals(expected, actual)
  }

  @Test
  public void testAuthenticateValidToken() throws Exception {
    when(dao.checkForUserAgainstToken(anyString())).thenReturn(user)

    Optional<User> expected = Optional.of(user)
    Optional<User> actual   = testAuthenticator.authenticate("Valid Token")

    verify(dao).checkForUserAgainstToken("Valid Token")
    assertEquals(expected, actual)
  }
}