package com.mobcrush.ums.streamwatcher.service

import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.model.{StreamDataModel, StreamEventModel}
import com.mobcrush.ums.streamwatcher.model.enums.StreamEventType
import com.rabbitmq.client._
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.Jedis

/**
  * Service to read messages from RabbitMQ
  */
class ConsumerServiceImpl extends ConsumerService {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private val QUEUE_NAME: String = "stream-events"
  private val QUEUE_REQUEST_DELAY: Int = 1000
  private val ELASTICACHE_HOST: String = "34.202.127.226"
  private val ELASTICACHE_PORT: Int = 6379
  private val JEDIS: Jedis = new Jedis(ELASTICACHE_HOST, ELASTICACHE_PORT)

  override def process(channel: Channel): Unit = {
    val consumer = new CustomConsumer(channel)

    processMessages(channel, consumer)
  }

  /**
    * Process messages one-by-one from queue
    *
    * @param channel  channel
    * @param consumer consumer
    */
  private def processMessages(channel: Channel, consumer: Consumer): Unit = {
    while(channel.queueDeclarePassive(QUEUE_NAME).getMessageCount > 0) {
      processOneMessage(channel, consumer)
    }
    Thread.sleep(QUEUE_REQUEST_DELAY)
    processMessages(channel, consumer)
  }

  /**
    * Process one message
    *
    * @param channel  channel
    * @param consumer consumer
    */
  private def processOneMessage(channel: Channel, consumer: Consumer): Unit = {
    try {
      channel.basicConsume(QUEUE_NAME, false, consumer)
    } catch {
      case e: IOException => {
        logger.error("Error occurred during consuming", e)
      }
    }
  }

  /**
    * Custom Consumer to handle and process one message from queue
    *
    * @param channel  channel
    */
  private class CustomConsumer(channel: Channel) extends DefaultConsumer(channel: Channel) {

    private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {
      val content: String = new String(body, UTF_8.name())
      logger.info("Received message: " + content)

      try {
        val streamModel = mapper.readValue(content, classOf[StreamEventModel])
        logger.info("Parsed Model Id: " + streamModel.id)
      } catch {
        case e: Exception => {
          logger.error("Error occurred during parsing stream data: '{" + content + "}'", e)
        }
      } finally {
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }

    private def processWithElasticache(eventModel: StreamEventModel, rawContent: String): Unit = {
      eventModel.event match {
        case StreamEventType.ADD => {
          logger.info("Going to ADD event in ElastiCache. Id: {}", eventModel.id)
          JEDIS.set(
            eventModel.id,
            mapper.writeValueAsString(convertToDataModel(eventModel))
          )
        }
        case StreamEventType.REMOVE => {
          logger.info("Going to REMOVE event in ElastiCache. Id: {}", eventModel.id)
          JEDIS.del(eventModel.id)
        }
        case _ => {
          logger.error("Unknown event type in stream event '{}'", rawContent)
        }
      }
    }

    private def convertToDataModel(eventModel: StreamEventModel): StreamDataModel = {
      val result: StreamDataModel = StreamDataModel(eventModel.id, eventModel.startTime)
      result
    }

  }

}
