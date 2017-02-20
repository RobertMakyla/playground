name := "playground"

version := "1.0"

scalaVersion := "2.11.7"

// scala test
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"

// akka actors

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.9-RC2"

// akka streams
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.4.9-RC2"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.9-RC2"