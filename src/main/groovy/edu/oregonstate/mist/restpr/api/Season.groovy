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

  public Season(){
    // Jackson deserialization
  }

  Season(Integer season_id , String community_name , String cycle_format , String cycle_count , Integer elo_default_seed , Integer year) {
    this.season_id = season_id
    this.community_name = community_name
    this.cycle_format = cycle_format
    this.cycle_count = cycle_count
    this.elo_default_seed = elo_default_seed
    this.year = year
  }
}

