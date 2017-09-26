package com.mobcrush.ums.streamwatcher.service.strategy

import com.mobcrush.ums.streamwatcher.model.event.StreamEventModel

/**
  * Trait for processing strategy
  */
trait StreamEventProcessStrategy {

  /**
    * Process event
    *
    * @param eventModel   event model
    */
  def process(eventModel: StreamEventModel): Unit

}
