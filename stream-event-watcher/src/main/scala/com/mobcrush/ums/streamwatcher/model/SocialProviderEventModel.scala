package com.mobcrush.ums.streamwatcher.model

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Model that represents data structure for social provider event information
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class SocialProviderEventModel(@JsonProperty("id") id: String,
                                    @JsonProperty("type") providerType: String) {

}
