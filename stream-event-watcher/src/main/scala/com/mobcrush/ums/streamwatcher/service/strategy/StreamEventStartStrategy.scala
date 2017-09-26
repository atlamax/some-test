package com.mobcrush.ums.streamwatcher.service.strategy

import java.util.Calendar

import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry
import com.mobcrush.ums.streamwatcher.model.data.{SocialProviderDataModel, StreamDataModel}
import com.mobcrush.ums.streamwatcher.model.event.{SocialProviderEventModel, StreamEventModel}
import com.mobcrush.ums.streamwatcher.model.sqs.{StreamSQSModel, StreamSQSPayloadModel}

import scala.collection.JavaConverters._

/**
  * Strategy to start streaming
  */
class StreamEventStartStrategy extends AbstractStreamEventProcessStrategy {

  override def process(eventModel: StreamEventModel): Unit = {
    LOGGER.info("Going to ADD event in ElastiCache. Id: {}", eventModel.payload.id)
    redisClient.set(
      eventModel.payload.id,
      mapper.writeValueAsString(convertToDataModel(eventModel))
    )

    val messages: java.util.List[SendMessageBatchRequestEntry] = eventModel.payload.destinations
      .zipWithIndex
      .map( { case(destination, index) => {
        val message: SendMessageBatchRequestEntry = new SendMessageBatchRequestEntry()
          .withId(index.toString)
          .withMessageBody(mapper.writeValueAsString(convertToSQSModel(eventModel.payload.id, destination)))
        message
      }}).asJava

    sqsClient.sendMessageBatch(SQS_URL, messages)
  }

  /**
    * Converts input model in [[StreamDataModel]]
    *
    * @param eventModel   input model
    *
    * @return instance of [[StreamDataModel]]
    */
  private def convertToDataModel(eventModel: StreamEventModel): StreamDataModel = {
    val providers: List[SocialProviderDataModel] = eventModel.payload.destinations.map(destination => {
      SocialProviderDataModel(destination.id, destination.name, destination.token)
    })
    StreamDataModel(eventModel.payload.id, eventModel.payload.startTime, eventModel.createTime, providers)
  }

  /**
    * Converts input model in [[StreamSQSModel]]
    *
    * @param streamId       stream Id
    * @param providerModel  input model
    *
    * @return instance of [[StreamSQSModel]]
    */
  private def convertToSQSModel(streamId: String, providerModel: SocialProviderEventModel): StreamSQSModel = {
    val payload: StreamSQSPayloadModel = StreamSQSPayloadModel(providerModel.id, providerModel.name,
      providerModel.token)

    StreamSQSModel(streamId, payload, Calendar.getInstance())
  }

}
