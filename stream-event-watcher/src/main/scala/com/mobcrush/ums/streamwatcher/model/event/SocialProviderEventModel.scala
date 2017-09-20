package com.mobcrush.ums.streamwatcher.model.event

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Model that represents data structure for social provider event information
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class SocialProviderEventModel(@JsonProperty("id") id: String,
                                    @JsonProperty("name") name: String,
                                    @JsonProperty("token") token: String) {

}
