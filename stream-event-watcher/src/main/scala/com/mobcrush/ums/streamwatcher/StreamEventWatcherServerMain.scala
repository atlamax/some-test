package com.mobcrush.ums.streamwatcher

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import org.slf4j.LoggerFactory

/**
  * Main class to start application
  */
object StreamEventWatcherServerMain {

  private val logger = LoggerFactory.getLogger(this.getClass.getName)
  private val QUEUE_NAME = "stream-events"

  def main(args: Array[String]) = {
    val connection = connect()
    val channel = connection.createChannel(1)
    logger.info("Start publishing")
    channel.basicPublish("", QUEUE_NAME, true, null, "Hello world".getBytes())
    logger.info("Finish publishing")

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
