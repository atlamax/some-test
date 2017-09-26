package com.mobcrush.ums.streamwatcher.model.event

import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}

/**
  * Created by msekerjitsky on 20.09.2017.
  */
case class StreamEventPayloadModel(@JsonProperty("id") id: String,
                                   @JsonProperty("action") action: String,
                                   @JsonProperty("destinations") destinations: List[SocialProviderEventModel],
                                   @JsonProperty("started") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                   startTime: String,
                                   @JsonProperty("finished") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                   finishTime: String) {

}
