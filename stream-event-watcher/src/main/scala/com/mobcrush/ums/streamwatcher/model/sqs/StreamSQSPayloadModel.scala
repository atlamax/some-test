package com.mobcrush.ums.streamwatcher.model.sqs

import com.fasterxml.jackson.annotation.JsonProperty

/**
  * Model that represents payload data structure, store in SQS
  */
case class StreamSQSPayloadModel(@JsonProperty("id") id: String,
                                 @JsonProperty("name") name: String,
                                 @JsonProperty("token") token: String) {

}
