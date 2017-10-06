package com.mobcrush.ums.messageforwarder.service.strategy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.slf4j.{Logger, LoggerFactory}

/**
  * Abstract class with common components for message processing strategies
  */
abstract class AbstractMessageProcessingStrategy {

  val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)
  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def process(message: String): Unit

}
