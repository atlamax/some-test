package com.mobcrush.ums.streamwatcher.model

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Created by msekerjitsky on 15.09.2017.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StreamModel(@JsonProperty("id") id: String) {

}
