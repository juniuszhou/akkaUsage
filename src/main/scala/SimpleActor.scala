/**
 * Created by junius on 14-12-6.
 */
package main.scala

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging

class Actor1 extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
  }
}

// 构造器参数不能是可变的(var),因为 call-by-name 块可能被其它线程调用,引起条件竞争。
class Actor2(name: String) extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
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

  }
}
