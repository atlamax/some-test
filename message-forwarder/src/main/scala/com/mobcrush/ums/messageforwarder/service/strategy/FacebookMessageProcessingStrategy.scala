package com.mobcrush.ums.messageforwarder.service.strategy

import com.mobcrush.ums.messageforwarder.model.chat.ChatMessageModel
import com.mobcrush.ums.messageforwarder.model.social.FacebookMessageModel
import com.mobcrush.ums.messageforwarder.service.PublisherService

/**
  * Created by msekerjitsky on 04.10.2017.
  */
object FacebookMessageProcessingStrategy extends AbstractMessageProcessingStrategy {

  override def process(message: String): Unit = {

    val messageModel = mapper.readValue(message, classOf[FacebookMessageModel])
    PublisherService.publish(
      convert(messageModel)
    )

  }

  private def convert(messageModel: FacebookMessageModel): ChatMessageModel = {
    ChatMessageModel(null, messageModel.id)
  }

}
