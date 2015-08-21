package edu.oregonstate.mist.restpr.resources

import edu.oregonstate.mist.restpr.api.ErrorPOJO
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
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException

import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static io.dropwizard.testing.FixtureHelpers.fixture
import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.*

class UserResourceTest{
  static UserDAO dao = mock(UserDAO.class)
  ObjectMapper MAPPER = Jackson.newObjectMapper()
  @ClassRule
  public static ResourceTestRule resources = ResourceTestRule.builder()
          .addResource(new UserResource(dao))
          .build()

  List<User> returnMockList
  User user = new User(user_id: 1,  display_name: "DISPLAY_NAME_TEST", user_login: "USER_LOGIN_TEST")


  //TODO ASSERT THAT POST FAILS ON USER_LOGIN AND DISPLAY_NAME BEING THE SAME
  @BeforeClass
  public static void setUpClass() throws Exception {
    // Code executed before the first test method

  }

  @Before
  public void setUp() throws Exception {
    // Code executed before each test
    returnMockList = new ArrayList<User>()
    returnMockList.push(user)
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
    //Mock the DAO to test the resource's usage of the mocked DAO
    when(dao.allRESTPRUsers()).thenReturn(returnMockList)

    Response response = resources.client().target("/user/all")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK)
    assertThat(response.hasEntity()).isTrue()
  }

  @Test
  public void testGetMatch_200(){
    when(dao.getPRUSERSmatch(anyString(),anyString())).thenReturn(returnMockList)

    Response response = resources.client().target("/user/")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK)
    assertThat(response.hasEntity()).isTrue()
  }

  @Test
  public void testPost_409_Duplicate_Fields(){
    User duplicate_fields = new User(user_id: 1, user_login: "dupe", display_name: "dupe")

    Response response = resources.client().target("/user/")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(duplicate_fields,MediaType.APPLICATION_JSON_TYPE))

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CONFLICT)
    assertThat(response.hasEntity()).isTrue()

    //If the response entity type isn't of the expected type the Mapper will fail (along with the whole test) and most likely throw an UnrecognizedPropertyException
    //FIXME There might be a better way to assert the JSON we recieve is the right type
    ErrorPOJO responseError = MAPPER.readValue(response.getEntity(),ErrorPOJO.class)
    assertThat(responseError.getErrorCode()).isEqualTo(Response.Status.CONFLICT.getStatusCode())
    assertThat(responseError.getErrorMessage()).isEqualTo("user_login and display_name fields are not unique.")
  }

  @Test
  public void testPost_409_User_login_Unique_Constraint_Violation(){
    UnableToExecuteStatementException nonUniqueUser_loginException = new UnableToExecuteStatementException("java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (MISTSTU1.PR_USER_U_USER_LOGIN) violated")

    when(dao.postUser(anyString(),anyString())).thenThrow(nonUniqueUser_loginException)

    Response response = resources.client().target("/user/")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(user,MediaType.APPLICATION_JSON_TYPE))

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CONFLICT)
    assertThat(response.hasEntity()).isTrue()

    //FIXME There might be a better way to assert the JSON we recieve is the right type
    ErrorPOJO responseError = MAPPER.readValue(response.getEntity(),ErrorPOJO.class)
    assertThat(responseError.getErrorCode()).isEqualTo(Response.Status.CONFLICT.getStatusCode())
    assertThat(responseError.getErrorMessage()).isEqualTo("User login is not unique.")

    //MAPPER.writeValueAsString(MAPPER.readValue(response.getEntity(),ErrorPOJO.class))
  }

  @Test
  public void testPost_409_Unique_Constraint_Violation(){
    UnableToExecuteStatementException nonUniqueDisplay_nameException = new UnableToExecuteStatementException("java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (MISTSTU1.PR_USER_U_DISPLAY_NAME) violated")

    when(dao.postUser(anyString(),anyString())).thenThrow(nonUniqueDisplay_nameException)

    Response response = resources.client().target("/user/")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(user,MediaType.APPLICATION_JSON_TYPE))

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CONFLICT)
    assertThat(response.hasEntity()).isTrue()

    //FIXME There might be a better way to assert the JSON we recieve is the right type
    ErrorPOJO responseError = MAPPER.readValue(response.getEntity(),ErrorPOJO.class)
    assertThat(responseError.getErrorCode()).isEqualTo(Response.Status.CONFLICT.getStatusCode())
    assertThat(responseError.getErrorMessage()).isEqualTo("Display name is not unique.")
  }




}