package com.mobcrush.ums.streamwatcher.service

import java.io.IOException

import com.mobcrush.ums.streamwatcher.consumer.CustomConsumer
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

}
