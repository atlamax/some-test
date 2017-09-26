package com.mobcrush.ums.streamwatcher.model.sqs

import java.util.{Calendar, Date}

import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}

/**
  * Model that represents data structure, stored in SQS
  */
case class StreamSQSModel(@JsonProperty("id") id: String,
                          @JsonProperty("payload") payload: StreamSQSPayloadModel,
                          @JsonProperty("created") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                          createTime: Calendar) {

}
