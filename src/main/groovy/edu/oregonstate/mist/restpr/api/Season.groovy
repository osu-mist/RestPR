package edu.oregonstate.mist.restpr.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by georgecrary on 7/13/15.
 */
@XmlRootElement(name = "season")
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

  Season(Integer season_id , String community_name , String cycle_format , String cycle_count , Integer elo_default_seed) {
    this.season_id = season_id
    this.community_name = community_name
    this.cycle_format = cycle_format
    this.cycle_count = cycle_count
    this.elo_default_seed = elo_default_seed
  }

  @JsonProperty
  Integer getSeason_id() {
    season_id
  }

  @JsonProperty
  void setSeason_id(Integer season_id) {
    this.season_id = season_id
  }

  @JsonProperty
  String getCommunity_name() {
    community_name
  }

  @JsonProperty
  void setCommunity_name(String community_name) {
    this.community_name = community_name
  }

  @JsonProperty
  String getCycle_format() {
    cycle_format
  }

  @JsonProperty
  void setCycle_format(String cycle_format) {
    this.cycle_format = cycle_format
  }

  @JsonProperty
  String getCycle_count() {
    cycle_count
  }

  @JsonProperty
  void setCycle_count(String cycle_count) {
    this.cycle_count = cycle_count
  }

  @JsonProperty
  Integer getElo_default_seed() {
    elo_default_seed
  }

  @JsonProperty
  void setElo_default_seed(Integer elo_default_seed) {
    this.elo_default_seed = elo_default_seed
  }

  @JsonProperty
  Integer getYear() {
    year
  }

  @JsonProperty
  void setYear(Integer year) {
    this.year = year
  }

}

