package com.mobcrush.ums.streamwatcher.consumer

import java.nio.charset.StandardCharsets.UTF_8

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.model.data.StreamDataModel
import com.mobcrush.ums.streamwatcher.model.enums.StreamEventType
import com.mobcrush.ums.streamwatcher.model.event.StreamEventModel
import com.rabbitmq.client._
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.Jedis

/**
  * Custom Consumer to handle and process one message from queue
  */
class CustomConsumer(channel: Channel) extends DefaultConsumer(channel: Channel) {

  private val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)
  private val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  private val ELASTICACHE_HOST: String = "34.202.127.226"
  private val ELASTICACHE_PORT: Int = 6379
  private val JEDIS: Jedis = new Jedis(ELASTICACHE_HOST, ELASTICACHE_PORT)

  override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties,
                              body: Array[Byte]): Unit = {

    val content: String = new String(body, UTF_8.name())
    LOGGER.info("Received message: " + content)

    try {
      val streamModel = MAPPER.readValue(content, classOf[StreamEventModel])
      LOGGER.info("Parsed Model Id: " + streamModel.payload.id)

      processWithElasticache(streamModel, content)
    } catch {
      case e: Exception => {
        LOGGER.error("Error occurred during parsing stream data: '" + content + "'", e)
      }
    } finally {
      channel.basicAck(envelope.getDeliveryTag, false)
    }
  }


  def processWithElasticache(eventModel: StreamEventModel, rawContent: String): Unit = {
    val action = StreamEventType.withName(eventModel.payload.action)

    action match {
      case StreamEventType.START => {
        LOGGER.info("Going to ADD event in ElastiCache. Id: {}", eventModel.payload.id)
        JEDIS.set(
          eventModel.payload.id,
          MAPPER.writeValueAsString(convertToDataModel(eventModel))
        )
      }
      case StreamEventType.FINISH => {
        LOGGER.info("Going to REMOVE event in ElastiCache. Id: {}", eventModel.payload.id)
        JEDIS.del(eventModel.payload.id)
      }
      case _ => {
        LOGGER.error("Unknown event type in stream event '{}'", rawContent)
      }
    }
  }

  def convertToDataModel(eventModel: StreamEventModel): StreamDataModel = {
    val result: StreamDataModel = StreamDataModel(eventModel.payload.id, eventModel.startTime, eventModel.createTime)
    result
  }
}
