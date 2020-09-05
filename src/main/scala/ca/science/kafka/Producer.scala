package ca.science.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{LongSerializer, StringSerializer}

object Producer {

  val topicName = "science1"
  val kafkaBootstrapServer = "localhost:9092"

  val producerProperties = new Properties()
  producerProperties.setProperty(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer
  )
  producerProperties.setProperty(
    ProducerConfig.CLIENT_ID_CONFIG, "MyKafkaProducer"
  )
  producerProperties.setProperty(
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName
  )
  producerProperties.setProperty(
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName
  )

  def getProducer() = new KafkaProducer[Long, String](producerProperties)

}
