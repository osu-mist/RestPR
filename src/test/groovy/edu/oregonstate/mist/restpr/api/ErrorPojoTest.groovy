package edu.oregonstate.mist.restpr.api

import static io.dropwizard.testing.FixtureHelpers.fixture
import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.jackson.Jackson
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Created by georgecrary on 8/17/15.
 */
class ErrorPojoTest {
  ObjectMapper MAPPER = Jackson.newObjectMapper()

  @Test
  void serializeToJSON() throws Exception {
    ErrorPOJO error = new ErrorPOJO(
            errorMessage: "Resource not found",
            errorCode: 404
    )
    String actual = MAPPER.writeValueAsString(error)

    String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/errorpojo.json"), ErrorPOJO.class))

    assertEquals(expected, actual)
  }

  //TODO ADD DESERIALIZATION TEST
}
