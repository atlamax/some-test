package com.mobcrush.ums.streamwatcher.service.strategy

import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.ums.streamwatcher.ConfigurationProvider._
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.Jedis

/**
  * Abstract class with common variables for processing strategy
  */
abstract class AbstractStreamEventProcessStrategy extends StreamEventProcessStrategy {

  val SQS_URL: String = AWS_SQS_URL
  val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)

  val redisClient: Jedis = new Jedis(ELASTICACHE_HOST, ELASTICACHE_PORT)
  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  private val credentials: AWSCredentials = new BasicAWSCredentials(AWS_SQS_API_KEY, AWS_SQS_API_SECRET)
  private val credentialsProvider: AWSCredentialsProvider = new AWSStaticCredentialsProvider(credentials)
  private val clientBuilder: AmazonSQSClientBuilder = AmazonSQSClientBuilder.standard()
  clientBuilder.setCredentials(credentialsProvider)
  val sqsClient: AmazonSQS = clientBuilder.build()
}
