package com.mobcrush.ums.streamwatcher.service.strategy
import com.mobcrush.ums.streamwatcher.model.event.StreamEventModel

/**
  * Strategy to finish streaming
  */
object StreamEventFinishStrategy extends AbstractStreamEventProcessStrategy {

  override def process(eventModel: StreamEventModel): Unit = {
    LOGGER.info("Going to REMOVE event in ElastiCache. Id: '{}'", eventModel.payload.id)

    val deletedCount = redisClient.del(eventModel.payload.id)
    if (deletedCount == 0) {
      LOGGER.warn("Record in ElastiCache not exists. Id: '{}'", eventModel.payload.id)
    }
  }

}
