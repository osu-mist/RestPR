package edu.oregonstate.mist.restpr.mapper

import edu.oregonstate.mist.restpr.api.User
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by georgecrary on 6/30/15.
 */
public class UserMapper implements ResultSetMapper<User>{
  public User map(int i, ResultSet rs, StatementContext sc) throws SQLException{
    User user = new User()

    user.with {

      user_id       = rs.getInt     'USER_ID'
      display_name  = rs.getString  'DISPLAY_NAME'
      user_login    = rs.getString  'USER_LOGIN'

    }

     user
  }
}
