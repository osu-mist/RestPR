package edu.oregonstate.mist.restpr.api

/**
 * Created by georgecrary on 7/8/15.
 */
class ErrorPOJO {
  String errorMessage
  Integer errorCode

  boolean equals(o) {
    if (this.is(o)) return true
    if (getClass() != o.class) return false

    ErrorPOJO errorPOJO = (ErrorPOJO) o

    if (errorCode != errorPOJO.errorCode) return false
    if (errorMessage != errorPOJO.errorMessage) return false

    return true
  }

}
