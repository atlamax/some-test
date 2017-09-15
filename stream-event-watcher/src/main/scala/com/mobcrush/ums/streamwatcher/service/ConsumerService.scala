package com.mobcrush.ums.streamwatcher.service

import com.rabbitmq.client.Channel

/**
  * Interface for service that is responsible for consuming messages from queue
  */
trait ConsumerService {

  /**
    * Start process messages in queue
    *
    * @param channel  channel
    */
  def process(channel: Channel)

}
