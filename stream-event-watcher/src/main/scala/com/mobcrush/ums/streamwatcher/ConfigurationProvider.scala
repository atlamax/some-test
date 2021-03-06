package com.mobcrush.ums.streamwatcher

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Configuration provider component
  */
object ConfigurationProvider {

  private val CONFIG_FILE_RUNTIME_PROPERTY_NAME = "config.file"
  private val CONFIG_FILE_DEFAULT_NAME: String = "stream-event-watcher.conf"
  private var config: Config = _

  {
    var configFilePath: String = sys.props(CONFIG_FILE_RUNTIME_PROPERTY_NAME)
    if (configFilePath == null) {
      configFilePath = getDefaultConfigFilePath
    }

    val configFile = new File(configFilePath)
    val fileConfig = ConfigFactory.parseFile(configFile).getConfig("stream-event-watcher")
    config = ConfigFactory.load(fileConfig)
  }

  val RABBITMQ_QUEUE_NAME: String = config.getString("rabbitmq.queue.name")
  val RABBITMQ_CONSUMER_STREAMS_COUNT: Int = getRabbitMQConsumerStreamsCount
  val ELASTICACHE_HOST: String = config.getString("elasticache.host")
  val ELASTICACHE_PORT: Int = config.getInt("elasticache.port")
  val AWS_SQS_URL: String = config.getString("aws.sqs.url")
  val AWS_SQS_API_KEY: String = config.getString("aws.sqs.api-key")
  val AWS_SQS_API_SECRET: String = config.getString("aws.sqs.api-secret")

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
