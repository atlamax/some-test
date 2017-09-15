package com.mobcrush.ums.streamwatcher.service

import com.rabbitmq.client.Channel

/**
  * Interface for service that is responsible for consuming messages from queue
  */
trait ConsumerService {

  def process(channel: Channel)

}
