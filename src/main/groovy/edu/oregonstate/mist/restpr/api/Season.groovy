package edu.oregonstate.mist.restpr.api

/**
 * Created by georgecrary on 7/13/15.
 */
class Season {
  Integer season_id
  String  community_name
  String  cycle_format
  String  cycle_count
  Integer elo_default_seed
  Integer year

  boolean equals(o) {
    if (this.is(o)){
      return true
    }
    if (getClass() != o.class) {
      return false
    }
    Season season = (Season) o

    if (community_name != season.community_name) {
      return false
    }
    if (cycle_count != season.cycle_count) {
      return false
    }
    if (cycle_format != season.cycle_format){
      return false
    }
    if (elo_default_seed != season.elo_default_seed){
      return false
    }
    if (season_id != season.season_id){
      return false
    }
    if (year != season.year){
      return false
    }

    true
  }
}

