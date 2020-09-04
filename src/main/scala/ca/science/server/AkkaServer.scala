package ca.science.server

import java.util.Properties

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.io.Source

object AkkaServer {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def getRoute() = {
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
          complete(StatusCodes.OK) // HTTP 200
        }
      }
  }

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(getRoute(), "localhost",9988)
  }

}
