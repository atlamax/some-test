package com.mobcrush.ums.streamwatcher.model.event

import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.{JsonFormat, JsonIgnoreProperties, JsonProperty}

/**
  * Model that represents data structure, available in queue
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StreamEventModel(@JsonProperty("type") eventType: String,
                            @JsonProperty("payload") payload: StreamEventPayloadModel,
                            @JsonProperty("created") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                            createTime: String) {

}
