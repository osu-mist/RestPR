package edu.oregonstate.mist.restpr.resources

import com.fasterxml.jackson.databind.ObjectMapper
import edu.oregonstate.mist.restpr.RESTPRApplication
import edu.oregonstate.mist.restpr.RESTPRConfiguration
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.auth.oauth.OAuthAuthenticator
import edu.oregonstate.mist.restpr.db.OAuthDAO
import edu.oregonstate.mist.restpr.db.SeasonDAO
import edu.oregonstate.mist.restpr.db.UserDAO
import io.dropwizard.auth.AuthFactory
import io.dropwizard.auth.oauth.OAuthFactory
import io.dropwizard.jackson.Jackson
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Environment
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import io.dropwizard.testing.junit.ResourceTestRule
import org.junit.*
import org.skife.jdbi.v2.DBI

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static org.assertj.core.api.Assertions.assertThat
import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

/**
 * Created by georgecrary on 8/26/15.
 */
class AuthTestResourceTest {
  static OAuthDAO dao = mock(OAuthDAO.class)
  ObjectMapper MAPPER = Jackson.newObjectMapper()
  User user = new User(user_id: 1,  display_name: "DISPLAY_NAME_TEST", user_login: "USER_LOGIN_TEST")


  @ClassRule
  public static final DropwizardAppRule<RESTPRConfiguration> APPLICATION =
           new DropwizardAppRule<RESTPRConfiguration>(
                   RESTPRApplication.class,
                   ResourceHelpers.resourceFilePath('configuration.yaml'))

  @BeforeClass
  public static void setUpClass() throws Exception {
    // Code executed before the first test method
    //Environment environment = APPLICATION.getEnvironment()

    //environment.jersey().register(new AuthTestResource())
    //environment.jersey().register(AuthFactory.binder(new OAuthFactory<User>(new OAuthAuthenticator(dao), 'RESTPRApplication', User.class)))
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
  public void testBase64() throws Exception {
    String test_string = "username:password"
    String encoded = test_string.bytes.encodeBase64().toString()
    String decoded = new String(encoded.decodeBase64())

    System.out.println(encoded)
    //System.out.println(decoded)
    assertEquals(test_string, decoded)
  }

  @Test
  public void testIfCredentialsNull() throws Exception {
    //new OAuthAuthenticator(myOAuthDao)
  }

  //this test doesn't work because we need a dropwizard instance hooked up with oauth running
  @Test
  public void testValidToken() throws Exception {
    when(dao.checkForUserAgainstToken(anyString())).thenReturn(user)

    Response response = resources.client().target("/AuthTest/TestMe")
            .request(MediaType.TEXT_PLAIN)
            .header("Authorization","Bearer TEST_TOKEN")
            .delete()

    verify(dao).checkForUserAgainstToken("TEST_TOKEN")

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK)
    System.out.println(response.getEntity().toString())
    assertThat(response.getEntity().toString()).isEqualTo("You're authed.")
  }

  @Test
  public void testBadToken() throws Exception {

  }
}
