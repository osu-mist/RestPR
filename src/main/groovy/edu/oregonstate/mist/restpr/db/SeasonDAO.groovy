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

  /**
   * Selects seasons that match the double ended wildcard match clauses using community_name_match, cycle_format_match
   * cycle_count_match, and year_match. In the event these parameters are empty strings it selects all seasons.
   *
   * @param community_name_match  Community name to be partially matched for
   * @param cycle_format_match    Cycle Format to be partially matched for
   * @param cycle_count_match     Cycle count to be partially matched for
   * //TODO consider changing year to strict equality matching
   * @param year_match            Year to be partially matched for
   * @return all seasons that match the parameter partial like clauses
   */
  @SqlQuery("""
            SELECT *
            FROM SEASON
            WHERE COMMUNITY_NAME LIKE '%'||:community_name_match||'%'
              AND CYCLE_FORMAT LIKE '%'||:cycle_format_match||'%'
              AND CYCLE_COUNT LIKE '%'||:cycle_count_match||'%'
              AND SYEAR LIKE '%'||:year_match||'%'
            """)

  // || is the concat operator for Oracle
  List<Season> getSeasonMatch(@Bind("community_name_match") String community_name_match,
                              @Bind ("cycle_format_match") String cycle_format_match,
                              @Bind("cycle_count_match") String cycle_count_match,
                              @Bind("year_match") Integer year_match)

  /**
   * Selects all seasons from the season table
   *
   * @return all seasons from the table
   */
  @SqlQuery("""
            SELECT *
            FROM SEASON
            """)
  List<Season> allSeasons()

  /**
   * Inserts a new season into the season table and uses the season sequence for the id
   *
   * @param community_name    Community name of the new season to be inserted
   * @param cycle_format      Cycle format of the new season to be inserted
   * @param cycle_count       Cycle count of the new season to be inserted
   * @param elo_default_seed  Elo default seed of the new season to be inserted
   * @param year              Year of the new season to be inserted
   */
  @SqlUpdate("""INSERT INTO SEASON (SEASON_ID, COMMUNITY_NAME, CYCLE_FORMAT, CYCLE_COUNT, ELO_DEFAULT_SEED, SYEAR)
                VALUES (SEASON_SEQ.NEXTVAL, :community_name, :cycle_format, :cycle_count, :elo_default_seed, :year)
             """)
  void postSeason(@Bind("community_name") String community_name, @Bind("cycle_format") String cycle_format,
                  @Bind("cycle_count") String cycle_count, @Bind("elo_default_seed") Integer elo_default_seed,
                  @Bind("year") Integer year)

  /**
   * Updates a specified season row using the season_id parameter using the other parameters as the updated values
   *
   * @param season_id         Season id of the season record to be updated
   * @param community_name    Community name of the season record to be updated
   * @param cycle_format      Cycle format of the season record to be updated
   * @param cycle_count       Cycle count of the season record to be updated
   * @param elo_default_seed  Elo default seed of the season record to be updated
   * @param year              Year of the season record to be updated
   */
  @SqlUpdate( """
              UPDATE SEASON
              SET SEASON_ID = :season_id , COMMUNITY_NAME = :community_name , CYCLE_FORMAT = :cycle_format ,
                  CYCLE_COUNT = :cycle_count, ELO_DEFAULT_SEED = :elo_default_seed, SYEAR = :year
              WHERE SEASON_ID = :season_id
              """)
  void putUser(@Bind("season_id") Integer season_id, @Bind("community_name") String community_name,
               @Bind("cycle_format") String cycle_format, @Bind("cycle_count") String cycle_count,
               @Bind("elo_default_seed") Integer elo_default_seed, @Bind("year") Integer year)

  /**
   * Selects and returns a specific season using the season_id
   *
   * @param season_id Season_id of the season record to be selected and returned
   * @return a specific season using the season_id given
   */
  @SqlQuery("""
            SELECT *
            FROM SEASON
            WHERE SEASON_ID = :season_id
            """)
  Season getSeasonById(@Bind("season_id") Integer season_id)

  /**
   * Inserts a season record to a specified row using the season id
   *
   * @param season_id         Season_id of the row to be inserted to
   * @param community_name    Community name of the row to be inserted with
   * @param cycle_format      Cycle format of the row to be inserted with
   * @param cycle_count       Cycle count of the row to be inserted with
   * @param elo_default_seed  Elo default seed of the row to be inserted with
   * @param year              Year of the row to be inserted with
   */
  @SqlUpdate("""INSERT INTO SEASON (SEASON_ID, COMMUNITY_NAME, CYCLE_FORMAT, CYCLE_COUNT, ELO_DEFAULT_SEED, SYEAR)
                VALUES (:season_id, :community_name, :cycle_format, :cycle_count, :elo_default_seed, :year)
             """)
  void postUserToUserId(@Bind("season_id") Integer season_id, @Bind("community_name") String community_name,
                        @Bind("cycle_format") String cycle_format, @Bind("cycle_count") String cycle_count,
                        @Bind("elo_default_seed") Integer elo_default_seed, @Bind("year") Integer year)

  /**
   * Deletes a specified season row from the table
   *
   * @param season_id season_id of the season to be deleted
   */
  @SqlUpdate( """
              DELETE FROM SEASON
              WHERE SEASON_ID = :season_id
              """)
  void deleteSeasonById(@Bind("season_id") Integer season_id)
  /**
   * Returns the season_id of the latest season created using the season sequence
   * //FIXME possible race condition
   *
   * @return the season_id of the latest season created using the season sequence
   */
  @SqlQuery("""
            SELECT SEASON_SEQ.CURRVAL FROM DUAL
            """)
  Integer getLatestSeasonId()

}
