package com.mobcrush.ums.messageforwarder

import com.mobcrush.ums.messageforwarder.service.SubscriberService

/**
  * Main class to start application
  */
object MessageForwarderServerMain {

  def main(args: Array[String]): Unit = {
    SubscriberService.process()
  }
}
