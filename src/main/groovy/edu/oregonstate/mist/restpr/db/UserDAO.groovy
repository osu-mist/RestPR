package edu.oregonstate.mist.restpr.db

import edu.oregonstate.mist.restpr.api.User
import edu.oregonstate.mist.restpr.mapper.UserMapper;

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
            SELECT *
            FROM PR_USER
            WHERE DISPLAY_NAME LIKE '%'||:display_name_match||'%'
            """)

  // || is the concat operator for Oracle
  List<User> getPRUSERSmatch(@Bind("display_name_match") String display_name_match);

  @SqlQuery("""
            SELECT *
            FROM PR_USER
            """)
  List<User> allRESTPRUsers()

  @SqlUpdate("insert into PR_USER (USER_ID, USER_LOGIN, DISPLAY_NAME) values (PLAYER_SEQ.NEXTVAL, :display_name, :user_login)")
  void postUser(@Bind("user_login") String user_login, @Bind("display_name") String display_name);

  @SqlUpdate()
  void putUser(@Bind("user_id") Integer user_id,@Bind("user_login") String user_login, @Bind("display_name") String display_name);

  @SqlQuery("""
            SELECT *
            FROM PR_USER
            WHERE USER_ID = :user_id
            """)
  User getUserById(@Bind("user_id") Integer user_id);
}

