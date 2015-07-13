package edu.oregonstate.mist.restpr.mapper

import edu.oregonstate.mist.restpr.api.Season
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by georgecrary on 7/13/15.
 */
public class SeasonMapper implements ResultSetMapper<Season> {
  public Season map(int i, ResultSet rs, StatementContext sc) throws SQLException{
    Season season = new Season()

    season.with {
      season_id         = rs.getInt     'SEASON_ID'
      community_name    = rs.getString  'COMMUNITY_NAME'
      cycle_format      = rs.getString  'CYCLE_FORMAT'
      cycle_count       = rs.getString  'CYCLE_COUNT'
      elo_default_seed  = rs.getInt     'ELO_DEFAULT_SEED'
      year              = rs.getInt     'YEAR'
    }

    return season;
  }
}