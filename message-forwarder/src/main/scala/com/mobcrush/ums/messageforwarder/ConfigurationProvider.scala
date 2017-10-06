package com.mobcrush.ums.messageforwarder

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Configuration provider component
  */
object ConfigurationProvider {

  private val CONFIG_FILE_RUNTIME_PROPERTY_NAME = "config.file"
  private val CONFIG_FILE_DEFAULT_NAME: String = "message-forwarder.conf"
  private var config: Config = _

  {
    var configFilePath: String = sys.props(CONFIG_FILE_RUNTIME_PROPERTY_NAME)
    if (configFilePath == null) {
      configFilePath = getDefaultConfigFilePath
    }

    val configFile = new File(configFilePath)
    val fileConfig = ConfigFactory.parseFile(configFile).getConfig("message-forwarder")
    config = ConfigFactory.load(fileConfig)
  }

  val RABBITMQ_YOUTUBE_QUEUE_NAME: String = config.getString("rabbitmq.youtube.queue.name")
  val RABBITMQ_FACEBOOK_QUEUE_NAME: String = config.getString("rabbitmq.facebook.queue.name")
  val RABBITMQ_PERISCOPE_QUEUE_NAME: String = config.getString("rabbitmq.periscope.queue.name")
  val RABBITMQ_PUBLISH_QUEUE_NAME: String = config.getString("rabbitmq.publisher.queue.name")
  val RABBITMQ_CONSUMER_STREAMS_COUNT: Int = getRabbitMQConsumerStreamsCount

  /**
    * Build default path to configuration file
    *
    * @return path to configuration file
    */
  private def getDefaultConfigFilePath: String = {
    sys.props("user.home") + sys.props("file.separator") + CONFIG_FILE_DEFAULT_NAME
  }

  /**
    * Gets number of parallel streams for RabbitMQ consumer
    * Value can be configured via properties, or get as number of CPU cores
    *
    * @return number of parallel streams for RabbitMQ consumer
    */
  private def getRabbitMQConsumerStreamsCount: Int = {
    if (config.hasPath("rabbitmq.consumer.streams-count")) {
      config.getInt("rabbitmq.consumer.streams-count")
    } else {
      Runtime.getRuntime.availableProcessors
    }
  }

}
