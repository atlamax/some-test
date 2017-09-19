package com.mobcrush.ums.streamwatcher.model

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Model that represents data structure, available in queue
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StreamEventModel(@JsonProperty("id") id: String,
                            @JsonProperty("event") event: String,
                            @JsonProperty("providers") providers: List[SocialProviderEventModel],
                            @JsonProperty("start_time") startTime: Long) {

}
