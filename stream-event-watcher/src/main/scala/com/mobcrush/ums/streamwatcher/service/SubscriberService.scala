package com.mobcrush.ums.streamwatcher.service

import akka.actor.{ActorRef, ActorSystem, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.ConfigurationProvider._
import com.mobcrush.ums.streamwatcher.model.enums.StreamEventType
import com.mobcrush.ums.streamwatcher.model.event.StreamEventModel
import com.mobcrush.ums.streamwatcher.service.strategy.{StreamEventFinishStrategy, StreamEventStartStrategy}
import com.spingo.op_rabbit._
import org.slf4j.{Logger, LoggerFactory}

/**
  * Service to work with RabbitMQ and processing messages
  */
object SubscriberService {

  import Directives._

  import scala.concurrent.ExecutionContext.Implicits.global

  private val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)
  private val ACTOR_SYSTEM_NAME = "stream-event-watcher"
  private val MAPPER: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  implicit val actorSystem: ActorSystem = ActorSystem(ACTOR_SYSTEM_NAME)
  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack()

  def process(): Unit = {

    Subscription.run(rabbitControl) {
      channel(qos = 3) {
        consume(Queue.passive(RABBITMQ_QUEUE_NAME)) {
          (body(UTF8StringMarshaller) & routingKey) { (rawContent, key) =>

            LOGGER.info("Content: '{}'", rawContent)
            val eventModel: StreamEventModel = MAPPER.readValue(rawContent, classOf[StreamEventModel])
            processMessage(eventModel, rawContent)

            ack
          }
        }
      }

    }
  }

  private def processMessage(eventModel: StreamEventModel, rawContent: String): Unit = {
    val action = StreamEventType.withName(eventModel.payload.action)

    action match {
      case StreamEventType.START => {
        new StreamEventStartStrategy().process(eventModel)
      }
      case StreamEventType.FINISH => {
        new StreamEventFinishStrategy().process(eventModel)
      }
      case _ => {
        LOGGER.error("Unknown event type in stream event '{}'", rawContent)
      }
    }
  }

}
