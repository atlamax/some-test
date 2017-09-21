package com.mobcrush.ums.streamwatcher.model.data

import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonFormat, JsonInclude, JsonProperty}

/**
  * Model that represents data structure, stored in ElastiCache
  */
@JsonInclude(Include.NON_NULL)
case class StreamDataModel(@JsonProperty("id") id: String,
                           @JsonProperty("start_time") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                           startTime: String,
                           @JsonProperty("created") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                           createTime: String,
                           @JsonProperty("last_modified") @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                           lastModifiedTime: Option[String] = None,
                           @JsonProperty("providers") providers: Option[List[SocialProviderDataModel]] = None) {

}
