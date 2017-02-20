package various

import java.util.concurrent.Executors

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object FutureKata extends App {

  def takeSomeTime(calcName: String) = for (i <- 1 to 3) {
    print(s"$calcName : $i\n"); Thread.sleep(400)
  }

  case class Name(value: String)
  case class UpperName(value: String)
  case class UpperNameWithSpaces(value: String)

  def makeUp(name: Name): UpperName = {
    takeSomeTime(s"Making up $name")
    UpperName(name.value.toUpperCase)
  }

  def addSpaces(upperName: UpperName): UpperNameWithSpaces = {
    takeSomeTime(s"Adding space to $upperName")
    UpperNameWithSpaces(upperName.value.mkString(" "))
  }

  /**
    * Execution Context
    *
    * It is possible to execute Future's computations in new Thread, Thread pool,
    * or current thread (discouraged cause it's supposed to be in parallel)
    *
    * Futures and Promises always do computations in Execution Context
    *
    * scala.concurrent.ExecutionContext.global - that's global static thread pool
    *
    * the number of max threads is set as VM attributes
    * scala.concurrent.context.minThreads
    * scala.concurrent.context.maxThreads
    *
    */
  val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  val firstFuture: Future[UpperName] = Future {
    makeUp(Name("independent_async_future"))
  }(ec) //not needed when global is implicit

  //makeUp - might be already calculating - we can't be sure

  /**
    * This will calculate parallely in separate thread from global thread pool
    * But we don't know when !!!
    *
    * We can stop and wait for the result: Await.result(firstFuture, 10 seconds)
    */


  /**
    * Synchronous Execution
    *
    * First it completes makeUp: Name => UpperName
    * Then, it starts addSpaces: UpperName => UpperNameWithSpaces
    *
    * If we gave different execution context to map, then thread pool would be different
    * and so synchronous execution would not be guaranteed !!!!
    */

  val syncFuture: Future[UpperNameWithSpaces] = Future {
    makeUp(Name("sync_future"))
  }(ec).map {
    upper => addSpaces(upper)
  }(ec)

  /**
    * Callback
    */
  syncFuture.onComplete {
    case Success(result) => println(s"future finished with $result")
    case Failure(t) => t.printStackTrace(System.out)
  }(ec)


  Thread.sleep(10000) // just to give Futures chance to execute
  // - without waiting for it
}