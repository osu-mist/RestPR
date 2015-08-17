package edu.oregonstate.mist.restpr.api

import static io.dropwizard.testing.FixtureHelpers.fixture
import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.jackson.Jackson
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Created by georgecrary on 8/17/15.
 */
class UserTest{

  ObjectMapper MAPPER = Jackson.newObjectMapper()

  @Test
  void serializesToJSON() throws Exception{
    User user = new User(user_id: 1, display_name: "DISPLAY_NAME_TEST" ,user_login: "USER_LOGIN_TEST")
    String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/user.json"), User.class))

    String actual = MAPPER.writeValueAsString(user)

    assertEquals(expected, actual)

  }

  @Test
  void deserializesFromJSON() throws Exception{
    User expected = new User(user_id: 1, display_name: "DISPLAY_NAME_TEST" ,user_login: "USER_LOGIN_TEST")
    User actual = MAPPER.readValue(fixture("fixtures/user.json"), User.class)

    assertEquals(expected,actual)


  }
}