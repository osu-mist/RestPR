package edu.oregonstate.mist.restpr.db

import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.mapper.UserMapper
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

/**
 * Created by georgecrary on 6/27/15.
 */
@RegisterMapper(UserMapper)
public interface UserDAO extends Closeable {
  /**
   * Selects users that match the double ended wildcard match clauses using display_name_match and user_login_match.
   * In the event these are empty strings it selects all users.
   * @param user_login_match      User login to be matched for
   * @param display_name_match    Display name to be matched for
   * @return the selected users that have partial matching user logins and display names
   */
  @SqlQuery("""
            SELECT DISPLAY_NAME, USER_ID, USER_LOGIN
            FROM PR_USER
            WHERE DISPLAY_NAME LIKE '%'||:display_name_match||'%'
            AND USER_LOGIN LIKE '%'||:user_login_match||'%'
            """)

  // || is the concat operator for Oracle
  List<User> getPRUSERSmatch(@Bind("user_login_match") String user_login_match, @Bind("display_name_match") String display_name_match)

  /**
   * Selects all users from the user table
   * @return all users from the user table
   */
  @SqlQuery("""
            SELECT DISPLAY_NAME, USER_ID, USER_LOGIN
            FROM PR_USER
            """)
  List<User> allRESTPRUsers()

  /**
   * Inserts a new user representation into the user table and uses the user sequence to set the user id
   * @param user_login    The new user's login
   * @param display_name  The new user's display name
   */
  @SqlUpdate("insert into PR_USER (USER_ID, USER_LOGIN, DISPLAY_NAME) values (PR_USER_SEQ.NEXTVAL, :user_login, :display_name)")
  void postUser(@Bind("user_login") String user_login, @Bind("display_name") String display_name)

  /**
   * Updates a user with the given user_id with a new user_login and a new display_name
   * @param user_id       The specified user to be updated
   * @param user_login    The new user_login to be updated with
   * @param display_name  The new display name to be updated with
   */
  @SqlUpdate( """
              UPDATE PR_USER
              SET USER_LOGIN = :user_login , DISPLAY_NAME = :display_name
              WHERE USER_ID = :user_id
              """)
  void putUser(@Bind("user_id") Integer user_id,@Bind("user_login") String user_login, @Bind("display_name") String display_name)

  /**
   * Selects a specified user using the given user_id
   * @param user_id   The specified user to be selected with this id
   * @return the specified user using the given user_id
   */
  @SqlQuery("""
            SELECT *
            FROM PR_USER
            WHERE USER_ID = :user_id
            """)
  User getUserById(@Bind("user_id") Integer user_id)

  /**
   * Posts a new user to the user table with the given user_id as the user_id to be used in the row.
   * @param user_id       The user_id of the new user to be inserted to
   * @param user_login    The user login of the new user
   * @param display_name  The display name of the new user
   */
  @SqlUpdate("insert into PR_USER (USER_ID, USER_LOGIN, DISPLAY_NAME) values (:user_id, :user_login, :display_name)")
  void postUserToUserId(@Bind("user_id") Integer user_id , @Bind("user_login") String user_login , @Bind("display_name") String display_name)

  /**
   * Deletes user from user table with the given user_id
   * @param user_id The user_id of the user that is to be deleted from the table
   */
  @SqlUpdate( """
              DELETE FROM PR_USER
              WHERE USER_ID = :user_id
              """)
  void deleteUserById(@Bind("user_id") Integer user_id)

  /**
   * Returns the userid of the latest user created using the User sequence
   * @return the userid of the latest user created using the User sequence
   */
  @SqlQuery("""
            SELECT PR_USER_SEQ.CURRVAL FROM DUAL
            """)
  Integer getLatestUserId()
}

