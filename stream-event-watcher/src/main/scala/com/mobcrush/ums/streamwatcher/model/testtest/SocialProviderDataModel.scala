package com.mobcrush.ums.streamwatcher.model.testtest
package com.mobcrush.ums.streamwatcher.model.data

import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonFormat, JsonInclude, JsonProperty}

/**
  * Model that represents data structure for social provider event information
  */
@JsonInclude(Include.NON_NULL)
case class SocialProviderDataModel(@JsonProperty("id") id: String,
                                   @JsonProperty("type") providerType: String,
                                   @JsonProperty("access_token") accessToken: String,
                                   @JsonProperty("last_access_time") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                   lastAccessTime: Option[String] = None,
                                   @JsonProperty("last_access_cursor") lastAccessCursor: Option[String] = None) {

}
