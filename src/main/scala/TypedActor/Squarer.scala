package TypedActor

/**
 * Created by junius on 14-12-13.
 */

import akka.actor.{ActorSystem, ActorContext, TypedActor, TypedProps}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait Squarer {
  def squareDontCare(i: Int): Unit //fire-forget

  def square(i: Int): Future[Int] //非阻塞 send-request-reply

  def squareNowPlease(i: Int): Option[Int] //阻塞 send-request-reply

  def squareNow(i: Int): Int //阻塞的 send-request-reply

  @throws(classOf[Exception])
  def squareTry(i: Int): Int
}

class SquarerImpl(val name: String) extends Squarer {
  def this() = this("default")
  def squareDontCare(i: Int): Unit = i * i //Nobody cares :(
  def square(i: Int): Future[Int] = Future.successful(i * i)
  def squareNowPlease(i: Int): Option[Int] = Some(i * i)
  def squareNow(i: Int): Int = i * i
  def squareTry(i: Int): Int = throw new Exception("Catch me!")
}

object Squarer {
  def main (args: Array[String]) {
    val system = ActorSystem("Typed")
    val calc: Squarer = TypedActor(system).typedActorOf(TypedProps[SquarerImpl]())

    calc.squareDontCare(1)
    calc.square(3)
    calc.squareNow(5)
    calc.squareNowPlease(7)
    calc.squareTry(9)
    TypedActor(system).stop(calc)
    TypedActor(system).poisonPill(calc)
  }
}
