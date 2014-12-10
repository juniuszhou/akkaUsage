import akka.actor.Actor
import akka.event.Logging

/**
 * Created by junius on 14-12-8.
 */

// junius example to show how to change the behavior of actor's receiver.
private class Actor3 extends Actor{
  // junius must import context. become belongs to this package.
  import context._
  val log = Logging(context.system, this)
  def angryState: Receive = {
    case "test" => println("angry")
    case _ => log.info("unknown message")
  }

  def happyState: Receive = {
    case "test" => println("happy")
    case _ => log.info("unknown message")
  }

  def receive = {
    case "test" => become(happyState)
    case _ =>      become(angryState)
  }
}

object ReceiveSwitch {
  def main (args: Array[String]) {

  }
}
