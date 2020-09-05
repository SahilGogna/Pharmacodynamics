package ca.science.server

import java.util.Properties

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import ca.science.kafka.Producer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source

object AkkaServer {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val kafkaTopic = "science1"

  def getRoute(producer: KafkaProducer[Long, String]) = {
    pathEndOrSingleSlash {
      get {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            Source.fromFile("src/main/html/pharma.html").getLines().mkString("")
          )
        )
      }
    } ~
      path("api" / "report") {
        (parameter("sessionId".as[String]) & parameter("time".as[Long])) { (sessionId: String, time: Long) =>
          println(s"I've found session ID $sessionId and time = $time")

          // create a record to send to kafka
          val record = new ProducerRecord[Long, String](kafkaTopic, 0, s"$sessionId,$time")
          producer.send(record)
          producer.flush()

          complete(StatusCodes.OK) // HTTP 200
        }
      }
  }

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    // spinning up the server
    val kafkaProducer = Producer.getProducer()
    val bindingFuture = Http().bindAndHandle(getRoute(kafkaProducer), "localhost", 8888)

    // cleanup
    bindingFuture.foreach { binding =>
      binding.whenTerminated.onComplete(_ => kafkaProducer.close())
    }
  }

}
