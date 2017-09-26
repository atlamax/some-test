package com.mobcrush.ums.streamwatcher.service.strategy

import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.Jedis

/**
  * Abstract class with common variables for processing strategy
  */
abstract class AbstractStreamEventProcessStrategy extends StreamEventProcessStrategy {

  private val ELASTICACHE_HOST: String = "X.X.X.X"
  private val ELASTICACHE_PORT: Int = 1111
  private val SQS_API_KEY: String  = "XXXX"
  private val SQS_API_SECRET: String  = "XXXX"


  val SQS_URL: String = "XXXX"
  val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)

  val redisClient: Jedis = new Jedis(ELASTICACHE_HOST, ELASTICACHE_PORT)
  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  private val credentials: AWSCredentials = new BasicAWSCredentials(SQS_API_KEY, SQS_API_SECRET)
  private val credentialsProvider: AWSCredentialsProvider = new AWSStaticCredentialsProvider(credentials)
  private val clientBuilder: AmazonSQSClientBuilder = AmazonSQSClientBuilder.standard();
  clientBuilder.setCredentials(credentialsProvider)
  val sqsClient: AmazonSQS = clientBuilder.build()
}
