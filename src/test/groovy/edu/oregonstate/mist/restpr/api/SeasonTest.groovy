package edu.oregonstate.mist.restpr.api

import static io.dropwizard.testing.FixtureHelpers.fixture
import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.jackson.Jackson
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Created by georgecrary on 8/17/15.
 */
class SeasonTest {

  ObjectMapper MAPPER = Jackson.newObjectMapper()

  @Test
  void serializesToJSON() throws Exception{
    Season season = new Season(  season_id:22,
                                 community_name : "Test POST",
                                 cycle_format :  "Feb",
                                 cycle_count : "Monthly",
                                 elo_default_seed: 1200,
                                 year: 2015
                               )

    String actual = MAPPER.writeValueAsString(season)

    String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/season.json"), season.class))



    assertEquals(expected, actual)

  }

  @Test
  void deserializeFromJSON() throws Exception{
    Season expected = new Season(  season_id:22,
            community_name : "Test POST",
            cycle_format :  "Feb",
            cycle_count : "Monthly",
            elo_default_seed: 1200,
            year: 2015
    )

    Season actual = MAPPER.readValue(fixture("fixtures/season.json"),Season.class)

    assertEquals(expected, actual)
  }
}
