/**
 * Created by junius on 14-12-6.
 */
package main.scala

import akka.actor.Actor.Receive
import akka.actor._
import akka.event.Logging

import scala.collection.immutable.Iterable
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration

private class Actor1 extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
  }
}

// 构造器参数不能是可变的(var),因为 call-by-name 块可能被其它线程调用,引起条件竞争。
private class Actor2(name: String) extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
  }
}

class WatchActor(val other: ActorRef) extends Actor {

  context.watch(other)
  var lastSender = context.system.deadLetters

  def receive = {
    case Terminated(other) => println(other.toString + " watched dead.")
  }
}

object SimpleActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor1], name = "Actor1")
    actor1 ! "test"
    actor1 ! "other"

    // how to create actor with parameter.
    val actor2 = system.actorOf(Props(new Actor2("actor2")), name = "Actor2")
    actor2 ! "test"
    actor2 ! "other"

    val watchActor = system.actorOf(Props(new WatchActor(actor2)), name = "WatchActor")

  }
}
