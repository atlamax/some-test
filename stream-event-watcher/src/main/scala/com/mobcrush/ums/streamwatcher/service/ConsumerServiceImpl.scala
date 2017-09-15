package com.mobcrush.ums.streamwatcher.service

import java.io.IOException

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.model.StreamModel
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import org.slf4j.LoggerFactory

/**
  * Service to read messages from RabbitMQ
  */
class ConsumerServiceImpl extends ConsumerService {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val QUEUE_NAME = "stream-events"

  override def process(channel: Channel): Unit = {
    val consumer = new CustomConsumer(channel)

    try {
      channel.basicConsume(QUEUE_NAME, consumer)
    } catch {
      case e: IOException => {
        logger.error("Error occurred during consuming", e)
      }
    }

  }


  private class CustomConsumer(channel: Channel) extends DefaultConsumer(channel: Channel) {

    private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {
      val content: String = new String(body, "UTF-8")
      logger.info("Received message: " + content)
      val streamModel = mapper.readValue(content, classOf[StreamModel])
      logger.info("Parsed Model Id: " + streamModel.id)
    }
  }

}
