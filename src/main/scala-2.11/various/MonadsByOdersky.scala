package various

import scala.util.Try

trait MonadByOdersky[T] {
  def flatMap[U](f: T => MonadByOdersky[U]): MonadByOdersky[U]

  def unit(t: T): MonadByOdersky[T]
}

//////////////////////////////////////////////////////////////////////
//
//  OPTION[T] :
//
//  abstract class Option[T] {
//    def flatMap[U](f: T => Option[U]): Option[U] = this match {
//      case Some(x) => f(x)
//      case None => None[U]
//    }
//    def unit(t: T) = Some(t)
//  }
//
//////////////////////////////////////////////////////////////////////

object MonadsByOdersky extends App {

  println {
    """Monad must satisfy 3 laws:
1. Associativity :  (m flatMap f1) flatMap f2 = m flatMap( x => f1(x) flatMap f2 )   // it's like (1+2)+3=1+(2+3)
2. Left Unit :      unit(x) flatMap f = f(x)
3. Right Unit :     m flatMap unit = m
    """
  }

  val maybeOne: Option[Int] = Some(1)

  def timesHundred(i: Int): Option[Int] = Some(i * 100)

  def timesTwo(i: Int): Option[Int] = None //or: Some(i * 2)

  def unit(i: Int): Option[Int] = Some(i)

  println {
    s"""Monad laws for Option[T]:
1. Associativity:  ${(maybeOne flatMap timesTwo flatMap timesHundred) == maybeOne.flatMap(one => timesTwo(one) flatMap timesHundred)}
2. Left Unit:      ${(unit(1) flatMap timesHundred) == timesHundred(1)}
3. Right Unit:     ${(maybeOne flatMap unit) == maybeOne}
"""
  }


  ////////////////////////////////////////////////////////////////////
  //
  //    Try[T] :
  //
  //    object Try{
  //       def apply[T](expr: => T): Try[T] =
  //          try Success(expr)
  //          catch { case NonFatal(e) => Failure(t) }
  //    }
  //
  //    abstract class Try[T] {
  //      def flatMap[U](f: T => Try[U]): Try[U] = this match {
  //        case Success(x) => try f(x) catch {case NonFatal(e) => Failure(e)}
  //        case fail: Failure => fail
  //      }
  //    }
  /////////////////////////////////////////////////////////////////////////////////

  def expr = 1

  def timesHundredTry(i: Int): Try[Int] = {
    /* may throw non-fatal exception before returning : */ Try(i * 100)
  }

  def unitTry(i: => Int) = Try(i)

  println {
    s"""Monad laws for Try[T]:
2. Left Unit:      ${unitTry(expr).flatMap(timesHundredTry) == timesHundredTry(expr)}

But Left unit could fail because: 'unit flatMap' will never throw non-fatal exception

'unit' it just wrapping expression with Try(), so non-fatal expressions are returned as Failure
'timesHundredTry' may throw non-fatal exception before returning anything

Even if Try is not a Monad, it is still good for for-comprehension, cause it has flatMap/map
      """
  }

}
