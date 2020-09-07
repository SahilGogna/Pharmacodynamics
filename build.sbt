name := "Pharmacodynamics"

version := "0.1"

scalaVersion := "2.12.10"

val hadoopVersion = "2.7.3"

val sparkVersion = "3.0.0"
val kafkaVersion = "2.4.0"
val akkaVersion = "2.5.24"
val akkaHttpVersion = "10.1.7"
val log4jVersion = "2.4.1"

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion,

  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,


  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,

  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.0",

  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,

  // akka
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

  // logging
  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,


  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.11.0"
)