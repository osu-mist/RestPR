package edu.oregonstate.mist.restpr.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by georgecrary on 6/26/15.
 */

@XmlRootElement(name = "user")
class User{
  private Integer user_id
  private String user_login
  private String display_name

  public User(){
    // Jackson deserialization
  }

  public User(Integer user_id, String user_login,String display_name){
    this.user_id = user_id
    this.user_login = user_login
    this.display_name = display_name

  }

  @JsonProperty
  Integer getUserId(){
    return user_id
  }
  @JsonProperty
  void setUserId(UserId){
    this.user_id = UserId
  }


  @JsonProperty
  public String getUserLogin(){
    return user_login
  }

  @JsonProperty
  public String getDisplayName(){
    return display_name
  }

  @JsonProperty
  void setUserLogin(UserLogin){
    this.user_login = UserLogin
  }

  @JsonProperty
  void setDisplayName(DisplayName){
    this.display_name = DisplayName
  }

}
