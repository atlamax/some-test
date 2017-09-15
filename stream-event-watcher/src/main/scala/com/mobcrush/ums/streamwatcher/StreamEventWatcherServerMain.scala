package com.mobcrush.ums.streamwatcher

import com.mobcrush.ums.streamwatcher.service.{ConsumerService, ConsumerServiceImpl}
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import org.slf4j.LoggerFactory

/**
  * Main class to start application
  */
object StreamEventWatcherServerMain {

  private val logger = LoggerFactory.getLogger(this.getClass.getName)

  def main(args: Array[String]) = {
    val connection = connect()
    val channel = connection.createChannel()

    val consumerService: ConsumerService = new ConsumerServiceImpl
    consumerService.process(channel)

    disconnect(connection, channel)
  }

  private def connect(): Connection = {
    val connectionFactory = new ConnectionFactory()
    val connection = connectionFactory.newConnection()
    logger.info("Connected")
    connection
  }

  private def disconnect(connection: Connection, channel: Channel) = {
    channel.close()
    connection.close()
    logger.info("Disconnected")
  }

}
