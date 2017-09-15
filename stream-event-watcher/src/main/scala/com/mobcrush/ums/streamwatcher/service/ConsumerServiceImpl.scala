package com.mobcrush.ums.streamwatcher.service

import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.model.StreamModel
import com.rabbitmq.client._
import org.slf4j.{Logger, LoggerFactory}

/**
  * Service to read messages from RabbitMQ
  */
class ConsumerServiceImpl extends ConsumerService {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private val QUEUE_NAME: String = "stream-events"
  private val QUEUE_REQUEST_DELAY: Int = 1000

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
        val streamModel = mapper.readValue(content, classOf[StreamModel])
        logger.info("Parsed Model Id: " + streamModel.id)
      } catch {
        case e: Exception => {
          logger.error("Error occurred during parsing stream data: '{" + content + "}'", e)
        }
      } finally {
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }
  }

}
