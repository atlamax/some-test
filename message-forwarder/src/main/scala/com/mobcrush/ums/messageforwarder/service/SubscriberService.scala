package com.mobcrush.ums.messageforwarder.service

import akka.actor.{ActorRef, ActorSystem, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.messageforwarder.ConfigurationProvider._
import com.mobcrush.ums.messageforwarder.service.strategy.{FacebookMessageProcessingStrategy, PeriscopeMessageProcessingStrategy, YoutubeMessageProcessingStrategy}
import com.spingo.op_rabbit._
import org.slf4j.{Logger, LoggerFactory}

/**
  * Service to work with RabbitMQ and processing messages
  */
object SubscriberService {

  private val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)
  private val ACTOR_SYSTEM_NAME = "message-forwarder-subscriber"
  private val MAPPER: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  implicit val actorSystem: ActorSystem = ActorSystem(ACTOR_SYSTEM_NAME)
  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack()

  def process(): Unit = {

    import Directives._

    import scala.concurrent.ExecutionContext.Implicits.global

    Subscription.run(rabbitControl) {

      channel(qos = RABBITMQ_CONSUMER_STREAMS_COUNT) {
        consume(Queue.passive(RABBITMQ_YOUTUBE_QUEUE_NAME)) {
          body(UTF8StringMarshaller) { (rawContent) =>

            LOGGER.info("YouTube content: '{}'", rawContent)
            YoutubeMessageProcessingStrategy.process(rawContent)

            ack
          }
        }
      }
    }

    Subscription.run(rabbitControl) {
      channel(qos = RABBITMQ_CONSUMER_STREAMS_COUNT) {
        consume(Queue.passive(RABBITMQ_FACEBOOK_QUEUE_NAME)) {
          body(UTF8StringMarshaller) { (rawContent) =>

            LOGGER.info("Facebook content: '{}'", rawContent)
            FacebookMessageProcessingStrategy.process(rawContent)

            ack
          }
        }
      }
    }

    Subscription.run(rabbitControl) {
      channel(qos = RABBITMQ_CONSUMER_STREAMS_COUNT) {
        consume(Queue.passive(RABBITMQ_PERISCOPE_QUEUE_NAME)) {
          body(UTF8StringMarshaller) { (rawContent) =>

            LOGGER.info("Periscope content: '{}'", rawContent)
            PeriscopeMessageProcessingStrategy.process(rawContent)

            ack
          }
        }
      }
    }

  }

}
