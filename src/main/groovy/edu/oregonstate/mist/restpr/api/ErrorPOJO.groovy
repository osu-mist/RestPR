package edu.oregonstate.mist.restpr.api

import com.fasterxml.jackson.annotation.JsonProperty

import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by georgecrary on 7/8/15.
 */
@XmlRootElement(name = "errorpojo")
class ErrorPOJO {
  private String errorMessage
  private Integer errorCode

  public ErrorPOJO(){
    // Jackson deserialization
  }


  public ErrorPOJO(String errorMessage, Integer errorCode){
    this.errorMessage = errorMessage
    this.errorCode = errorCode
  }

  @JsonProperty
  public String getErrorMessage(){
    return errorMessage;
  }
  @JsonProperty
  public void setErrorMessage(String errorMessage){
    this.errorMessage = errorMessage
  }

  @JsonProperty
  public Integer getErrorCode(){
    return errorCode;
  }

  @JsonProperty
  public void setErrorCode(Integer errorCode){
    this.errorCode = errorCode;
  }

}
