package edu.oregonstate.mist.restpr.api

/**
 * Created by georgecrary on 7/8/15.
 */
class ErrorPOJO {
  String errorMessage
  Integer errorCode

  public ErrorPOJO(){
    // Jackson deserialization
  }

  public ErrorPOJO(String errorMessage, Integer errorCode){
    this.errorMessage = errorMessage
    this.errorCode = errorCode
  }
}
