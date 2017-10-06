package com.mobcrush.ums.messageforwarder.service

import akka.actor.{ActorRef, ActorSystem, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.messageforwarder.ConfigurationProvider.RABBITMQ_PUBLISH_QUEUE_NAME
import com.mobcrush.ums.messageforwarder.model.chat.ChatMessageModel
import com.spingo.op_rabbit.{Message, RabbitControl, RecoveryStrategy}

/**
  * Created by msekerjitsky on 04.10.2017.
  */
object PublisherService {

  private val ACTOR_SYSTEM_NAME = "message-forwarder-publisher"
  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  implicit val actorSystem: ActorSystem = ActorSystem(ACTOR_SYSTEM_NAME)
  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack()

  def publish(message: ChatMessageModel): Unit = {

    rabbitControl ! Message.queue(
      mapper.writeValueAsString(message),
      queue = RABBITMQ_PUBLISH_QUEUE_NAME
    )
  }

}
