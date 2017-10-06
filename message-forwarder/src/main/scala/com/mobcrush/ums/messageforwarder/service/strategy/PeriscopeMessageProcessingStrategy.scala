package com.mobcrush.ums.messageforwarder.service.strategy

import com.mobcrush.ums.messageforwarder.model.chat.ChatMessageModel
import com.mobcrush.ums.messageforwarder.model.social.PeriscopeMessageModel
import com.mobcrush.ums.messageforwarder.service.PublisherService

/**
  * Created by msekerjitsky on 04.10.2017.
  */
object PeriscopeMessageProcessingStrategy extends AbstractMessageProcessingStrategy {

  override def process(message: String): Unit = {

    val messageModel = mapper.readValue(message, classOf[PeriscopeMessageModel])
    PublisherService.publish(
      convert(messageModel)
    )

  }

  private def convert(messageModel: PeriscopeMessageModel): ChatMessageModel = {
    ChatMessageModel(null, messageModel.id)
  }

}
