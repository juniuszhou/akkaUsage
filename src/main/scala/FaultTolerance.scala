import akka.actor.{AllForOneStrategy, Actor, OneForOneStrategy}
import akka.event.Logging
import akka.actor.SupervisorStrategy._

import scala.concurrent.duration._

/**
 * Created by junius on 14-12-9.
 */


private class Actor4 extends Actor{

  // OneForOneStrategy just for failed actor.

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException => Resume
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception => Escalate
    }

  // AllForOneStrategy for failed actor and its child actor
  val supervisorStrategy2 =
    AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException => Resume
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception => Escalate
    }

  def receive = {
    case "test" => {
      Thread.sleep(2000)
      println("test received")
      sender ! "I am actor1 who got your message."
    }
    case _ => println("unknown message")
  }
}

object FaultTolerance {
  def main (args: Array[String]) {

  }
}
