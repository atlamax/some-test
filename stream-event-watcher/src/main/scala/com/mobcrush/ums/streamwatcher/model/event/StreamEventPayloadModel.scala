package com.mobcrush.ums.streamwatcher.model.event

import com.fasterxml.jackson.annotation.JsonProperty

/**
  * Created by msekerjitsky on 20.09.2017.
  */
case class StreamEventPayloadModel(@JsonProperty("id") id: String,
                                   @JsonProperty("action") action: String,
                                   @JsonProperty("destinations") destinations: List[SocialProviderEventModel]) {

}
