package edu.oregonstate.mist.restpr.db

import edu.oregonstate.mist.restpr.api.Season
import edu.oregonstate.mist.restpr.mapper.SeasonMapper
import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

/**
 * Created by georgecrary on 7/13/15.
 */
@RegisterMapper(SeasonMapper)
public interface SeasonDAO extends Closeable  {

  @SqlQuery("""
            SELECT *
            FROM SEASON
            WHERE COMMUNITY_NAME LIKE '%'||:community_name_match||'%'
              AND CYCLE_FORMAT LIKE '%'||:cycle_format_match||'%'
              AND CYCLE_COUNT LIKE '%'||:cycle_count_match||'%'
              AND YEAR LIKE '%'||:year_match||'%'
            """)

  // || is the concat operator for Oracle
  List<Season> getSeasonMatch(@Bind("community_name") community_name_match,@Bind ("cycle_format_match") cycle_format_match, @Bind("cycle_count_match") cycle_count_match, @Bind("year_match") year_match)

  @SqlQuery("""
            SELECT *
            FROM SEASON
            """)
  List<Season> allSeasons()

  @SqlUpdate("""INSERT INTO SEASON (SEASON_ID, COMMUNITY_NAME, CYCLE_FORMAT, CYCLE_COUNT, ELO_DEFAULT_SEED, YEAR)
                VALUES (SEASON_SEQ.NEXTVAL, :community_name, :cycle_format, :cycle_count, :elo_default_seed, :year)
             """)
  void postSeason(@Bind("community_name") community_name, @Bind("cycle_format") cycle_format,
                  @Bind("cycle_count") cycle_count, @Bind("elo_default_seed") elo_default_seed, @Bind("year") year)

  @SqlUpdate( """
              UPDATE SEASON
              SET SEASON_ID = :season_id , COMMUNITY_NAME = :community_name , CYCLE_FORMAT = :cycle_format ,
                  CYCLE_COUNT = :cycle_count, ELO_DEFAULT_SEED = :elo_default_seed, YEAR = :year)
              WHERE SEASON_ID = :season_id
              """)
  void putUser(@Bind("season_id") Integer season_id, @Bind("community_name") community_name,
               @Bind("cycle_format") cycle_format, @Bind("cycle_count") cycle_count,
               @Bind("elo_default_seed") elo_default_seed, @Bind("year") year)

  @SqlQuery("""
            SELECT *
            FROM SEASON
            WHERE SEASON_ID = :season_id
            """)
  Season getSeasonById(@Bind("season_id") Integer season_id)

  @SqlUpdate("""INSERT INTO SEASON (SEASON_ID, COMMUNITY_NAME, CYCLE_FORMAT, CYCLE_COUNT, ELO_DEFAULT_SEED, YEAR)
                VALUES (:season_id, :community_name, :cycle_format, :cycle_count, :elo_default_seed, :year)
             """)
  void postUserToUserId(@Bind("season_id") Integer season_id, @Bind("community_name") community_name,
                        @Bind("cycle_format") cycle_format, @Bind("cycle_count") cycle_count,
                        @Bind("elo_default_seed") elo_default_seed, @Bind("year") year)

  @SqlUpdate( """
              DELETE FROM SEASON
              WHERE SEASON_ID = :season_id
              """)
  void deleteSeasonById(@Bind("season_id") Integer season_id)

  @SqlQuery("""
            SELECT SEASON_SEQ.CURRVAL FROM DUAL
            """)
  Integer getLatestSeasonId()

}
