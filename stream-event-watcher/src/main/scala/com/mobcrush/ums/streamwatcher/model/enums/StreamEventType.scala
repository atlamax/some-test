package com.mobcrush.ums.streamwatcher.model.enums

/**
  * Enumeration with possible stream event types
  */
object StreamEventType extends Enumeration {

//  type StreamEvent = Value
  val START = Value("StreamStarted")
  val FINISH = Value("StreamFinished")

}
