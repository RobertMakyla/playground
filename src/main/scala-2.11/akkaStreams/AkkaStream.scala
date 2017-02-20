package akkaStreams

import akka.stream._
import akka.stream.scaladsl._

import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

/**
  * http://doc.akka.io/docs/akka/2.4/scala.html
  * http://doc.akka.io/docs/akka/2.4/scala/stream/stream-quickstart.html
  */
object AkkaStream extends App {

  /** The Materializer needs ActorSystem - a context */
  implicit val system = ActorSystem("QuickStartAkkaStream")

  /** The Materializer is a factory for stream execution engines */
  implicit val materializer = ActorMaterializer()

  /** The Source type is parameterized with two types:
    *
    * - The first one is the type of element that this source emits.
    *
    * - The second one may signal that running the source produces some additional value
    * (e.g. a network source may provide information about the bound port or the peer’s address).
    *
    * Where no additional information is produced, the type akka.NotUsed is used
    */
  val source: Source[Int, NotUsed] = Source(1 to 10) // just a description of source

  // we still need to run it:

  source.runForeach(i => println(i))(materializer)
  // println() is consumer function

  /**
    * Source is just a description of what you want to run,
    * we can reuse Source any time:
    */

  //scan() is like a fold() for Streams - nothing computed yet, just a definition
  val factorials: Source[BigInt, NotUsed] = source.scan(BigInt(1))(_ * _)

  /**
    * even if factorial is a val, Source is just a description, so nothing is computed yet
    */

  // we must convert Source[BigInt] into Source[ByteString]
  // because FileIO.toPath() returns consumer: Sink[ByteString, Future[IOResult]]
  val factorialsByteString: Source[ByteString, NotUsed] = factorials.map(num => ByteString(s"$num\n"))

  val result: Future[IOResult] = factorialsByteString.runWith(
    FileIO.toPath(Paths.get("factorials.txt"))
  )
  /**
    * IOResult is a type that IO operations return in Akka Streams in order to tell you how many
    * bytes or elements were consumed and whether the stream terminated normally or exceptionally.
    */


  /**
    * Reusing
    */

  /** In Akka Streams we can reuse not only sources but also consumers (eg: file writing Sink) */

  // Sink[String, Future[IOResult]] - it accepts strings as its input and when materialized it will create
  // additional information of type Future[IOResult]
  def stringSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right) // toMat connects flow with a sink

  private val sourceOfString: Source[String, NotUsed] = factorials.map(_.toString)
  sourceOfString.runWith(stringSink("factorial2.txt"))
}
