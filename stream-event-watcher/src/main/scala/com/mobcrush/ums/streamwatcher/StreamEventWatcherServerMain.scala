package com.mobcrush.ums.streamwatcher

import com.mobcrush.ums.streamwatcher.service.SubscriberService

/**
  * Main class to start application
  */
object StreamEventWatcherServerMain {

  def main(args: Array[String]): Unit = {
    SubscriberService.process()
  }
}
