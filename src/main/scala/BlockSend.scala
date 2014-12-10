import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
/**
 * Created by junius on 14-12-7.
 */

private class Actor5 extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => {
      Thread.sleep(2000)
      println("test received")
      sender ! "I am actor1 who got your message."
    }
    case _ => log.info("unknown message")
  }
}

object BlockSend {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor5], name = "Actor1")

    implicit val timeout = Timeout(5 seconds)
    // val future1: Future[String] = ask(actor1, "test").mapTo[String]

    // junius if send message via ? , then sender will get a message back from
    // target actor via a future method. so it actually not block the main thread
    // execution. of course it is not like ! don't care if target actor got/handle it at all.
    val future1: Future[String] = (actor1 ? "test").mapTo[String]
    // future1 pipeTo actor1
    future1.onSuccess {
      case str => println(str + " success")
      case _ => println("default handler")
    }

    // future could be pipe to other actor.
    future1 pipeTo system.actorOf(Props[Actor5], name = "Actor2")

    println("I am main")
    Thread.sleep(5000)
    println("over")
  }
}
