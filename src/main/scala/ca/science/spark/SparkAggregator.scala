package ca.science.spark

import org.apache.spark.sql.streaming.{GroupState, GroupStateTimeout, OutputMode}
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkAggregator {

  val spark = SparkSession.builder()
    .appName("Pharmacodynamics")
    .master("local[2]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  case class UserResponse(sessionId: String, responseTime: Long)

  case class UserAvgResponse(sessionId: String, avgDuration: Double)

  def readUserResponse(): Dataset[UserResponse] = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "science1")
    .load()
    .select("value")
    .as[String]
    .map { line =>
      val tokens = line.split(",")
      val sessionId = tokens(0)
      val time = tokens(1).toLong

      UserResponse(sessionId, time)
    }

  def updateUserResponseTime
  (n: Int)
  (sessionId: String, group: Iterator[UserResponse], state: GroupState[List[UserResponse]])
  : Iterator[UserAvgResponse] = {
    group.flatMap { record =>
      val lastWindow =
        if (state.exists) state.get
        else List()

      val windowLength = lastWindow.length
      val newWindow =
        if (windowLength >= n) lastWindow.tail :+ record
        else lastWindow :+ record

      // for Spark to give us access to the state in the next batch
      state.update(newWindow)

      if (newWindow.length >= n) {
        val newAverage = newWindow.map(_.responseTime).sum * 1.0 / n
        Iterator(UserAvgResponse(sessionId, newAverage))
      } else {
        Iterator()
      }
    }
  }

  def getAverageResponseTime(n: Int) = {
    readUserResponse()
      .groupByKey(_.sessionId)
      .flatMapGroupsWithState(OutputMode.Append, GroupStateTimeout.NoTimeout())(updateUserResponseTime(n))
      .map{
        userRes =>
          if(userRes.avgDuration > 1500) s"${userRes.sessionId} is Drunk"
          else s"${userRes.sessionId} is not Drunk"
      }
      .writeStream
      .format("console")
      .outputMode("append")
      .option("truncate", false)
      .start()
      .awaitTermination()
  }

  def logUsers() = {
    readUserResponse().writeStream
      .format("console")
      .outputMode("append")
      .start()
      .awaitTermination()
  }

  def main(args: Array[String]): Unit = {
    getAverageResponseTime(3)
  }

}
