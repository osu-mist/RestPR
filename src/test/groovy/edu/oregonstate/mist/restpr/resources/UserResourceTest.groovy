import edu.oregonstate.mist.restpr.RESTPRApplication
import edu.oregonstate.mist.restpr.RESTPRConfiguration
import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.db.UserDAO
import edu.oregonstate.mist.restpr.resources.UserResource
import io.dropwizard.jackson.Jackson
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.ResourceTestRule

import org.junit.After
import io.dropwizard.testing.junit.DropwizardAppRule
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.*

class UserResourceTest{
  static UserDAO dao = mock(UserDAO.class)



  @ClassRule
  public static ResourceTestRule resources = ResourceTestRule.builder()
          .addResource(new UserResource(dao))
          .build()

  User user = new User(user_id: 1,  display_name: "DISPLAY_NAME_TEST", user_login: "USER_LOGIN_TEST")


  //TODO ASSERT THAT POST FAILS ON USER_LOGIN AND DISPLAY_NAME BEING THE SAME
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
  public void testGetAll_200() {
    //TODO make database model

    //Using mocking we isolate the behavior of the database by mocking the dao class
    //We can feed the mock the behavior (db responses) we want.
    //Using this isolation we can unit test the resource code and the responses we get back

    //DAO behavior we need to mock
    //List<User> allRESTPRUsers()
    // Code that tests one thing
    //Mock the DAO to test the resource's usage of the mocked DAO
    List<User> returnMockList = new ArrayList<User>()
    returnMockList.push(user)
    returnMockList.push(new User(user_id: 2,  display_name: "DISPLAY_NAME_2", user_login: "USER_LOGIN_2"))

    when(dao.allRESTPRUsers()).thenReturn(returnMockList)

    Response response = resources.client().target("/user/all")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK)
    assertThat(response.hasEntity()).isTrue()


  }

}