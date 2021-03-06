package BasicActor

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

/**
 * Created by junius on 14-12-11.
 */

private class Actor10 extends Actor{
  // The log in akka is ash
  // log also print in console except debug.
  val log = Logging(context.system, this)
  def receive = {
    case "test" => {
      log.debug("test")
      log.error("error")
      log.info("info")
      log.warning("warning")

      // {} can be used as occupant signal. will be replaced by 120

      log.info(" happened at [{}] line", 120)
      log.info(" happened at {} line", 120)
    }
    case _ => log.info("unknown message")
  }
}

object ActorLog {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor10], name = "Actor1")
    actor1 ! "test"
    system.shutdown()

  }
}
