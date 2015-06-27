package edu.oregonstate.mist.webapiskeleton.core

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by georgecrary on 6/26/15.
 */
class User {
  private string display_name;
  private string user_login;
  public User(){
    // Jackson deserialization
  }

  public User(string display_name, string user_login){
    this.display_name = display_name;
    this.user_login = user_login;
  }

  @JsonProperty
  public string getDisplayName(){
    return display_name;
  }

  @JsonProperty
  public string getUserLogin(){
    return user_login;
  }
}
