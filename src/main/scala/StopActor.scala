import akka.actor._
import akka.event.Logging

/**
 * Created by junius on 14-12-8.
 */

private class Actor8 extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "go to hell." => {
      // stop actor by call context stop method.
      context.stop(self)
    }
    case _ => log.info("unknown message")
  }
}

object StopActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor8], name = "Actor8")
    // send poison pill to actor let it stop. asynchronous way
    actor1 ! PoisonPill

    // stop actor in synchronous way
    actor1 ! Kill

    // or terminate (shutdown is deprecated.) whole actor system. then all actors will stop.
    system.terminate()
  }

}
