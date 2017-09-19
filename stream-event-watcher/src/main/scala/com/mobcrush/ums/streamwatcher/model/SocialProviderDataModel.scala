package com.mobcrush.ums.streamwatcher.model

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

/**
  * Model that represents data structure for social provider event information
  */
@JsonInclude(Include.NON_NULL)
case class SocialProviderDataModel(@JsonProperty("id") id: String,
                                   @JsonProperty("type") providerType: String,
                                   @JsonProperty("access_token") accessToken: String,
                                   @JsonProperty("last_access_time") lastAccessTime: Option[Long] = None,
                                   @JsonProperty("last_access_cursor") lastAccessCursor: Option[String] = None) {

}
