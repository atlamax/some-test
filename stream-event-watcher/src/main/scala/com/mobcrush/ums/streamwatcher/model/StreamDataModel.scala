package com.mobcrush.ums.streamwatcher.model

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

/**
  * Model that represents data structure, stored in ElastiCache
  */
@JsonInclude(Include.NON_NULL)
case class StreamDataModel(@JsonProperty("id") id: String,
                           @JsonProperty("start_time") startTime: Long,
                           @JsonProperty("providers") providers: Option[List[SocialProviderDataModel]] = None) {

}
