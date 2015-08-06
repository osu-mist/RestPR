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
  @SqlQuery("""
            SELECT DISPLAY_NAME, USER_ID, USER_LOGIN
            FROM PR_USER
            WHERE DISPLAY_NAME LIKE '%'||:display_name_match||'%'
            AND USER_LOGIN LIKE '%'||:user_login_match||'%'
            """)

  // || is the concat operator for Oracle
  List<User> getPRUSERSmatch(@Bind("user_login_match") String user_login_match, @Bind("display_name_match") String display_name_match)

  @SqlQuery("""
            SELECT DISPLAY_NAME, USER_ID, USER_LOGIN
            FROM PR_USER
            """)
  List<User> allRESTPRUsers()

  @SqlUpdate("insert into PR_USER (USER_ID, USER_LOGIN, DISPLAY_NAME) values (PR_USER_SEQ.NEXTVAL, :user_login, :display_name)")
  void postUser(@Bind("user_login") String user_login, @Bind("display_name") String display_name)

  @SqlUpdate( """
              UPDATE PR_USER
              SET USER_LOGIN = :user_login , DISPLAY_NAME = :display_name
              WHERE USER_ID = :user_id
              """)
  void putUser(@Bind("user_id") Integer user_id,@Bind("user_login") String user_login, @Bind("display_name") String display_name)

  @SqlQuery("""
            SELECT *
            FROM PR_USER
            WHERE USER_ID = :user_id
            """)
  User getUserById(@Bind("user_id") Integer user_id)

  @SqlUpdate("insert into PR_USER (USER_ID, USER_LOGIN, DISPLAY_NAME) values (:user_id, :user_login, :display_name)")
  void postUserToUserId(@Bind("user_id") Integer user_id , @Bind("user_login") String user_login , @Bind("display_name") String display_name)

  @SqlUpdate( """
              DELETE FROM PR_USER
              WHERE USER_ID = :user_id
              """)
  void deleteUserById(@Bind("user_id") Integer user_id)

  @SqlQuery("""
            SELECT PR_USER_SEQ.CURRVAL FROM DUAL
            """)
  Integer getLatestUserId()
}

