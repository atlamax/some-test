package com.mobcrush.ums.streamwatcher.consumer

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, Envelope}
import org.junit.{Assert, Before, Test}
import org.scalatest.mockito.MockitoSugar._

class CustomConsumerTest {

  var sut: CustomConsumer = _

  val channel: Channel = mock[Channel]

  @Before
  def setUp(): Unit = {
    sut = new CustomConsumer(channel)
  }

  @Test
  def test(): Unit = {
    // given
    val envelope: Envelope = mock[Envelope]
    val properties: BasicProperties = mock[BasicProperties]
    // when
    sut.handleDelivery("", envelope, properties, new String("{\"type\":\"event\"}").getBytes)
    // then
    Assert.assertTrue(true)
  }

}
