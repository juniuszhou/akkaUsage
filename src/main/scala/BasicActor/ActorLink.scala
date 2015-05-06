package BasicActor

import akka.actor.{Actor, ActorSystem, Props}

/**
 * Created by junius on 14-12-9.
 */

private abstract class GenericActor extends Actor {
  def specificHandler : Receive

  def genericHandler: Receive = {
    case "general" => println("handled by generic actor")
  }

  // junius link two actor via orElse method.
  // then each actor just focus one type of message.
  def receive = specificHandler orElse genericHandler
}

private class Actor6 extends GenericActor{
  override def specificHandler = {
    case "special" => println("handled by specific actor")
  }
}


object ActorLink {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor6], name = "Actor1")
    actor1 ! "special"
    actor1 ! "general"
    Thread.sleep(1000)
  }
}
