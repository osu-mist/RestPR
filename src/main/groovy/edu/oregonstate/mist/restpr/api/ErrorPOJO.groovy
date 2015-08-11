package edu.oregonstate.mist.restpr.api

import com.fasterxml.jackson.annotation.JsonProperty
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by georgecrary on 7/8/15.
 */
@XmlRootElement(name = "errorpojo")
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
