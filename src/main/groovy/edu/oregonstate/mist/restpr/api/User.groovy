package edu.oregonstate.mist.restpr.api

/**
 * Created by georgecrary on 6/26/15.
 */

class User{
  Integer user_id
  String user_login
  String display_name

  public User(){
    // Jackson deserialization
  }

  public User(Integer user_id, String user_login,String display_name){
    this.user_id = user_id
    this.user_login = user_login
    this.display_name = display_name
  }
}
