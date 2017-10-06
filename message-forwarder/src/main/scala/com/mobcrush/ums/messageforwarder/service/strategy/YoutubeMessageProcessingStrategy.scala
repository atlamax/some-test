package com.mobcrush.ums.messageforwarder.service.strategy

import com.mobcrush.ums.messageforwarder.model.chat.ChatMessageModel
import com.mobcrush.ums.messageforwarder.model.social.YoutubeMessageModel
import com.mobcrush.ums.messageforwarder.service.PublisherService

/**
  * Created by msekerjitsky on 04.10.2017.
  */
object YoutubeMessageProcessingStrategy extends AbstractMessageProcessingStrategy {

  override def process(message: String): Unit = {

    val messageModel = mapper.readValue(message, classOf[YoutubeMessageModel])
    PublisherService.publish(
      convert(messageModel)
    )

  }

  private def convert(messageModel: YoutubeMessageModel): ChatMessageModel = {
    ChatMessageModel(null, messageModel.id)
  }

}
