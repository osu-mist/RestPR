package edu.oregonstate.mist.restpr.api

/**
 * Created by georgecrary on 6/26/15.
 */

class User{
  Integer user_id
  String user_login
  String display_name

  boolean equals(o) {
    if (this.is(o)) return true
    if (getClass() != o.class) return false

    User user = (User) o

    if (display_name != user.display_name) return false
    if (user_id != user.user_id) return false
    if (user_login != user.user_login) return false

    return true
  }
}
