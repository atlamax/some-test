package com.mobcrush.ums.messageforwarder.model.social

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Created by msekerjitsky on 04.10.2017.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class YoutubeMessageModel(@JsonProperty("id") id: String) {

}
