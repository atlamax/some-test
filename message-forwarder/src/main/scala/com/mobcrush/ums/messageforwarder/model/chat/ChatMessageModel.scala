package com.mobcrush.ums.messageforwarder.model.chat

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

/**
  * Created by msekerjitsky on 04.10.2017.
  */
@JsonInclude(Include.NON_ABSENT)
case class ChatMessageModel(@JsonProperty("chatroomActivityData") chatroomActivityData: ChatroomActivityData,
                           @JsonProperty("chatroomActivityType") chatroomActivityType: String) {

}
